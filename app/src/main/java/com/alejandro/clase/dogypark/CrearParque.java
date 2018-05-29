package com.alejandro.clase.dogypark;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CrearParque extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101 ;
    private static final int REQUEST_IMAGE_CAPTURE= 100;
    private Button btn_cancelar, btn_guardar;
    private EditText editTextName, editTextCar;
    Uri uriParkImage;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.clase.dogypark";
    public static final String URLPREF= "url_imagen_parque";
    private ImageView imageView;

    MapsActivity latlong = new MapsActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_parque);

        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        btn_cancelar= findViewById(R.id.btn_atras);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Volver = new Intent(getApplicationContext() , SecondActivity.class);
                startActivity(Volver);
            }
        });

        imageView= findViewById(R.id.imagepark);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editTextName=  findViewById(R.id.editText);
        editTextCar=  findViewById(R.id.editText2);

        btn_guardar=  findViewById(R.id.btn_guardarparque);
        btn_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveParkInformation();
                Intent Volver = new Intent(getApplicationContext() , SecondActivity.class);
                startActivity(Volver);
            }
        });
    }

    private void showImageChooser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona opcion:")
                .setItems(R.array.options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                                break;
                            case 1:
                                Intent intent= new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Selecciona imagen del parque"),CHOOSE_IMAGE);
                                break;
                        };
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    private void saveParkInformation(){

        String nombreparque= editTextName.getText().toString().trim();
        String descripcion= editTextCar.getText().toString().trim();
        //String ubicacion= latlong.getmLastKnownLocation().toString().trim();
        Localizacion loc= new Localizacion(latlong.getmLastKnownLocation().getLatitude(),latlong.getmLastKnownLocation().getLongitude());
        Log.d("latlong", "probando longitud y latitud: "+latlong.getmLastKnownLocation());

        uploadFile();

        ParkInformation parkInformation= new ParkInformation(nombreparque,descripcion,loc);

        //FirebaseUser user= firebaseAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Parques");
        String parqueId = mDatabase.push().getKey();
        mDatabase.child(parqueId).setValue(parkInformation);
        //databaseReference.child("Parques").child(nombreparque).setValue(parkInformation);
        Toast.makeText(this,"Parque Guardado", Toast.LENGTH_SHORT).show();

    }
    private void uploadFile(){

        if (uriParkImage!=null) {

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Subiendo...");
            progressDialog.show();


            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


            String uid= "imagen_"+ editTextName.getText().toString().trim();
            StorageReference ref = storageReference.child("parques/"+uid+".jpg");

            ref.putFile(uriParkImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Uri descarga= taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(),"Foto actualizada", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putString(URLPREF, descarga.toString());
                            preferencesEditor.apply();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0* taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int)progress)+" %");

                        }
                    });
        }else{
            Toast.makeText(CrearParque.this,"No hay foto seleccionada", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!= null && data.getData()!=null){

            uriParkImage=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uriParkImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class settings extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_IMAGE = 101 ;
    private static final int REQUEST_IMAGE_CAPTURE= 100;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextNick,editTextBio;
    private Button btnGuardar;
    private ImageView imageView;
    Uri uriProfileImage;
    private Button btnsalir;
    private FirebaseAuth firebaseAuth;
    Spinner spinner;
    private StorageReference storageReference;


    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.clase.dogypark";
    public static final String URLPREF= "url_imagen_perfil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth= FirebaseAuth.getInstance();

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] sexo = {"Hombre","Mujer","Otros"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexo));

        databaseReference= FirebaseDatabase.getInstance().getReference();

        imageView=(ImageView) findViewById(R.id.foto);
        editTextName= (EditText) findViewById(R.id.editTextName);
        editTextNick= (EditText) findViewById(R.id.editTextNick);
        editTextBio= (EditText) findViewById(R.id.editTextBio);

        btnGuardar= (Button) findViewById(R.id.guardar);
        btnsalir= (Button) findViewById(R.id.salir);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        loadUserInformation();

        btnGuardar.setOnClickListener(this);
        btnsalir.setOnClickListener(this);
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

    private void uploadFile(){

        if (uriProfileImage!=null) {

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Subiendo...");
            progressDialog.show();

            /*StorageReference islandRef = storageReference.child("images/island.jpg");
            islandRef.getDownloadUrl();*/

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String uid= user.getUid();
            StorageReference ref = storageReference.child("perfiles/"+uid+".jpg");

            ref.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Uri descarga= taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(),"Foto actualizada", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putString(URLPREF, descarga.toString());
                            preferencesEditor.apply();

                            Glide.with(settings.this)
                                    .load(descarga)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imageView);
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
            Toast.makeText(settings.this,"No hay foto seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!= null && data.getData()!=null){

            uriProfileImage=data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
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

    private void saveUserInformation(){

        String nombre= editTextName.getText().toString().trim();
        String nickname= editTextNick.getText().toString().trim();
        String biografia= editTextBio.getText().toString().trim();
        String sexo=spinner.getSelectedItem().toString().trim();

        uploadFile();

        UserInformation userInformation= new UserInformation(nombre,nickname,biografia,sexo);

        FirebaseUser user= firebaseAuth.getCurrentUser();
        databaseReference.child("Usuarios").child(user.getUid()).setValue(userInformation);
        Toast.makeText(this,"Guardado", Toast.LENGTH_SHORT).show();

    }
    private void loadUserInformation(){

        FirebaseUser user= firebaseAuth.getCurrentUser();
        DatabaseReference dbNombre =
                FirebaseDatabase.getInstance().getReference()
                        .child("Usuarios").child(user.getUid());


        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("perfiles/"+user.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //TODO cargar imagen en imageView
                //imageView.setImageURI(storageRef);
                Glide.with(settings.this)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("User", exception.getMessage());
            }
        });


        dbNombre.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoadUser loadUser = dataSnapshot.getValue(LoadUser.class);

                editTextName.setText(loadUser.getNombre());
                editTextNick.setText(loadUser.getNickname());
                editTextBio.setText(loadUser.getBiografia());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "Error!", databaseError.toException());
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view==btnGuardar){
            saveUserInformation();
        }

        if (view==btnsalir){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

    }
}

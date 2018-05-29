package com.alejandro.clase.dogypark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class Registro2 extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101 ;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextNick,editTextBio;
    private Button btnGuardar;
    private ImageView imageView;
    Uri uriProfileImage;
    private FirebaseAuth firebaseAuth;
    Spinner spinner;
    private StorageReference storageReference;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.clase.dogypark";
    public static final String URLPREF= "url_imagen_perfil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        firebaseAuth= FirebaseAuth.getInstance();

        spinner = findViewById(R.id.spinner2);
        String[] sexo = {"Hombre","Mujer","Otros"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexo));

        databaseReference= FirebaseDatabase.getInstance().getReference();

        imageView= findViewById(R.id.foto2);
        editTextName=  findViewById(R.id.editTextName2);
        editTextNick=  findViewById(R.id.editTextNick2);
        editTextBio=  findViewById(R.id.editTextBio2);

        btnGuardar=  findViewById(R.id.guardar2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        btnGuardar.setOnClickListener(this);
    }

    private void showImageChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona imagen de perfil"),CHOOSE_IMAGE);
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

            final Context context =this;
            ref.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Uri descarga= taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(),"Foto actualizada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context,SecondActivity.class));

                            /*SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putString(URLPREF, descarga.toString());
                            preferencesEditor.apply();

                            Glide.with(Registro2.this)
                                    .load(descarga)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imageView);*/
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
            Toast.makeText(Registro2.this,"No hay foto seleccionada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SecondActivity.class));
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
        //Toast.makeText(this,"Guardado", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(View view) {
        if (view==btnGuardar){
            saveUserInformation();
        }
    }
}

package com.alejandro.clase.dogypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity implements View.OnClickListener{

    EditText usuario;
    EditText pass;
    Button Btn_iniciar;


    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth= FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        Btn_iniciar=(Button) findViewById(R.id.Btn_iniciar);
        usuario=(EditText) findViewById(R.id.usuario);
        pass=(EditText) findViewById(R.id.password);

        Btn_iniciar.setOnClickListener(this);
    }

    private void registrar(){
        String email= usuario.getText().toString().trim();
        String password= pass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email vacio
            Toast.makeText(this,"Email vacio", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //pass vacio
            Toast.makeText(this,"Contraseña vacia", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(Registro.this,"Usuario registrado", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),Registro2.class));
                            //startActivity(new Intent(getApplicationContext(),SecondActivity.class));
                        }else {
                            Toast.makeText(Registro.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Log.w("0", "createUserWithEmail:failure", task.getException());
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view== Btn_iniciar){

            registrar();
        }
    }
}

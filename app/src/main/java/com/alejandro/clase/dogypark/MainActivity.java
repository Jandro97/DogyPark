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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText usuario;
    EditText pass;
    Button Btn_iniciar;
    TextView tv_registrar;

    public Errores errores;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        errores= new Errores();

        if (firebaseAuth.getCurrentUser() != null ){
            finish();
            startActivity(new Intent(getApplicationContext(),SecondActivity.class));
        }

        progressDialog=new ProgressDialog(this);

        tv_registrar=findViewById(R.id.tv_reg);
        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg= new Intent(MainActivity.this, Registro.class);
                MainActivity.this.startActivity(intentReg);
            }
        });

        Btn_iniciar=(Button) findViewById(R.id.Btn_iniciar);
        usuario=(EditText) findViewById(R.id.usuario);
        pass=(EditText) findViewById(R.id.password);

        Btn_iniciar.setOnClickListener(this);
    }


    private void userLogin (){
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

        progressDialog.setMessage("Logueando");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),SecondActivity.class));

                        }else {
                            // Toast.makeText(MainActivity.this,task.getException().getCause().toString(), Toast.LENGTH_LONG).show();
                         //   Log.w("0", "createUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthException e) {
                                Toast.makeText(MainActivity.this,errores.getToastString(e), Toast.LENGTH_LONG).show();

                            } catch(Exception e) {
                                Log.e("0", e.getMessage());
                            }
                        }   

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view== Btn_iniciar){
            userLogin();
        }
    }

}

package com.alejandro.clase.dogypark;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageReference;

public class infomarker extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView TextName, TextDesc;
    private Context context;
    private ImageView favorito;
    private String key;
    private LoadUser loadUser;
    private int i =0;
    ParkInformation infoWindowData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomarker);

        TextName = findViewById(R.id.txNP);
        TextDesc = findViewById(R.id.TXDP);
        favorito = findViewById(R.id.imageView3);

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoritos(true);
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference dbNombre =
                FirebaseDatabase.getInstance().getReference()
                        .child("Usuarios").child(user.getUid());
        dbNombre.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadUser=dataSnapshot.getValue(LoadUser.class);
                favoritos(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ImageView img = findViewById(R.id.imagepark);

        Intent intent= getIntent();

        infoWindowData = new ParkInformation();
        infoWindowData=infoWindowData.toObject(intent.getStringExtra("info"));
        key = intent.getStringExtra("key");
        Log.d("INFOMARKER", key);

        String nnombreimagen= infoWindowData.getNombreparque();

        Log.e("NombreImg", nnombreimagen);

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("parques/imagen_"+nnombreimagen+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(infomarker.this)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("User", exception.getMessage());
            }
        });
        /*int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());*/
        //img.setImageResource(imageId);

        TextName.setText(infoWindowData.getNombreparque());
        TextDesc.setText(infoWindowData.getDescripcion());

    }

    public boolean favoritos(boolean onClick){

        for ( i=0 ; i<loadUser.getFavoritos().size(); i++){
            if (infoWindowData.getNombreparque().equals(loadUser.getFavoritos().get(i).getnombreparque())) {
                favorito.setImageResource(R.drawable.favoritomarcado);
                if (onClick)

                removeFav();
                Log.e("favorito", "quitando favoritos");
                return true;
            }

        }
        favorito.setImageResource(R.drawable.fav);

        if(onClick)
            saveFav();
        Log.e("favorito", "añadiendo favoritos");
        return false;
    }

    private void saveFav(){

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid= user.getUid();

        Parquesfavoritos parkfav= new Parquesfavoritos(key,infoWindowData.getNombreparque(), infoWindowData.getLoc());

        loadUser.getFavoritos().add(parkfav);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");
        mDatabase.child(uid).setValue(loadUser);
        favorito.setImageResource(R.drawable.favoritomarcado);

        Toast.makeText(this,"Favorito Guardado", Toast.LENGTH_SHORT).show();

    }
    //TODO retocar el remove porque borra todos los datos de favoritos
    private void removeFav(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid= user.getUid();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid);
        for (int i=0; i < loadUser.getFavoritos().size() ; i++){
            Log.d("INFOMARKER FOR", loadUser.getFavoritos().get(i).getIdparque());
            if(loadUser.getFavoritos().get(i).getIdparque().equals(key)){
                loadUser.getFavoritos().remove(i);
                databaseReference.setValue(loadUser);
            }
        }
        favorito.setImageResource(R.drawable.fav);
    }
}

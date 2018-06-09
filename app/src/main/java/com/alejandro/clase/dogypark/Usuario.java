package com.alejandro.clase.dogypark;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class Usuario extends Fragment{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView TextName, TextNick, TextBio;
    private ImageView mImageView;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.clase.dogypark";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        View view= inflater.inflate(R.layout.activity_usuario, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mContext=this.getContext();

        TextName= view.findViewById(R.id.Txtname);
        TextNick= view.findViewById(R.id.Txtnick);
        TextBio= view.findViewById(R.id.Txtbio);
        mImageView= view.findViewById(R.id.foto);
        mPreferences = this.getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        loadProfile();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

    public void loadProfile(){
        /*Uri descarga= Uri.parse(mPreferences.getString(settings.URLPREF,""));
        Glide.with(Usuario.this)
                .load(descarga)
                .fitCenter()
                .centerCrop()
                .into(mImageView);*/

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
                Glide.with(Usuario.this)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .into(mImageView);
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

                TextName.setText(loadUser.getNombre());
                TextNick.setText(loadUser.getNickname());
                TextBio.setText(loadUser.getBiografia());

                mAdapter= new MyAdapter(loadUser.getFavoritos(), mContext);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "Error!", databaseError.toException());
            }
        });
    }

}

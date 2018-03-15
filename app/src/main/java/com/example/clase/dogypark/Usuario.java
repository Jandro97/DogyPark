package com.example.clase.dogypark;


import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;


public class Usuario extends Fragment{

    private ImageView mImageView;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.clase.dogypark";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_usuario, container, false);

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
        Uri descarga= Uri.parse(mPreferences.getString(settings.URLPREF,""));
        Glide.with(Usuario.this)
                .load(descarga)
                .fitCenter()
                .centerCrop()
                .into(mImageView);
    }

}

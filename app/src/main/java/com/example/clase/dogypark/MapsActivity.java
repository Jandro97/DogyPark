package com.example.clase.dogypark;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    FusedLocationProviderClient mFusedLocationProviderClient;
    public static Location mLastKnownLocation;
    FloatingActionButton floatbutton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.maplayout, container, false);

        floatbutton=(FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        floatbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent CrearParque = new Intent(getContext(), CrearParque.class);
                startActivity(CrearParque);
            }
        });

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        Log.d("MAP", "Entrando en onCreateView");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return rootView;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MAP", "Entrando en onMapReady");

        mMap = googleMap;
        int n = 0;
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        checkLocationPermission();

    }

    public boolean checkLocationPermission() {
        Log.d("MAP", "Entrando en CheckLocationPermission");
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("MAP", "Explicando Permisos");

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.title_location_permission)
                        .setMessage("Esta aplicacion necesita los permisos de localizacion")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {

                Log.d("MAP", "Pidiendo Permisos");
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            Log.d("MAP", "Tiene Permisos");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            getDeviceLocation();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("MAP", "Entrando en onRequestPermission");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        getDeviceLocation();
                        Toast.makeText(getContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getContext(), "permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void getDeviceLocation() {
        Log.d("deviceloc", "entrando en getDeviceLocation");
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            mLastKnownLocation = (Location) task.getResult();
                            //TODO supervisar comportamiento movecamera...a veces da error
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15));
                            Log.d("deviceloc", "cargando posicion:"+mLastKnownLocation.getLatitude()+", "+mLastKnownLocation.getLongitude());
                        } else {
                            Log.d("deviceloc", "Current location is null. Using defaults.");
                            Log.e("deviceloc", "Exception: %s", task.getException());
                            LatLng latLng= new LatLng(0,0);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public Location getmLastKnownLocation(){
        return mLastKnownLocation;
    }
}

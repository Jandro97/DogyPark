package com.alejandro.clase.dogypark;

import com.google.gson.Gson;

/**
 * Created by clase on 06/04/2018.
 */

public class ParkInformation {

    public String nombreparque;
    public String descripcion;
    public Localizacion loc;
    public String key;

    public ParkInformation(){

    }

    public ParkInformation(String nombreparque, String descripcion, Localizacion loc, String key) {
        this.nombreparque = nombreparque;
        this.descripcion = descripcion;
        this.loc = loc;
        this.key = key;
    }

    public ParkInformation(String nombreparque, String descripcion, Localizacion loc) {
        this.nombreparque = nombreparque;
        this.descripcion = descripcion;
        this.loc = loc;
    }

    public String getNombreparque() {
        return nombreparque;
    }

    public void setNombreparque(String nombreparque) {
        this.nombreparque = nombreparque;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Localizacion getLoc() {
        return loc;
    }

    public void setLoc(Localizacion loc) {
        this.loc = loc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ParkInformation toObject(String item){
        Gson gson = new Gson();
        return gson.fromJson(item, ParkInformation.class);
    }
}

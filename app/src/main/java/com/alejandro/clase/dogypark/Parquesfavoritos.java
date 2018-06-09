package com.alejandro.clase.dogypark;

import com.google.gson.Gson;

/**
 * Created by clase on 10/05/2018.
 */

public class Parquesfavoritos {
    public String idparque;
    public  String nombreparque;
    public Localizacion loc;

    public Parquesfavoritos(){}

    public Parquesfavoritos(String idparque, String nombreparque, Localizacion loc){
        this.idparque = idparque;
        this.nombreparque = nombreparque;
        this.loc= loc;
    }

    public String getIdparque() {
        return idparque;
    }

    public void setIdparque(String idparque) {
        this.idparque = idparque;
    }

    public String getnombreparque() {
        return nombreparque;
    }

    public void setnombreparque(String nombreparque) {
        this.nombreparque = nombreparque;
    }

    public Localizacion getLoc() {
        return loc;
    }

    public void setLoc(Localizacion loc) {
        this.loc = loc;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Parquesfavoritos toObject(String item){
        Gson gson = new Gson();
        return gson.fromJson(item, Parquesfavoritos.class);
    }
}

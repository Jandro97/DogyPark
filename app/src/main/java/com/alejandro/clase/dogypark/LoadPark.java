package com.alejandro.clase.dogypark;

/**
 * Created by clase on 22/04/2018.
 */

public class LoadPark {

    private String nombreparque;
    private String descripcion;
    private String latitud;
    private String longitud;

    public LoadPark() {
    }

    public LoadPark(String nombreparque, String descripcion, String latitud, String longitud)
    {
        this.nombreparque = nombreparque;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "" +
                "Parque{" +
                "descripcion='" + descripcion + '\'' +
                ", loc{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                "}, nombreparque=" + nombreparque +
                '}';
    }
}

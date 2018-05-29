package com.alejandro.clase.dogypark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clase on 21/04/2018.
 */

public class LoadUser {

    private String nombre;
    private String nickname;
    private String biografia;
    private String sexo;
    private List<Parquesfavoritos> favoritos;


    public LoadUser() {
        favoritos=new ArrayList<>();
    }

    public LoadUser(String nombre, String nickname, String biografia, String sexo)
    {
        this.nombre = nombre;
        this.nickname = nickname;
        this.biografia = biografia;
        this.sexo = sexo;
        favoritos=new ArrayList<>();
    }

    public LoadUser(String nombre, String nickname, String biografia, String sexo, List<Parquesfavoritos> favoritos)
    {
        this.nombre = nombre;
        this.nickname = nickname;
        this.biografia = biografia;
        this.sexo = sexo;
        this.favoritos= favoritos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<Parquesfavoritos> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Parquesfavoritos> favoritos) {
        this.favoritos = favoritos;
    }

    @Override
    public String toString() {
        return "" +
                "Usuario{" +
                "biografia='" + biografia + '\'' +
                ", nickname=" + nickname +
                ", nombre=" + nombre +
                ", sexo=" + sexo +
                '}';
    }
}

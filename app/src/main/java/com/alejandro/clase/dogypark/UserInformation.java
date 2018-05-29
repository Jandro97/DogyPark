package com.alejandro.clase.dogypark;

/**
 * Created by clase on 23/02/2018.
 */

public class UserInformation {
    public String nombre;
    public String nickname;
    public String biografia;
    public String sexo;

    public UserInformation(){

    }

    public UserInformation(String nombre, String nickname, String biografia, String sexo) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.biografia = biografia;
        this.sexo = sexo;
    }
}

package com.alejandro.clase.dogypark;

import com.google.firebase.auth.FirebaseAuthException;

/**
 * Created by clase on 01/02/2018.
 */

public class Errores {
    public String getToastString(FirebaseAuthException e){
        String mensage="";
        if (e.getErrorCode()== "ERROR_INVALID_EMAIL"){
            mensage="email incorrecto";
        }
        else if (e.getErrorCode()== "ERROR_WRONG_PASSWORD"){
            mensage="password incorrecto";
        }
        else if (e.getErrorCode()== "FirebaseAuthWeakPasswordException"){
            mensage="password invalido";
        }
        else{
            mensage=e.getErrorCode();
        }
        return mensage;
    }
}

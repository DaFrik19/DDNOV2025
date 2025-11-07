package org.amerike.ameribank.config;

public class test {
     public static void main(String[] args) {
        security.init();
        System.out.println(security.obtenerUrl());
        System.out.println(security.obtenerUsuario());
        System.out.println(security.obtenerPassword());
    }
}


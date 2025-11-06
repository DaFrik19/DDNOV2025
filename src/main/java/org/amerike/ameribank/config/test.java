package org.amerike.ameribank.config;

import org.amerike.ameribank.config.security.sec;

public class test {
     public static void main(String[] args) {
        sec.init();
        System.out.println(sec.obtenerUrl());
        System.out.println(sec.obtenerUsuario());
        System.out.println(sec.obtenerPassword());
    }
}


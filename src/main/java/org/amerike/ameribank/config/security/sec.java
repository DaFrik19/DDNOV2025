package org.amerike.ameribank.config.security;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class sec {
    private static String rutaPrivateKey = "/src/main/java/org/amerike/ameribank/config/security/secrets/private_key.pem";
    private static String rutaPublicKey = "/src/main/java/org/amerike/ameribank/config/security/secrets/public_key.pem";
    private static String rutaCryptedFile = "/src/main/java/org/amerike/ameribank/config/security/secrets/accesodbjava.enc";


    private static boolean checkKeys() {
        File PrivateKey = new File(rutaPrivateKey);
        File PublicKey = new File(rutaPublicKey);
        File CryptedFile = new File(rutaCryptedFile);

        if (PrivateKey.exists() && PublicKey.exists() && CryptedFile.exists()) {
            System.out.println("Se encontro la llave publica y privada, y archivo encryptado...");
            return true;
        } else {
            System.out.println("Creando archivos de cifrado.");
            return false;
        }
    }

    private static void generadorClaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        KeyPair parClaves = generador.generateKeyPair();

        // Guardar llave Privada
        guardarClavePEM(rutaPublicKey, parClaves.getPrivate(), "PRIVATE KEY");

        // Guardar llave Pública
        guardarClavePEM(rutaPrivateKey, parClaves.getPublic(), "PUBLIC KEY");
    }

    private static void guardarClavePEM(String archivo, Key clave, String tipo) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN " + tipo + "-----\n");
        sb.append(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(clave.getEncoded()));
        sb.append("\n-----END " + tipo + "-----\n");

        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(sb.toString());
        }
    }
    ///
    private static void cifrador(String NombreBaseDeDatos, String User, String Password) throws Exception {

        byte[] claveBytes = Files.readAllBytes(Paths.get(rutaPublicKey));
        PublicKey clavePublica = cargarClavePublica(claveBytes);


        String datos = String.format("\"jdbc:mysql://localhost:3306/%s\"|\"%s\"|\"%s\"", NombreBaseDeDatos, User, Password);
        Cipher cifrador = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
        byte[] cifrado = cifrador.doFinal(datos.getBytes());

        Files.write(Paths.get(rutaCryptedFile), cifrado);
    }

    private static PublicKey cargarClavePublica(byte[] pem) throws Exception {
        String contenido = new String(pem).replaceAll("-----.*-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(contenido);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
    ///
    public static String[] obtenerCredenciales() throws Exception {
        byte[] claveBytes = Files.readAllBytes(Paths.get(rutaPrivateKey));
        PrivateKey clavePrivada = cargarClavePrivada(claveBytes);

        byte[] cifrado = Files.readAllBytes(Paths.get(rutaCryptedFile));
        Cipher descifrador = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        descifrador.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] descifrado = descifrador.doFinal(cifrado);

        return new String(descifrado).split("\\|");
    }

    private static PrivateKey cargarClavePrivada(byte[] pem) throws Exception {
        String contenido = new String(pem).replaceAll("-----.*-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(contenido);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
    ///
    private static void escribirCredenciales() {
        try {
            System.out.println("Cifrando credenciales.");
            Scanner sc = new Scanner(System.in);
            System.out.print("Nombre de la Base de Datos: ");
            String NombreBaseDeDatos = sc.nextLine();
            System.out.print("Nombre de usuario: ");
            String User = sc.nextLine();
            System.out.print("Password: ");
            String Password = sc.nextLine();
            cifrador(NombreBaseDeDatos, User, Password);
            System.out.println("Cifrado finalizado");
        } catch (Exception e){
            System.err.println("Error al cifrar base de datos");
        }
    }
    ///
    public static String obtenerUrl()  {
        try {
            String[] datos = obtenerCredenciales();
            String url = datos[0].replace("\"", "");
            return url;
        } catch (Exception e ) {
            System.err.println("Error al obtener las credenciales");
            return null;
        }
    }
    public static String obtenerUsuario()  {
        try {
            String[] datos = obtenerCredenciales();
            String user = datos[1].replace("\"", "");
            return user;
        } catch (Exception e ) {
            System.err.println("Error al obtener las credenciales");
            return null;
        }
    }
    public static String obtenerPassword()  {
        try {
            String[] datos = obtenerCredenciales();
            String Password = datos[2].replace("\"", "");
            return Password;
        } catch (Exception e ) {
            System.err.println("Error al obtener las credenciales");
            return null;
        }
    }
    ///


    public static void init() {
        if (checkKeys()) {
            System.out.println("Continuando...");
        } else {
           try {
               generadorClaves();
               System.out.println("LLaves generadas...");
           } catch (Exception e ){
               System.err.println("Error al generar las llaves");
           }
           escribirCredenciales();
        }
    }




}

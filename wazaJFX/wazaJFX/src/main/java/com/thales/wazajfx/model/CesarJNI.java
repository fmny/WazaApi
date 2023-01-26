package com.thales.wazajfx.model;

//Classe servant à utiliser la dll (.so sous linux) de chiffrement


public class CesarJNI {  // Save as HelloJNI.java
    static {
        System.loadLibrary("CesarFront"); // Load native library CesarFront.dll (Windows) or libCesarFront.so (Unixes)
        //  at runtime
        // This library contains a native method called sayHello()
    }

    // Declare an instance native method sayHello() returns void
    public native String encrypt(int i, String msg);

    public native String decrypt(int i, String msg);

}


/*
HelloJNI d'origine

public class HelloJNI {
    public HelloJNI() {
    }

    private native void sayHello(int var1, String var2);

    public static void main(String[] var0) {
        (new HelloJNI()).sayHello(3, "ABC");
    }

    static {
        System.loadLibrary("hello");
    }
}


 */
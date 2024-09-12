package com.example.stressApp.NativeClass;

public class NativeLibrary {
    static
    {
        System.loadLibrary("stressApp");
    }

    public static native void init();
}

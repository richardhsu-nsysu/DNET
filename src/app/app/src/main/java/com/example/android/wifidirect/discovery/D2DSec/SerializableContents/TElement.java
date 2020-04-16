package com.example.android.wifidirect.discovery.D2DSec.SerializableContents;

import it.unisa.dia.gas.jpbc.Element;

public class TElement {
    public static byte[][] toByteArray(Element[] E){
        byte[][] Element = new byte[E.length][];
        for (int u=0;u<E.length;u++) {
            Element[u] = E[u].toBytes();
        }
        return Element;
    }
}
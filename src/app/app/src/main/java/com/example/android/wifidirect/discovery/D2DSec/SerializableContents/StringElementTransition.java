package com.example.android.wifidirect.discovery.D2DSec.SerializableContents;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import it.unisa.dia.gas.jpbc.Element;


class StringElementTransition implements Serializable{

    private static final long serialVersionUID = 1L;

    @TargetApi(Build.VERSION_CODES.O)
    public String toTransferableObjectBase64() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(this);
            final byte[] byteArray = bos.toByteArray();
            return Base64.getEncoder().encodeToString(byteArray);
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    public static StringElementTransition getObjectBase64(String transferableString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(transferableString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return (StringElementTransition) in.readObject();
        }
    }


    public byte[][] toByteArray(Element[] E){
        byte[][] Element = new byte[E.length][];
        for (int u=0;u<E.length;u++) {
            Element[u] = E[u].toBytes();
        }
        return Element;
    }

}
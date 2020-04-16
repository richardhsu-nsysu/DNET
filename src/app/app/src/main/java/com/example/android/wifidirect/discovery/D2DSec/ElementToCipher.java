package com.example.android.wifidirect.discovery.D2DSec;

import android.util.Base64;

import it.unisa.dia.gas.jpbc.Element;


class ElementToCipher {
   static String ParctialCipherCreation(Element[] PC){
      return toCipher(PC);
   }

   static String CompleteCipherCreation(Element[] C){
        return toCipher(C);
    }


    static Element[] toPrivateKeyElementArray(String pk, int mNumAttributeType ,COABE co_abe) {
        String[] c_str_arr = pk.split("#");
        Element[] E = new Element[mNumAttributeType+1];
        for(int q = 0; q<E.length;q++){
            String temp = c_str_arr[q];
            byte[] encoded = Base64.decode(temp.getBytes(),Base64.DEFAULT);
                Element tempE = co_abe.mG2.newRandomElement();
                tempE.setFromBytes(encoded);
                E[q]=tempE.getImmutable();
        }
        return E;
    }


   private static String toCipher(Element[] E){
          String s="";
          for (int u=0;u<E.length;u++) {
              byte[] temp = E[u].toBytes();
              String temp_str = Base64.encodeToString(temp, Base64.DEFAULT);
              if(u==0){
                  s=s+temp_str;
              }else{
                  s= s+"#"+temp_str;
              }
          }
          return s;
      }

    // //test version to reduce bytes
    // private static String toCipher(Element[] E){
    //     String s="";
    //     for (int u=0;u<E.length;u++) {
    //         byte[] temp = E[u].toBytes();
    //         BigInteger tempInt = E[u].toBigInteger();
    //         String temp_str = new String(Base64.encode(temp, Base64.DEFAULT));
    //         if(u==0){
    //             s=s+temp_str;
    //         }else{
    //             s= s+"#"+temp_str;
    //         }
    //     }
    //     return s;
    // }

   static Element[] toElementArray(String S, COABE co_abe){
       String[] c_str_arr = S.split("#");
       Element[] E = new Element[5];
       for(int q = 0; q<E.length;q++){
           String temp = c_str_arr[q];
           byte[] encoded = Base64.decode(temp.getBytes(),Base64.DEFAULT);
                   //(temp.getBytes());
           if(q==2){
               Element tempE = co_abe.mGT.newRandomElement();
               tempE.setFromBytes(encoded);
               E[q] = tempE;
               // System.out.println("##!!!!!!##"+M);
           }else{
               Element tempE = co_abe.mG1.newRandomElement();
               tempE.setFromBytes(encoded);
               E[q] = tempE;
               //System.out.println("##!!!!!!##"+M);
           }
       }
       return E;
   }

    public static String toPolicyString(Element[] policy) {
        return toCipher(policy);
    }

    public static Element[] toPolicy(String policyString, int mNumAttributeType ,COABE co_abe) {
        String[] c_str_arr = policyString.split("#");
        Element[] policy = new Element[mNumAttributeType];
        for(int q = 0; q<policy.length;q++){
            String temp = c_str_arr[q];
            byte[] encoded = Base64.decode(temp.getBytes(),Base64.DEFAULT);
            Element tempE = COABE.mZr.newRandomElement();
            tempE.setFromBytes(encoded);
            policy[q]=tempE;
        //policy[q]=tempE.getImmutable();
        }
        return policy;
    }
}
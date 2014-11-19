package app.security;


import org.spongycastle.openpgp.PGPEncryptedDataGenerator;

import java.util.ArrayList;
import java.util.List;
//Lists all the available algorithms
public enum Algorithm {
    AES256(PGPEncryptedDataGenerator.AES_256, "P-521"),AES192(PGPEncryptedDataGenerator.AES_192,"P-384"),
    AES128(PGPEncryptedDataGenerator.AES_128,"P-256");

    private int value;
    private String name;

    private Algorithm(int val, String name){
        this.value = val;
        this.name = name;
    }
    public int getValue(){
        return value;
    }
    public String getCurveName(){
        return name;
    }
    public List<String> getCurveNames(){
        List<String> names = new ArrayList<String>();
        names.add("P-521");
        names.add("P-384");
        names.add("P-256");
        return names;
    }
};

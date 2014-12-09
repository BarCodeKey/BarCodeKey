package app.security;


public enum Curve {
    SECP192("secp192k1","92G"),SECP224("secp224k1","24E"),SECP256("secp256k1","56H");

    private String curve;
    private String Id;

    private Curve(String curve, String code){
        this.curve = curve;
        this.Id = code;
    }
    public String getCurve(){
        return curve;
    }
    public String getId(){
        return Id;
    }

}
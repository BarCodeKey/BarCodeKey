package app.domain;

public class Person {

    private String first_name;
    private String last_name;
    private String number;
    private String email;

    public void Person(){

    }

    public void Person(String first_name, String last_name, String number, String email){
        this.first_name = first_name;
        this.last_name = last_name;
        this.number = number;
        this.email = email;
    }
   // public void Person(String info){

    //}

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

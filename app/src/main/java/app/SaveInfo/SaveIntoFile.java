package app.SaveInfo;

import java.io.File;
import java.io.FileWriter;

import app.domain.Person;

public class SaveIntoFile {

    public SaveIntoFile(){
    }
        public boolean Save(Person person){
            File f;
            FileWriter writer;
            try{
                f = new File("info.txt");
                writer = new FileWriter(f);
                String info = toString(person);
                writer.write(info);
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    protected String toString(Person person){
        String info;
        info = "BEGIN:VCARD/n";
        info +="VERSION 3.0/n";
        info += "N:"+person.getLast_name()+";"+person.getFirst_name()+"/n";
        info += "TEL:"+person.getNumber()+"/n";
        info += "EMAIL:"+person.getEmail()+"/n";
        info +="END:VCARD";
        return info;
    }




}

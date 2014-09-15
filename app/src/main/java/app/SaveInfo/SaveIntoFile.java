package app.SaveInfo;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import app.domain.Person;


public class SaveIntoFile {

    public SaveIntoFile(){
    }
        public boolean Save(Person person, Context context){
            // ei pelaa vielä, joku tossa pathnamessa tökkii
            String filePath = context.getFilesDir().getPath() + "/info.txt";
            FileOutputStream fos;
            //File f;
            //FileWriter writer;
            try{
               fos = context.openFileOutput("info.txt", Context.MODE_PRIVATE);
               String testi = "testi";
               fos.write(testi.getBytes());
               fos.close();
              //String info = toString(person);
                //f = new File(filePath);
                System.out.println("trololo");
                //writer = new FileWriter(f);


                //writer.write(testi);
                //writer.close();

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

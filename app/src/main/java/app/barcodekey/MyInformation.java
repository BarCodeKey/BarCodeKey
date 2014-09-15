package app.barcodekey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//import java.SaveInfo.*;

import app.barcodekey.R;
import app.SaveInfo.SaveIntoFile;
import app.domain.Person;


public class MyInformation extends Activity {

    int[] id = {R.id.first_name,R.id.last_name,R.id.number,R.id.email};
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveInformation(View view){
        Intent intent = new Intent(this, Main_menu.class);
        Person person = makePerson();

        SaveIntoFile saver = new SaveIntoFile();
        saver.Save(person, context);
        // Asetuksiin
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, Main_menu.class); // Takas alkuun
        startActivity(intent);
    }
    public Person makePerson(){
        String[] info = new String[4];
        EditText editText;
        for (int i = 0; i < 4; i++) {
            editText = (EditText) findViewById(id[i]);
            info[i] = editText.getText().toString();
        }
        Person p = new Person();
        p.setFirst_name(info[0]);
        p.setLast_name(info[1]);
        p.setNumber(info[2]);
        p.setEmail(info[3]);
       //new Person(info[0],info[1],info[2],info[3]);eitoimi
        return p;
    }
}

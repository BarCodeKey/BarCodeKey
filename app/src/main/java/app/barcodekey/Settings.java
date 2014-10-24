package app.barcodekey;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {

    private int RESULT_CHANGED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CHANGED = getResources().getInteger(R.integer.RESULT_CHANGED);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed(){
        System.out.println("tultu onBackPressediin");
        if (getIntent().hasExtra("change")){
            System.out.println("Muutos on tehty");
            setResult(RESULT_CHANGED);
        }
        finish();
        /**
        Intent intent = new Intent(this, Main_menu.class);
        if (getIntent().hasExtra("change")){
            intent.putExtra("change", true);
            System.out.println("Muutos on tehty");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        **/
    }

    @Override
    public void onResume(){
        System.out.println("Settings onResume");
        super.onResume();
    }

    @Override
    public void onPause(){
        System.out.println("Settings onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        System.out.println("Settings onRestart");
        super.onRestart();
    }

    @Override
    public void onDestroy(){
        System.out.println("Settings onDestroy");
        super.onDestroy();
    }

}
package app.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import app.util.Constants;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed(){
        System.out.println("tultu onBackPressediin");
        if (getIntent().hasExtra(Constants.EXTRA_SETTINGS_CHANGED)){
            System.out.println("Muutos on tehty");
            setResult(Constants.RESULT_CHANGED);
        }
        finish();
        /*
        Intent intent = new Intent(this, Main_menu.class);
        if (getIntent().hasExtra("change")){
            intent.putExtra("change", true);
            System.out.println("Muutos on tehty");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        */
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
package app.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import app.util.Constants;

/**
 * An android-style preferences screen class. Creates the basic view on which a
 * SettingsFragment is built to list all the preferences available to the user.
 */
public class Settings extends PreferenceActivity {

    /**
     * Executed when the settings view is started. Starts a FragmentManager that
     * builds the SettingsFragment in the view.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    /**
     * Handles the situation when the user presses the back button on his/hers device to
     * return to the main view and informs the main activity about changes in the preferences.
     */
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

    /**
     * POISTETAAN LOPULLISESTA KOODISTA?
     */
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
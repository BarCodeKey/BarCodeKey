package app.preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import app.barcodekey.R;
import app.util.Constants;
import app.util.InputValidator;

/**
 * Creates a list of preferences that the user can see and edit in the settings view.
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{


    private InputValidator inputValidator;

    /**
     * Executed when the class is created. Goes through all the methods that build the different
     * preferences.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inputValidator = new InputValidator();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        //PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
        initSummary(getPreferenceScreen());
        initValidator();
        initResetKeys();
        initHelp();
    }

    /**
     * Handles the secure key pair resetting that is executed from a button.
     */
    public void resetKeys(){
        this.getActivity().setResult(Constants.RESULT_RESET_KEYS);
        this.getActivity().finish();
        /**
        Intent intent = new Intent(getActivity(), Main_menu.class);
        intent.putExtra("reset_keys", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    **/
    }

    /**
     * This updates the shared preference after a change has been made.
     * @param sharedPreferences
     * @param s a text to identify the preference that is edited
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Constants.log("tultu sharedpreferenceen");
        Preference pref = findPreference(s);
        updatePreferenceSummary(pref);

        getActivity().getIntent().putExtra(Constants.EXTRA_SETTINGS_CHANGED, true);

    //    resetInfo();
    }

    /**
     * Goes through the initializing of validators for all fields that the user can edit.
     */
    public void initValidator(){
        initializeFieldValidator("email");
        initializeFieldValidator("first_name");
        initializeFieldValidator("last_name");
        initializeFieldValidator("number");
    }

    /**
     * Validates the text that the user is trying to insert in a field.
     *
     * @param field the field that is edited
     */
    public void initializeFieldValidator(final String field){
        final EditTextPreference editTextPreference = (EditTextPreference) getPreferenceScreen().findPreference(field);
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String pattern = "";
                if (inputValidator.validate(field, (String) newValue)) {
                    return true;
                } else {
                    alert((String) newValue);
                    return false;
                }
            }
        });
    }

    /**
     * Sets up the button for resetting the user's secure key pair.
     */
    public void initResetKeys(){
        final Preference preference = getPreferenceScreen().findPreference("reset_keys");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) {
                askToConfirm((String) getText(R.string.reset_keys_note), 2);
                return true;
            }
        });
    }

    /**
     * Sets up the help buttons that display quick information about the app.
     */
    private void initHelp() {
        initializeSimpleTextAlert("quick_guide_button", R.string.quick_guide);
        initializeSimpleTextAlert("encryption_info_button", R.string.encryption_info_text);
    }

    /**
     * Sets up a simple pop-up window that communicates with the user when editing preferences.
     * Used to show the app information to the user.
     *
     * @param field the field that is used
     * @param resString the information text that should be shown
     */
    public void initializeSimpleTextAlert(final String field, final int resString) {
        final Preference preference = getPreferenceScreen().findPreference(field);
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(resString);
                builder.setPositiveButton(R.string.ok, null);
                builder.show();
                return true;
            }
        });
    }

    /**
     * Sets up a simple alert pop-up that shows up when the user tries to insert invalid
     * text to a preference field.
     *
     * @param givenValue
     */
    public void alert(String givenValue){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.invalid_input);
        builder.setMessage(givenValue);
        builder.setPositiveButton(R.string.ok, null);
        builder.show();
    }

    /**
     * Sets up a simple confirmation pop-up that prevents accidental key pair resetting.
     *
     * @param message the message that is displayed in the pop-up window
     * @param n a counting variable that enables multiple queries before really
     *          resetting the keys for added security.
     */
    public void askToConfirm(final String message, final int n){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.string.confirm);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        if (n < 2){
                            resetKeys();
                        } else {
                            askToConfirm((String) getText(R.string.confirm_double), n-1);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * This initializes the summaries of given set of preferences. Given argument can be typed as
     * Preference, PreferenceGroup or PreferenceScreen.
     * @param pref the preference to be initialized
     */
    private void initSummary(Preference pref) {
        // If pref is some kind of PreferenceGroup we will call initSummary recursively
        if (pref instanceof PreferenceGroup) {
            PreferenceGroup prefGroup = (PreferenceGroup) pref;
            for (int i = 0; i < prefGroup.getPreferenceCount(); i++) {
                initSummary(prefGroup.getPreference(i));
            }
        } else {
            // Otherwise we can update the summary of pref
            updatePreferenceSummary(pref);
        }
    }

    /**
     * This updates the summary of the given preference. The summary will show the current value.
     * @param pref the preference to be updated
     */
    private void updatePreferenceSummary(Preference pref){
        if (pref instanceof EditTextPreference) {
            EditTextPreference editText = (EditTextPreference) pref;
            pref.setSummary(editText.getText());
        }
    }

    /**
     * Returns the edited preferences if the execution of the app is resumed from a
     * suspended state.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * Saves the edited preferences if the app is suspended.
     */
    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}



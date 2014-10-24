package app.barcodekey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import app.domain.Validator;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{


    private Validator validator;
    private int RESULT_RESET_KEYS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.validator = new Validator();
        RESULT_RESET_KEYS = getResources().getInteger(R.integer.RESULT_RESET_KEYS);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        //PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
        initSummary(getPreferenceScreen());
        initValidator();
        initResetKeys();
        initHelp();
    }

    public void resetKeys(){
        this.getActivity().setResult(RESULT_RESET_KEYS);
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
     * @param s
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        System.out.println("tultu sharedpreferenceen");
        Preference pref = findPreference(s);
        updatePreferenceSummary(pref);

        getActivity().getIntent().putExtra("change", true);

    //    resetInfo();
    }

    public void initValidator(){
        initializeFieldValidator("email");
        initializeFieldValidator("first_name");
        initializeFieldValidator("last_name");
        initializeFieldValidator("number");
    }

    public void initializeFieldValidator(final String field){
        final EditTextPreference editTextPreference = (EditTextPreference) getPreferenceScreen().findPreference(field);
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String pattern = "";
                if (validator.validate(field, (String) newValue)) {
                    return true;
                } else {
                    alert((String) newValue);
                    return false;
                }
            }


        });
    }

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

    private void initHelp() {
        initializeSimpleTextAlert("quick_guide_button", R.string.quick_guide);
        initializeSimpleTextAlert("encryption_info_button", R.string.encryption_info_text);
    }

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

    public void alert(String givenValue){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.invalid_input);
        builder.setMessage(givenValue);
        builder.setPositiveButton(R.string.ok, null);
        builder.show();
    }

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
     * @param pref
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
     * @param pref
     */
    private void updatePreferenceSummary(Preference pref){
        if (pref instanceof EditTextPreference) {
            EditTextPreference editText = (EditTextPreference) pref;
            pref.setSummary(editText.getText());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}



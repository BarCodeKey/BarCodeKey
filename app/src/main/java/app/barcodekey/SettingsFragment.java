package app.barcodekey;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
        initSummary(getPreferenceScreen());

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

    /**
     * This updates the shared preference after a change has been made.
     * @param sharedPreferences
     * @param s
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference pref = findPreference(s);
        updatePreferenceSummary(pref);

    }



}

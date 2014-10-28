package app.preferences;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import app.preferences.Settings;

public class SettingsTest extends ActivityInstrumentationTestCase2<Settings> {

    private Solo solo;

    public SettingsTest() {
        super(Settings.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

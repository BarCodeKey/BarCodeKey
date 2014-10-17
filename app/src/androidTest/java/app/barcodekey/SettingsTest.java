package app.barcodekey;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;

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

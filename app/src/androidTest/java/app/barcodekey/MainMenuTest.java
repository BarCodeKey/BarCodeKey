package app.barcodekey;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;


public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenu> {

    private Solo solo;

    public MainMenuTest() {
        super(MainMenu.class);
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

    /*public void testButtonsAreVisible() {
        Assert.assertTrue(solo.searchButton(this.getActivity().getString(R.string.button_scan)));
        solo.sendKey(Solo.MENU);
        Assert.assertTrue(solo.searchText("Settings"));
    }*/
}

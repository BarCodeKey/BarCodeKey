package app.barcodekey;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;

import junit.framework.Assert;

public class Main_menuTest extends ActivityInstrumentationTestCase2<Main_menu> {

    private Solo solo;

    public Main_menuTest() {
        super(Main_menu.class);
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

    public void testPreferences() {
        solo.sendKey(Solo.MENU);
        solo.clickOnText("Settings");
        Assert.assertTrue(solo.searchText("First name"));
    }
}

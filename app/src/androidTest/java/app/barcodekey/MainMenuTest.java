package app.barcodekey;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import app.contacts.QRScanner;


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
    }

    public void testScanButtonIsVisible(){
        solo.assertCurrentActivity("wrong activity", MainMenu.class);
        assertTrue(solo.searchButton(solo.getString(R.string.button_scan)));
    }

    public void testMenuButtonIsVisibleAndMenuContainsSettings(){
        solo.assertCurrentActivity("wrong", MainMenu.class);
        solo.sendKey(Solo.MENU);
        solo.clickOnText(solo.getString(R.string.action_settings));
        assertTrue(solo.searchText(solo.getString(R.string.first_name)));
        solo.goBack();
        solo.assertCurrentActivity("wrong", MainMenu.class);
        assertTrue(solo.searchText(solo.getString(R.string.button_scan)));
    }

    public void testCanEditSettings(){
        solo.assertCurrentActivity("wrong", MainMenu.class);
        solo.sendKey(Solo.MENU);
        solo.clickOnText(solo.getString(R.string.action_settings));
        assertTrue(solo.searchText(solo.getString(R.string.first_name)));

        solo.clickOnText(solo.getString(R.string.first_name));
        solo.clearEditText(0);
        solo.enterText(0, "Matti");
        solo.clickOnButton(solo.getString(R.string.ok));

        solo.clickOnText(solo.getString(R.string.last_name));
        solo.clearEditText(0);
        solo.enterText(0, "Meikäläinen");
        solo.clickOnButton(solo.getString(R.string.ok));

        solo.clickOnText(solo.getString(R.string.number));
        solo.clearEditText(0);
        solo.enterText(0, "123456789");
        solo.clickOnButton(solo.getString(R.string.ok));

        solo.clickOnText(solo.getString(R.string.email));
        solo.clearEditText(0);
        solo.enterText(0, "matti@mail.com");
        solo.clickOnButton(solo.getString(R.string.ok));

        solo.goBack();
        solo.assertCurrentActivity("wrong", MainMenu.class);
        assertTrue(solo.searchText("Matti"));
        assertTrue(solo.searchText("Meikäläinen"));
        assertTrue(solo.searchText("123456789"));
        assertTrue(solo.searchText("matti@mail.com"));
    }

    public void testResetKeys(){
        solo.assertCurrentActivity("wrong", MainMenu.class);
        solo.sendKey(Solo.MENU);
        solo.clickOnText(solo.getString(R.string.action_settings));
        assertTrue(solo.searchText(solo.getString(R.string.reset_keys)));
        solo.clickOnText(solo.getString(R.string.reset_keys));
        solo.clickOnText(solo.getString(R.string.ok));
        solo.clickOnText(solo.getString(R.string.ok));
        assertTrue(solo.searchText(solo.getString(R.string.your_public_key)));
    }

    public void testQuickGuide(){
        solo.assertCurrentActivity("wrong", MainMenu.class);
        solo.sendKey(Solo.MENU);
        solo.clickOnText(solo.getString(R.string.action_settings));
        solo.clickOnText(solo.getString(R.string.quick_guide_button));
        assertTrue(solo.searchText(solo.getString(R.string.quick_guide)));
        solo.clickOnText(solo.getString(R.string.ok));
        assertTrue(solo.searchText(solo.getString(R.string.first_name)));
    }

    public void testEncryptionInformation(){
        solo.assertCurrentActivity("wrong", MainMenu.class);
        solo.sendKey(Solo.MENU);
        solo.clickOnText(solo.getString(R.string.action_settings));
        solo.clickOnText(solo.getString(R.string.encryption_info));
        assertTrue(solo.searchText(solo.getString(R.string.encryption_info_text)));
        solo.clickOnText(solo.getString(R.string.ok));
        assertTrue(solo.searchText(solo.getString(R.string.first_name)));
    }*/
}


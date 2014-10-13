package app.domain;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Adapter;


public class ContactsHandlerTest extends ActivityInstrumentationTestCase2 {

    private ContactsHandler handler;
    private Activity handlerActivity;
    private Adapter handlerAdapter;


    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
    public ContactsHandlerTest(){
        super(ContactsHandler.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        handlerActivity = getActivity();
        //handler = (ContactsHandler) handlerActivity.findViewById();

    }
    public void firstTest(){
        handler.addOrEditContact("trololoo");
    }
}

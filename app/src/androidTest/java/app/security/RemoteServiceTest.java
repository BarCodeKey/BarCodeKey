package app.security;


import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class RemoteServiceTest extends ServiceTestCase<RemoteService> {

    public RemoteServiceTest() {
        super(RemoteService.class);
    }


    @MediumTest
    public void testBindable() {
        Intent startIntent = new Intent("app.security.RemoteService");
        IBinder service = bindService(startIntent);
    }

    public void testIntentCheck() {
        Intent i = new Intent("app.security.RemoteService");
        i.putExtra(Intent.EXTRA_UID, 2);
        i.putExtra("package_name","kissa");
        assertTrue(new RemoteService().intentCheck(i));
    }
    public void testIntentCheckReturnsFalse() {
        Intent i = new Intent("app.security.RemoteService");
        i.putExtra(Intent.EXTRA_UID, 2);
        assertFalse(new RemoteService().intentCheck(i));
    }


}

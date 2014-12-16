package app.security;

import android.app.Application;
import android.content.Context;

/**
 * A helper class that can be used to check the android application context that is
 * needed in different operations around the app.
 */
public class ContextHandler  extends Application{
    public static Context context;

    public void onCreate(){
        super.onCreate();
        ContextHandler.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return ContextHandler.context;
    }
}

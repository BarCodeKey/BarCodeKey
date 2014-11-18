package app.security;

import android.app.Application;
import android.content.Context;


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

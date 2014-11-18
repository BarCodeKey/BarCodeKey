package app.security;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class RemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

   private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

       @Override
       public String encrypt(String type,byte[] data, String uri) throws RemoteException{

           return "";
       }
       @Override
       public String decrypt(String type,byte[] data,String uri) throws RemoteException{
           return "";
       }

   };
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

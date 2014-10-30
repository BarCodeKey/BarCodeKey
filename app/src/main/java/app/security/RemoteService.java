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

   private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
       @Override
       public int getPid()throws RemoteException{
           return 0;
       }
       @Override
       public String encrypt(byte[] data) throws RemoteException{
           return CryptoHandler.encryptSimple(data);
       }
       @Override
       public String decrypt(byte[] data) throws RemoteException{
           return CryptoHandler.decryptSimple(data);
       }

   };
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

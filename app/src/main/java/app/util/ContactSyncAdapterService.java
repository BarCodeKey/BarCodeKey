package app.util;


import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;

public class ContactSyncAdapterService extends Service{
    private static ContactSyncAdapter contactSyncAdapter = null;
    private static ContentResolver contentResolver = null;

    public ContactSyncAdapterService(){
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder r = null;
        r = getContactSyncAdapter().getSyncAdapterBinder();
        return r;
    }

    private ContactSyncAdapter getContactSyncAdapter(){
        if (contactSyncAdapter == null){
            contactSyncAdapter = new ContactSyncAdapter(this);
        }
        return contactSyncAdapter;
    }

    private static void performSync(Context context, Account account, Bundle bundle, String s, ContentProviderClient provider, SyncResult syncResult) throws OperationCanceledException{
        contentResolver = context.getContentResolver();
    }

    public static void requestSync() {
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(new Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE), ContactsContract.AUTHORITY, extras);
    }

    private class ContactSyncAdapter extends AbstractThreadedSyncAdapter {
        private Context mContext;

        public ContactSyncAdapter(){
            super(ContactSyncAdapterService.this, true);
        }

        public ContactSyncAdapter(Context context){
            super(context, true);
        }

        @Override
        public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
            try {
                ContactSyncAdapterService.performSync(mContext, account, bundle, s, contentProviderClient, syncResult);
            } catch (OperationCanceledException e){

            }

        }
    }
}

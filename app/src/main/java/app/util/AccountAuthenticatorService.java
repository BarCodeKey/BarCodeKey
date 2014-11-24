package app.util;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {
    private static Authenticator sAccountAuthenticator = null;

    public AccountAuthenticatorService(){
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder r = null;
        if (intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT)){
            r = getAuthenticator().getIBinder();
        }
        return r;
    }

    private Authenticator getAuthenticator(){
        if (sAccountAuthenticator == null){
            sAccountAuthenticator = new Authenticator(this);
        }
        return sAccountAuthenticator;
    }

    private class Authenticator extends AbstractAccountAuthenticator {
        private Context mContext;

        public Authenticator(){
            super(AccountAuthenticatorService.this);
        }

        public Authenticator(Context context){
            super(context);
            mContext = context;
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String s, String s2, String[] strings, Bundle bundle) throws NetworkErrorException {
            Bundle reply = new Bundle();
            Intent intent = new Intent(mContext, this.getClass());
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
            reply.putParcelable(AccountManager.KEY_INTENT, intent);
            return reply;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String s) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
            return null;
        }
    }
}

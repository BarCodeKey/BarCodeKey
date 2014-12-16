// IRemoteService.aidl
package app.security;


interface IRemoteService {

    String encrypt(String data, String lookupKey);
    String decrypt(String data, String lookupKey);
}

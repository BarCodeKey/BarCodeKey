// IRemoteService.aidl
package app.security;


interface IRemoteService {

    byte[] encrypt(byte[] data, String lookupKey);
    byte[] decrypt(byte[] data, String lookupKey);
}

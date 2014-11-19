// IRemoteService.aidl
package app.security;


interface IRemoteService {

    String encrypt(String type,in byte[] data, String uri);
    String decrypt(String type,in byte[] data, String uri);
}

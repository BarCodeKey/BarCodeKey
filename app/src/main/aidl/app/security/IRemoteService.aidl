// IRemoteService.aidl
package app.security;


interface IRemoteService {

    int getPid();

    String encrypt(in byte[] data);
    String decrypt(in byte[] data);
}

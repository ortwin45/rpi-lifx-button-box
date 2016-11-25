package org.ojothepojo.rpi.lifx;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class Util {
    private static String ip;

    public static String getIpAddress() {
        if (ip == null) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                ip = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return ip;
    }
}

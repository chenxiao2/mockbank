package ssl;

import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by liang on 4/15/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        createSSLClientSocketFactory();
        SSLServerSocketFactory sslServerSocketFactory = createSSLServerSocketFactory();

        SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketFactory.createServerSocket(8080);

        String[] serverSupportedProtocols = sslServerSocket.getSupportedProtocols();
        System.out.println("Server supported protocols: " + Arrays.asList(serverSupportedProtocols));

        String[] serverEnabledProtocols = sslServerSocket.getEnabledProtocols();
        System.out.println("Server enabled protocols: " + Arrays.asList(serverEnabledProtocols));

        String[] serverEnabledCipherSuites = sslServerSocket.getEnabledCipherSuites();
        String[] serverSupportedCipherSuites = sslServerSocket.getSupportedCipherSuites();
        System.out.println("The number of server socket enabled cipher suites: " + serverEnabledCipherSuites.length);
        System.out.println("The number of server socket supported cipher suites: " + serverSupportedCipherSuites.length);
    }

    private static SSLSocketFactory createSSLClientSocketFactory() {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String[] defaultCipherSuites = sslSocketFactory.getDefaultCipherSuites();
        String[] supportedCipherSuites = sslSocketFactory.getSupportedCipherSuites();
        System.out.println("The number of client default cipher suites: " + defaultCipherSuites.length);
        System.out.println("The number of client supported cipher suites: " + supportedCipherSuites.length);
//        System.out.println(String.join("\n", defaultCipherSuites));
//        System.out.println(String.join("\n", supportedCipherSuites));
        return sslSocketFactory;
    }

    private static SSLServerSocketFactory createSSLServerSocketFactory() {
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        String[] serverDefaultCipherSuites = sslServerSocketFactory.getDefaultCipherSuites();
        String[] serverSupportedCipherSuites = sslServerSocketFactory.getSupportedCipherSuites();
        System.out.println("The number of server default cipher suites: " + serverDefaultCipherSuites.length);
        System.out.println("The number of server supported cipher suites: " + serverSupportedCipherSuites.length);
        return sslServerSocketFactory;
    }
}

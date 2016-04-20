package ssl;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SSLSimpleServer extends Thread {
    static final int PORT = 9090;
    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketFactory.createServerSocket(PORT);
        System.out.println("EnableSessionCreation: " + sslServerSocket.getEnableSessionCreation());
        System.out.println("WantClientAuth: " + sslServerSocket.getWantClientAuth());
        System.out.println("NeedClientAuth: " + sslServerSocket.getNeedClientAuth());
        System.out.println("UseClientMode: " + sslServerSocket.getUseClientMode());

        while (true) {
            SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
            new SSLSimpleServer(sslSocket).start();
        }
    }

    private SSLSocket sslSocket;

    public SSLSimpleServer(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
        this.sslSocket.addHandshakeCompletedListener((event) -> System.out.println("Handshake completed"));
    }

    public void run() {
        try {
            SSLSession sslSession = sslSocket.getSession();
            SSLSimpleClient.inspectSSLSession(sslSession);

            BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());

            String data = br.readLine();
//            while(data != null) {
                System.out.println("received: " + data);
//                data = br.readLine();
//            }
            pw.println("he is my son.");

            pw.close();
            sslSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

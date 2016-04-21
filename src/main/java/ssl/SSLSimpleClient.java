package ssl;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;

import static ssl.SSLSimpleServer.PORT;

public class SSLSimpleClient {
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket)sslSocketFactory.createSocket(HOST, PORT);
        sslSocket.addHandshakeCompletedListener((event) -> System.out.println("Handshake completed"));
        SSLSession sslSession = sslSocket.getSession();
        inspectSSLSession(sslSession);


        BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());

        pw.println("Who is Nash?");
        pw.flush();

        System.out.println(br.readLine());

        sslSocket.close();
    }

    public static void inspectSSLSession(SSLSession sslSession){
        System.out.println(">>ApplicationBufferSize: " + sslSession.getApplicationBufferSize());
        System.out.println(">>CipherSuite: " + sslSession.getCipherSuite());
        System.out.println(">>Protocol: " + sslSession.getProtocol());
        System.out.println(">>CreationTime: " + Instant.ofEpochMilli(sslSession.getCreationTime()).atZone(ZoneId.systemDefault()));
        System.out.println(">>LastAccessedTime: " + Instant.ofEpochMilli(sslSession.getLastAccessedTime()).atZone(ZoneId.systemDefault()));

        System.out.println(">>LocalPrinciple: " + sslSession.getLocalPrincipal());
        System.out.println(">>LocalCertificates.size: " + (sslSession.getLocalCertificates() != null ?
                sslSession.getLocalCertificates().length : null));

        System.out.println(">>PeerHost:PearPort " + sslSession.getPeerHost() + ":" + sslSession.getPeerPort());
        try {
            System.out.println(">>PeerPrinciple: " + sslSession.getPeerPrincipal());
            System.out.println(">>PeerCertificates.size: " + sslSession.getPeerCertificates().length);
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }

    }
}

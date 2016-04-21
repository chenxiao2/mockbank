package ssl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public class HttpsURLConnectionSimpleClient {

    public static void main(String[] args) throws IOException {
        URL hostURL = new URL("https://localhost:9090");
        HttpsURLConnection.setDefaultHostnameVerifier(
                (String hostname, SSLSession session) -> "localhost".equalsIgnoreCase(hostname) ? true : false);
        HttpsURLConnection connection = (HttpsURLConnection) hostURL.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();
        System.out.println("CihperSuite: " + connection.getCipherSuite());
        //could complete SSL handshake but unable to get response because it is not valid HTTP response.
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        PrintWriter pw = new PrintWriter(connection.getOutputStream());
//        pw.println("Who is Nash?");
//        pw.flush();
//        System.out.println(br.readLine());
        connection.disconnect();
    }
}

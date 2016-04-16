package ssl;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SSLSimpleClient {
    /**
     * @param args arg[0] portNumber
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SocketFactory sf = SSLSocketFactory.getDefault();
        Socket s = sf.createSocket(args[0], Integer.parseInt(args[1]));

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        System.out.println("Who is Liang?");
        pw.println("Who is Sylvia?");
        pw.flush();
        System.out.println(br.readLine());
        s.close();
    }
}

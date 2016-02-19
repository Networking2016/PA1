import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Client {
    public static void main(String[] args) {
        if(args.length <= 1) {
            System.out.println("Usage: Client hostname port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        try (
            Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("receive: " + in.readLine());
            while(true) {
                Scanner sc = new Scanner(System.in);
                String msg = sc.nextLine();
                out.println(msg);
                long rcv = Long.parseLong(in.readLine());
                System.out.print("receive: ");
                if(rcv >= 0) {
                    System.out.println(Long.toString(rcv));
                } else {
                    ServerResponses rsp = ServerResponses.forValue((int)rcv);
                    System.out.println(rsp.getDescription());
                    if(ServerResponses.EXIT == rsp) {
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
            System.exit(-1);
        }
    }
}
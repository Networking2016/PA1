import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            System.out.println("ServerSocket connected to port " + port + "!");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress ip = clientSocket.getInetAddress();
                System.out.println("get connect from " + ip.getHostAddress());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("Hello!");

                String line;
                while ((line = in.readLine()) != null) {
                    String[] split = line.split(" ");
                    String command = split[0];

                    if (command.equals("bye") || command.equals("exit")) {
                        out.println(ServerResponses.EXIT.getValue());
                        break;
                    }
                    if (command.equals("terminate")) {
                        out.println(ServerResponses.EXIT.getValue());
                        System.exit(0);
                    }
                    BinaryArithmeticOp operator;
                    try {
                        operator = BinaryArithmeticOp.getOpFromString(command);
                    } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                        out.println(ServerResponses.INVALID_OPERATION.getValue());
                        continue;
                    }

                    if (split.length < 3) {
                        out.println(ServerResponses.TOO_FEW_INPUTS.getValue());
                        continue;
                    } else if (split.length > 5) {
                        out.println(ServerResponses.TOO_MANY_INPUTS.getValue());
                        continue;
                    }

                    try {
                        long intermediateVal = Long.parseLong(split[1]);
                        for (int index = 2; index < split.length; ++index) {
                            Long operand = Long.parseLong(split[index]);
                            intermediateVal = operator.exec(intermediateVal, operand);
                        }
                        out.println(Long.toString(intermediateVal));
                    } catch (NumberFormatException ex) {
                        out.println("-4");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
            System.exit(-1);
        }
    }
}

abstract class BinaryArithmeticOp {
    public abstract long exec(long i, long j);

    @SuppressWarnings("unused")
    public abstract double exec(double a, double b);

    public static BinaryArithmeticOp getOpFromString(String command) {
        command = command.toLowerCase();

        switch (command) {
            case "addition":
                // fall through to shorthand add
            case "add":
                return new add();
            case "multiply":
                // fall through to shorthand mult
            case "mult":
                return new mult();
            case "subtract":
                // fall through to shorthand sub
            case "sub":
                return new sub();
            case "divide":
                // fall through to shorthand div
            case "div":
                return new div();
            default:
                throw new IllegalArgumentException("Invalid command for binaryArithmeticOp.");
        }
    }
}

class add extends BinaryArithmeticOp {
    public long exec(long a, long b) {
        return a + b;
    }

    public double exec(double a, double b) {
        return a + b;
    }
}

class sub extends BinaryArithmeticOp {
    public long exec(long a, long b) {
        return a - b;
    }

    public double exec(double a, double b) {
        return a - b;
    }
}

class mult extends BinaryArithmeticOp {
    public long exec(long a, long b) {
        return a * b;
    }

    public double exec(double a, double b) {
        return a * b;
    }
}

class div extends BinaryArithmeticOp {
    public long exec(long a, long b) {
        return a / b;
    }

    public double exec(double a, double b) {
        return a / b;
    }
}
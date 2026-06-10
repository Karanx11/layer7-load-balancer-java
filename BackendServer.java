import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BackendServer {

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);

        HttpServer server =
                HttpServer.create(new InetSocketAddress(port), 0);

       server.createContext("/", exchange -> {

    String path = exchange.getRequestURI().getPath();

    String response =
            "Backend " + port +
            " handled path: " + path;

    exchange.sendResponseHeaders(
            200,
            response.length()
    );

    OutputStream os =
            exchange.getResponseBody();

    os.write(response.getBytes());
    os.close();
});

server.createContext("/health", exchange -> {

    String response = "OK";

    exchange.sendResponseHeaders(
            200,
            response.length()
    );

    OutputStream os =
            exchange.getResponseBody();

    os.write(response.getBytes());
    os.close();
});

        server.setExecutor(null);
        server.start();

        System.out.println(
                "Backend Server Started on Port: " + port);
    }
}
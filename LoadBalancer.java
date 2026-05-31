import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private static final List<ServerInfo> BACKENDS = List.of(
            new ServerInfo("http://localhost:8081"),
            new ServerInfo("http://localhost:8082"),
            new ServerInfo("http://localhost:8083")
    );

    private static final AtomicInteger counter =
            new AtomicInteger(0);

    private static String getNextBackend() {

        int size = BACKENDS.size();

        for (int i = 0; i < size; i++) {

            int index =
                    counter.getAndIncrement() % size;

            ServerInfo server =
                    BACKENDS.get(index);

            if (server.isHealthy()) {
                return server.getUrl();
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        // Main Load Balancer Endpoint
        server.createContext(
                "/",
                LoadBalancer::handleRequest
        );

        // Metrics Endpoint
        server.createContext(
                "/metrics",
                exchange -> {

                    String response =
                            MetricsManager.getMetrics();

                    exchange.sendResponseHeaders(
                            200,
                            response.length()
                    );

                    OutputStream os =
                            exchange.getResponseBody();

                    os.write(response.getBytes());

                    os.close();
                }
        );

        // Multi-threaded Request Handling
        server.setExecutor(
                Executors.newFixedThreadPool(20)
        );

        // Health Check Scheduler
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                new HealthChecker(BACKENDS),
                0,
                5,
                TimeUnit.SECONDS
        );

        server.start();

        System.out.println(
                "Load Balancer running on port 8080"
        );
    }

    private static void handleRequest(
            HttpExchange exchange
    ) {

        try {

            MetricsManager.incrementTotalRequests();

            String backendUrl =
                    getNextBackend();

            if (backendUrl == null) {

                String response =
                        "No healthy backend servers available";

                exchange.sendResponseHeaders(
                        503,
                        response.length()
                );

                OutputStream os =
                        exchange.getResponseBody();

                os.write(response.getBytes());

                os.close();

                return;
            }

            URL url =
                    new URL(backendUrl);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            InputStream input =
                    connection.getInputStream();

            String response =
                    new String(input.readAllBytes());

            exchange.sendResponseHeaders(
                    200,
                    response.length()
            );

            OutputStream os =
                    exchange.getResponseBody();

            os.write(response.getBytes());

            MetricsManager.incrementBackendRequest(
                    backendUrl
            );

            os.close();

            System.out.println(
                    Thread.currentThread().getName()
                            + " -> "
                            + backendUrl
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
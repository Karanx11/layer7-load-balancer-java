import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HealthChecker implements Runnable {

    private final List<ServerInfo> backends;

    public HealthChecker(List<ServerInfo> backends) {
        this.backends = backends;
    }

    @Override
    public void run() {

        for (ServerInfo server : backends) {

            try {

                URL url =
                        new URL(server.getUrl() + "/health");

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);

                connection.setRequestMethod("GET");

                int responseCode =
                        connection.getResponseCode();

                boolean healthy =
                        responseCode == 200;

                server.setHealthy(healthy);

                System.out.println(
                        server.getUrl()
                                + " -> "
                                + (healthy ? "HEALTHY" : "UNHEALTHY")
                );

            } catch (Exception e) {

                server.setHealthy(false);

                System.out.println(
                        server.getUrl()
                                + " -> UNHEALTHY"
                );
            }
        }

        System.out.println("------------------------");
    }
}
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricsManager {

    private static final AtomicInteger totalRequests = new AtomicInteger(0);

    private static final ConcurrentHashMap<String, AtomicInteger> backendRequests = new ConcurrentHashMap<>();

    public static void incrementTotalRequests() {
        totalRequests.incrementAndGet();
    }

    public static void incrementBackendRequest(String backend) {

        backendRequests
                .computeIfAbsent(
                        backend,
                        k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public static String getMetrics() {

        StringBuilder sb = new StringBuilder();

        sb.append("Total Requests: ")
                .append(totalRequests.get())
                .append("\n\n");

        sb.append("Requests Per Backend:\n");

        backendRequests.forEach((backend, count) -> sb.append(backend)
                .append(" -> ")
                .append(count.get())
                .append("\n"));

        return sb.toString();
    }
}
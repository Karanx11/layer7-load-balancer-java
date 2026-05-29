public class ServerInfo {

    private final String url;
    private volatile boolean healthy = true;

    public ServerInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}
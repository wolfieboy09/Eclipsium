import com.mojang.logging.LogUtils;
import dev.wolfieboy09.eclipsium.routing.RouteProcessor;
import dev.wolfieboy09.eclipsium.web.BaseWebServer;
import org.slf4j.Logger;

public class Main {
    private final Logger LOGGER = LogUtils.getLogger();
    private static final BaseWebServer webServer = new BaseWebServer(8000);

    public static void main(String[] args) {
        WebPages routes = new WebPages();
        RouteProcessor.registerRoutes(routes, webServer, true);
        webServer.start();
    }
}
import com.mojang.logging.LogUtils;
import dev.wolfieboy09.eclipsium.routing.RouteProcessor;
import org.slf4j.Logger;

public class Main {
    private final Logger LOGGER = LogUtils.getLogger();
    public static final BaseWebServer webServer = new BaseWebServer(8080);

    public static void main(String[] args) {
        RouteProcessor.registerRoutes(WebPages::new, webServer, true);
        webServer.start();
    }
}
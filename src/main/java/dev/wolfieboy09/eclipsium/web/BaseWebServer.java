package dev.wolfieboy09.eclipsium.web;

import com.mojang.logging.LogUtils;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dev.wolfieboy09.eclipsium.webpages.WebServerFailedToStart;
import dev.wolfieboy09.eclipsium.webpages.WebsiteProvider;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BaseWebServer implements WebsiteProvider {
    private HttpServer server;
    private final int port;
    private final Map<String, HttpHandler> routes = new HashMap<>();
    private final Logger LOGGER = LogUtils.getLogger();

    public BaseWebServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            LOGGER.info("Running on port {}", port);

            for (Map.Entry<String, HttpHandler> route : routes.entrySet()) {
                server.createContext(route.getKey(), route.getValue());
            }

            server.setExecutor(null);
            server.start();
            LOGGER.info("WebServer started successfully");
        } catch (IOException e) {
            throw new WebServerFailedToStart(e.getCause());
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Web server stopped.");
        }
    }

    public void addRoute(String path, HttpHandler handler) {
        if (server != null) {
            throw new IllegalStateException("Cannot add routes after the server has started.");
        }
        routes.put(path, handler);
        LOGGER.debug("Route added: {}", path);
    }

    public int getPort() {
        return this.port;
    }
}

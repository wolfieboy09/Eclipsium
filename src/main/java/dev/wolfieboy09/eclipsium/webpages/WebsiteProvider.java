package dev.wolfieboy09.eclipsium.webpages;


import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public abstract class WebsiteProvider {
    public static Map<String, HttpHandler> routes = new HashMap<>();
    private static HttpServer server;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsiteProvider.class);
    private final int port;

    public WebsiteProvider(int port) {
        this.port = port;
    }

    public final void start() throws WebServerFailedToStart {
        onServerStart(port);
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

    public void onServerStart(int port) {};

    public final void stop() {
        onServerStop();
        if (server != null) server.stop(0);
    }

    public void onServerStop() {}

    final public HttpServer getServer() {
        return server;
    }

    @Contract("!null, _, _ -> fail")
    public final void addRoute(HttpServer server, String path, HttpHandler handler) {
        if (server != null) {
            throw new IllegalStateException("Cannot add routes after the server has started.");
        }
        routes.put(path, handler);
    }
}


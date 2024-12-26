package dev.wolfieboy09.eclipsium.routing;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import dev.wolfieboy09.eclipsium.images.Image;
import dev.wolfieboy09.eclipsium.webpages.WebsiteProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class RouteProcessor {
    public static List<String> registeredRoutes = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void registerRoutes(Supplier<? extends PageProvider> provider, WebsiteProvider webServer) {
        registerRoutes(provider, webServer, false);
    }

    public static void registerRoutes(@NotNull Supplier<? extends PageProvider> provider, WebsiteProvider webServer, boolean accessPrivateMethods) {
        Method[] methods = provider.get().getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (accessPrivateMethods) method.setAccessible(true);

            if (method.isAnnotationPresent(Route.class) && (!Modifier.isPrivate(method.getModifiers()) || accessPrivateMethods)) {
                Route route = method.getAnnotation(Route.class);
                registeredRoutes.add(route.value());
                String path = route.value();

                webServer.addRoute(webServer.getServer(), path, exchange -> {
                    try {
                        Object instance = provider.get();
                        Object response = method.invoke(instance);

                        if (response instanceof String responseBody) {
                            exchange.getResponseHeaders().add("Content-Type", "text/plain");
                            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
                            exchange.getResponseBody().write(responseBody.getBytes());
                        } else if (response instanceof JsonObject jsonResponse) {
                            String responseBody = jsonResponse.toString();
                            exchange.getResponseHeaders().add("Content-Type", "application/json");
                            exchange.sendResponseHeaders(200, responseBody.getBytes().length);
                            exchange.getResponseBody().write(responseBody.getBytes());
                        } else if (response instanceof Image(BufferedImage bufferedImage, Path imagePath)) {
                            String mimeType = getMimeType(imagePath.toString());

                            exchange.getResponseHeaders().set("Content-Type", "image/" + mimeType);
                            exchange.sendResponseHeaders(200, 0);

                            try (OutputStream os = exchange.getResponseBody()) {
                                ImageIO.write(bufferedImage, mimeType, os);
                            }
                        }
                        exchange.getResponseBody().close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(500, 0);
                        exchange.getResponseBody().close();
                    }
                });
            } else if (method.isAnnotationPresent(Route.class)) {
                LOGGER.warn("Skipping private method with @Route annotation: {}", method.getName());
            }
        }
    }

    private static @NotNull String getMimeType(@NotNull String path) {
        String format = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
        return switch (format) {
            case "png" -> "png";
            case "jpg", "jpeg" -> "jpeg";
            case "bmp" -> "bmp";
            case "gif" -> "gif";
            default -> throw new IllegalArgumentException("Unsupported image format: " + format);
        };
    }
}
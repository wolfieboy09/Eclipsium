package dev.wolfieboy09.eclipsium.webpages;

public class WebServerFailedToStart extends RuntimeException {
    public WebServerFailedToStart(String message) {
        super(message);
    }

    public WebServerFailedToStart(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServerFailedToStart(Throwable cause) {
        super(cause);
    }

    public WebServerFailedToStart() {}
}
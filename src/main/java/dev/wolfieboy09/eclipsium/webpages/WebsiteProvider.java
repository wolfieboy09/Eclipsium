package dev.wolfieboy09.eclipsium.webpages;


public interface WebsiteProvider {
    void start() throws WebServerFailedToStart;
    void stop();
}


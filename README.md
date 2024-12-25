<h1><img src="icon.png" alt="Eclipsium Icon" width=100 height=100 /> Eclipsium</h1>
Java HttpServer made easy™


# Setting up a basic server
Let's say you want to greet users on the main (`/`) page.

Step 1:
*Creating the endpoints*

```java
// We implement PageProvider for endpoint registration to capture
// The routes we want to have
public class MyEndpoints implements PageProvider {
    // You can have your init whatever you want
    // But for this example, we're going to have it blank
    public MyWebserver() {}
    
    // We create routes using the annotation with the endpoint we want
    @Route("/")
    public String greetUsers() {
        return "Hello, World!";
    }
    
    // We can return a JsonObject (from GSON) for a JSON response
    // The server will return {"hello": "world"} to the client
    @Route("/json")
    public JsonObject returnJsonResponse() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hello", "world");
        return jsonObject;
    }

    // You can return images to the client by using Image#fromFile (not the java.awt version)
    // Image#fromFile can use a string or a Path as a path to your image.
    @Route("/image")
    public Image returnImage() {
        return Image.fromFile("path/to/image.png");
    }
}

```

Step 2:
*registering the endpoints*

```java
public class MyWebserver {
    // We create an instance of BaseWebServer on port 8080
    private static final BaseWebServer webServer = new BaseWebServer(8080);

    public static void runTheServer() {
        // We must register the endpoints BEFORE the server is started
        // You can't add endpoints as the server is running.
        RouteProcessor.registerRoutes(MyEndpoints::new, webServer);
        // You can also do RouteProcessor.registerRoutes(() -> new MyEndpoints(...), webServer)
        // If you need additional info for your endpoints
        
        // If you have private methods inside your class with @Route attached then you can do
        // RouteProcessor.registerRoutes(MyEndpoints::new, webServer, true);
        // for the server to access private methods, otherwise, they will be skipped during registration    
        
        // Here, we start the webserver
        webServer.start();
    }
}
```


# Additional Notes
If you wanted to get all routes registered, just get the variable `registeredRoutes` from `RouteProcessor`. Varable type is `List<String>`

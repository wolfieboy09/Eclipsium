<h1><img src="icon.png" alt="Eclipsium Icon" width=100 height=100 /> Eclipsium</h1>
Java HttpServer made easy™

# Installation
With Gradle Groovy, you can install Eclipsium by the following:

Add the following:
```groovy
repositories {
  maven {
      name = "Eclipsium" 
      url "https://dl.cloudsmith.io/public/wolfieboy09/eclipsium/maven/"
  }
}
```

Now, you can add Eclipsium by
```groovy
dependencies {
  implementation "dev.wolfieboy09.eclipsium:Eclipsium:${eclipsium_version}"
}
```

Don't forget to add `eclipsium_version` to `gradle.properties`
```properties
eclipsium_version=0.0.1
```

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
    public MyEndpoints() {}
    
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
public class MyWebserver extends WebsiteProvider {
    public MyWebserver(int port) {
        super(port);
    }
    
    // Optional
    @Override
    public void onServerStart(int port) {
        // Do something extra when the server starts
    }

    // Optional
    @Override
    public void onServerStop() {
        // Do something extra when the server stops
    }
}
```

Step 3:
*Starting the server*

```java
public class MyMainClass {
    // Create an instance of the webserver
    // We need to add the port we want to use
    private static MyWebserver webserver = new MyWebServer(8080);
    public static void main(String[] args) {
        // Register the routes
        RouteProcessor.registerRoutes(MyEndpoints::new, webserver);
        webserver.start();
    }
}
```

Congrats! You now just made a simple webserver!
You can access your webserver on `localhost:8080` (or whichever port you used)

# Requirements
* Java 21+

# Additional Notes
If you wanted to get all routes registered, just get the variable `registeredRoutes` from `RouteProcessor`. Variable type is `List<String>`

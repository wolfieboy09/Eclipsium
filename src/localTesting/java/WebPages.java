import com.google.gson.JsonObject;
import dev.wolfieboy09.eclipsium.images.Image;
import dev.wolfieboy09.eclipsium.routing.PageProvider;
import dev.wolfieboy09.eclipsium.routing.Route;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;


public class WebPages implements PageProvider {
    public WebPages() {}

    @Route("/")
    public String index() {
        return "Hello, world!";
    }

    @Contract(" -> new")
    @Route("/image")
    public @NotNull Image createSampleImage() {
        return Image.fromFile(Path.of("src/localTesting/resources/sendme.png"));
    }

    @Route("/json")
    public JsonObject jsonResp() {
        JsonObject data = new JsonObject();
        data.addProperty("millis", System.currentTimeMillis());
        return data;
    }
}

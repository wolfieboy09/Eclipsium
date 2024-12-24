package dev.wolfieboy09.eclipsium.images;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

public record Image(BufferedImage bufferedImage, Path path) {
    @Contract("_ -> new")
    public static @NotNull Image fromFile(Path path) {
        try {
            return new Image(ImageIO.read(new File(path.toString())), path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Contract("_ -> new")
    public static @NotNull Image fromFile(String path) {
        try {
            return new Image(ImageIO.read(new File(path)), Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


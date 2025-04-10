package me.danvb10.mtsr.upscale;

import me.danvb10.mtsr.config.MTSRConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UpscaleManager {
    private static final Path CACHE_DIR = MinecraftClient.getInstance().runDirectory.toPath().resolve("mtsr_cache");

    public static BufferedImage readImage(InputStream stream) throws IOException {
        return ImageIO.read(stream);
    }

    public static BufferedImage getUpscaledImage(Identifier id, BufferedImage input) throws IOException, NoSuchAlgorithmException {
        String hash = generateCacheKey(id, input);
        Path cached = CACHE_DIR.resolve(hash + ".png");

        if (Files.exists(cached)) {
            return ImageIO.read(cached.toFile());
        }

        Path inputPath = CACHE_DIR.resolve(hash + "_input.png");
        Path outputPath = CACHE_DIR.resolve(hash + ".png");

        ImageIO.write(input, "PNG", inputPath.toFile());

        ProcessBuilder pb = new ProcessBuilder("python", "upscale.py", inputPath.toString(), outputPath.toString(), MTSRConfig.get().modelPath);
        pb.directory(new File("."));
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException ignored) {}

        return ImageIO.read(outputPath.toFile());
    }

    public static void injectTexture(Identifier id, BufferedImage img) throws IOException {
        NativeImage nativeImage = NativeImage.read(toInputStream(img));
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(nativeImage));
    }

    private static String generateCacheKey(Identifier id, BufferedImage img) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(id.toString().getBytes());
        digest.update((byte) MTSRConfig.get().scaleFactor);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", baos);
        digest.update(baos.toByteArray());
        return bytesToHex(digest.digest());
    }

    private static InputStream toInputStream(BufferedImage img) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static boolean shouldUpscale(Identifier id) {
        String path = id.getPath();

        if (!MTSRConfig.get().enableUpscaling) return false;
        if (path.contains("/font/") && MTSRConfig.get().excludeFonts) return false;
        if (path.contains("/gui/") && !MTSRConfig.get().upscaleUI) return false;
        if (path.contains("/particle/") && !MTSRConfig.get().upscaleParticles) return false;

        return path.endsWith(".png");
    }
}
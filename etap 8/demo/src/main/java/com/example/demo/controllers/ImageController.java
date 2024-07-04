package com.example.demo.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class ImageController {
    private final TokenManager tokenManager = new TokenManager();
    private final BufferedImage bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

    public ImageController() {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 512, 512);
        g2d.dispose();
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    @GetMapping("/image-page")
    public String getImagePage() {
        return "<html>" +
                "<body>" +
                "<h1>Generated Image</h1>" +
                "<img src='/image' alt='Generated Image' />" +
                "</body>" +
                "</html>";
    }

    @PostMapping(value = "/pixel", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> setPixel(@RequestParam String id, @RequestParam int x, @RequestParam int y,
                                           @RequestParam String color) {
        if (!(tokenManager.isTokenValid(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid");
        }

        if (x < 0 || x >= 512 || y < 0 || y >= 512) {
            return ResponseEntity.badRequest().body("Coordinates out of bounds");
        }

        try {
            int rgb = Integer.parseInt(color, 16);
            bufferedImage.setRGB(x, y, rgb);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid color format");
        }

        return ResponseEntity.ok("Pixel set successfully");
    }

    @PostMapping("/ban")
    public ResponseEntity<String> banToken(@RequestParam String token) {
        int recordsRemoved = tokenManager.banToken(token);
        regenerateImage();
        return ResponseEntity.ok("Token banned. Records removed: " + recordsRemoved);
    }

    private void regenerateImage() {
        // Reset the image
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 512, 512);

        // Re-draw the image based on remaining tokens (this is a simplified example)
        for (TokenInfo tokenInfo : tokenManager.getAllTokens()) {
            // Draw pixels based on remaining tokens
            // Example: bufferedImage.setRGB(x, y, color);
        }

        g2d.dispose();
    }
}

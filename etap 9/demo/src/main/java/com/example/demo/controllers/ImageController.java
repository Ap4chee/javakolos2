package com.example.demo.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {
    private final TokenManager tokenManager = new TokenManager();
    private final BufferedImage bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
    private final List<String> changeLog = new ArrayList<>(); // Logowanie zmian

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

            // Logowanie zmian
            String changeLogEntry = String.format("x:%d,y:%d,color:%s", x, y, color);
            changeLog.add(changeLogEntry);
            saveImage();
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

    public void generateVideo() {
        try {
            File tempDir = new File("temp_images");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            for (int i = 0; i < changeLog.size(); i++) {
                BufferedImage imageCopy = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
                Graphics g = imageCopy.getGraphics();
                g.drawImage(bufferedImage, 0, 0, null);

                // Apply changes up to the current log entry
                for (int j = 0; j <= i; j++) {
                    String[] parts = changeLog.get(j).split(",");
                    int x = Integer.parseInt(parts[0].split(":")[1]);
                    int y = Integer.parseInt(parts[1].split(":")[1]);
                    int color = Integer.parseInt(parts[2].split(":")[1], 16);
                    imageCopy.setRGB(x, y, color);
                }

                File outputfile = new File(tempDir, String.format("frame_%04d.png", i));
                ImageIO.write(imageCopy, "png", outputfile);
                g.dispose();
            }

            // Create video using ffmpeg
            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-framerate", "30", "-pattern_type", "glob", "-i", "temp_images/frame_*.png", "-c:v", "libx265", "out.mp4");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

            // Clean up temporary images
            for (File file : tempDir.listFiles()) {
                file.delete();
            }
            tempDir.delete();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveImage() {
        try {
            ImageIO.write(bufferedImage, "png", new File("current_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

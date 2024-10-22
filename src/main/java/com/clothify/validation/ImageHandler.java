package com.clothify.validation;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageHandler {

    //Encode Image using base64
    private String encodeImageFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] imageBytes = new byte[(int) file.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    //Decode Image
    public static Image decodeImageFile(String encodedImage) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return new Image(inputStream);
    }
}

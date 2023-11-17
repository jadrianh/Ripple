package org.ripple.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

class Contact {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BufferedImage profileImage;

    private static final List<String> IMAGE_PATHS = List.of(
            "/images/drawable-pictures/profile00.png",
            "/images/drawable-pictures/profile01.png",
            "/images/drawable-pictures/profile02.png",
            "/images/drawable-pictures/profile03.png",
            "/images/drawable-pictures/profile04.png",
            "/images/drawable-pictures/profile05.png",
            "/images/drawable-pictures/profile06.png",
            "/images/drawable-pictures/profile07.png",
            "/images/drawable-pictures/profile08.png",
            "/images/drawable-pictures/profile09.png",
            "/images/drawable-pictures/profile10.png",
            "/images/drawable-pictures/profile11.png",
            "/images/drawable-pictures/profile12.png"
    );

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        assignRandomProfileImage();
    }

    private void assignRandomProfileImage() {
        Random random = new Random();
        int randomIndex = random.nextInt(IMAGE_PATHS.size());
        String imagePath = IMAGE_PATHS.get(randomIndex);
        try {
            this.profileImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getProfileImage() {
        return profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

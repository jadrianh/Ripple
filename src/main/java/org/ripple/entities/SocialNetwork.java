package org.ripple.entities;
public class SocialNetwork {
    private int idSocial;
    private String socialNetwork;
    private String socialUsername;

    public SocialNetwork(int idSocial, String socialNetwork, String socialUsername) {
        this.idSocial = idSocial;
        this.socialNetwork = socialNetwork;
        this.socialUsername = socialUsername;
    }

    public int getIdSocial() {
        return idSocial;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public String getSocialUsername() {
        return socialUsername;
    }
}


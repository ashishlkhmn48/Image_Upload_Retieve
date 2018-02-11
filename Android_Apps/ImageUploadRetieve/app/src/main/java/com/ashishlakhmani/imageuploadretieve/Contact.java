package com.ashishlakhmani.imageuploadretieve;

public class Contact {
    String name;
    String email;
    String profilePic;

    public Contact(String name, String email, String profilePic) {
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }
}

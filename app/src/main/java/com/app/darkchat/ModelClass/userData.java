package com.app.darkchat.ModelClass;

public class userData {
    String Name ,Email, phone ,Password ,key,imageUri,status ;

    public userData() {

    }

    public userData(String name, String email, String phone, String password, String key, String imageUri, String status) {
        Name = name;
        Email = email;
        this.phone = phone;
        Password = password;
        this.key = key;
        this.imageUri = imageUri;
        this.status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.nepali.nepali_app.nepali_app;

public class NepaliWords {
    private int id;
    private String origin;
    private String sound;
    private String meaning;
    private String role;


    public NepaliWords(int id, String origin, String sound, String meaning,String role) {
        this.id = id;
        this.origin = origin;
        this.sound = sound;
        this.meaning = meaning;
        this.role = role;

    }

    public String getOrigin() {
        return origin;
    }
    public String getSound() {
        return sound;
    }
    public String getMeaning() {
        return meaning;
    }
    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}

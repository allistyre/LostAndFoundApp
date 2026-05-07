package com.example.lostandfoundapp;

public class LostFoundItem {
    public LostFoundItem(String location, int id, String postType, String name, String phone,
                         String description, String date, String category, String image,
                         String timestamp) {
        this.location = location;
        this.id = id;
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.category = category;
        this.image = image;
        this.timestamp = timestamp;
    }

    private int id;
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String location;
    private String date;
    private String category;
    private String image;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPostType() {
        return postType;
    }

    public int getId() {
        return id;
    }

}

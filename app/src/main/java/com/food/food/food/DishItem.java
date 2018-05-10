package com.food.food.food;

import org.json.JSONArray;

public class DishItem {

    private String nazwa;
    private String cena;
    private String czas;
    private String img;
    private String id;
    private String restaurantId;
    private Double average;
    private JSONArray comments;

    public DishItem(String nazwa, String cena, String czas, String img, String id, String restaurantId, Double average, JSONArray comments){
        this.nazwa = nazwa;
        this.cena = cena;
        this.czas = czas;
        this.img = img;
        this.id = id;
        this.restaurantId = restaurantId;
        this.average = average;
        this.comments = comments;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getCena() {
        return cena;
    }

    public String getCzas() {
        return czas;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public Double getAverage() {
        return average;
    }

    public JSONArray getComments() {
        return comments;
    }
}

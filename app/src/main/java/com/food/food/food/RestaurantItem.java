package com.food.food.food;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class RestaurantItem implements Serializable {
    private String name;
    private String address;
    private String id;
    private String img;
//    private Map<String, String> availableFood;

    public RestaurantItem(String id, String name, String address, String img){//, Map<String, String> availableFood
        this.name = name;
        this.address = address;
        this.id = id;
        this.img = img;
//        this.availableFood = availableFood;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getImg() { return img; }

    //    public Map<String, String> getAvailableFood() {
//        return availableFood;
//    }
}

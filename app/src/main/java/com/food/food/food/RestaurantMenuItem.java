package com.food.food.food;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RestaurantMenuItem {

    private String image;
    private String text;
    private String menuList;

    public RestaurantMenuItem(String image, String text, String menuList){
        this.image = image;
        this.text = text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getImage(){
        return image;
    }

    public String getText() {
        return text;
    }

    public String getMenuList() {
        return menuList;
    }
}

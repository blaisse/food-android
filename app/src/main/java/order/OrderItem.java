package order;

public class OrderItem {

    private String id;
    private String dishes;
    private String price;
    private String date;
    private String user;
    private String restaurantId;
    private String restaurantName;

    public OrderItem(String id, String dishes, String price, String date, String user, String restaurantId, String restaurantName) {
        this.id = id;
        this.dishes = dishes;
        this.price = price;
        this.date = date;
        this.user = user;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public String getId() {
        return id;
    }

    public String getDishes() {
        return dishes;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}

package cart;

public class CartItem {

    private String id;
    private String name;
    private String price;
    private String time;
    private String img;
    private String amount;
    private String restaurantId;

    public CartItem(String id, String name, String price, String time, String img, String amount, String restaurantId){
        this.id = id;
        this.name = name;
        this.price = price;
        this.time = time;
        this.img = img;
        this.amount = amount;
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getImg() {
        return img;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRestaurantId() { return restaurantId; }
}

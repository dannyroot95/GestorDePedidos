package aukde.food.aukdeliver.paquetes.Modelos;

import java.io.Serializable;

public class Cart implements Serializable {

    String user_id;
    String provider_id;
    String product_id;
    String title;
    String price;
    String image;
    String cart_quantity;
    String stock_quantity;
    String id;

    public Cart(){}

    public Cart(String user_id, String provider_id, String product_id, String title, String price,
                String image, String cart_quantity, String stock_quantity, String id) {
        this.user_id = user_id;
        this.provider_id = provider_id;
        this.product_id = product_id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.cart_quantity = cart_quantity;
        this.stock_quantity = stock_quantity;
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(String stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package aukde.food.aukdeliver.paquetes.Modelos;

import java.io.Serializable;
import java.util.ArrayList;


public class Order implements Serializable {

    String user_id;
    ArrayList<Cart> items;
    Address address;
    String title;
    String image;
    String sub_total_amount;
    String shipping_charge;
    String total_amount;
    Long order_datetime= 0L;
    String id;
    Integer status = 0;
    String driver_id;

    public Order(){}

    public Order(String user_id, ArrayList<Cart> items, Address address, String title, String image,
                 String sub_total_amount, String shipping_charge, String total_amount, Long order_datetime,
                 String id, Integer status, String driver_id) {
        this.user_id = user_id;
        this.items = items;
        this.address = address;
        this.title = title;
        this.image = image;
        this.sub_total_amount = sub_total_amount;
        this.shipping_charge = shipping_charge;
        this.total_amount = total_amount;
        this.order_datetime = order_datetime;
        this.id = id;
        this.status = status;
        this.driver_id = driver_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Cart> getItems() {
        return items;
    }

    public void setItems(ArrayList<Cart> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSub_total_amount() {
        return sub_total_amount;
    }

    public void setSub_total_amount(String sub_total_amount) {
        this.sub_total_amount = sub_total_amount;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public Long getOrder_datetime() {
        return order_datetime;
    }

    public void setOrder_datetime(Long order_datetime) {
        this.order_datetime = order_datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }
}

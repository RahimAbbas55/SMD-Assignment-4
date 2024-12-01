package com.example.shopping_list;

public class ItemModal {
    private String key;
    private String item_name;
    private float price;
    private int quantity;

    public ItemModal() {}
    public ItemModal(String key , String item_name, float price, int quantity) {
        this.key = key;
        this.item_name = item_name;
        this.price = price;
        this.quantity = quantity;
    }
    public String getKey(){
        return key;
    }
    public String getItem_name() {
        return item_name;
    }
    public float getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

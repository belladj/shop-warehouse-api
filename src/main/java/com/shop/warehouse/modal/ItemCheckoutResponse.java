package com.shop.warehouse.modal;

import java.util.ArrayList;
import java.util.List;

public class ItemCheckoutResponse {
    private List<Item> items;
    private int amount;
    private String totalPrice;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = new ArrayList<>(items);
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String price) {
        this.totalPrice = price;
    }
}

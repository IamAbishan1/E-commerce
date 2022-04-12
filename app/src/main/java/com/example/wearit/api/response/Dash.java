package com.example.wearit.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dash {

    @SerializedName("customers")
    @Expose
    private Integer customers;
    @SerializedName("processingOrders")
    @Expose
    private Integer processingOrders;
    @SerializedName("pendingOrders")
    @Expose
    private Integer pendingOrders;
    @SerializedName("shippedOrders")
    @Expose
    private Integer shippedOrders;
    @SerializedName("products")
    @Expose
    private Integer products;
    @SerializedName("categories")
    @Expose
    private Integer categories;

    public Integer getCustomers() {
        return customers;
    }

    public void setCustomers(Integer customers) {
        this.customers = customers;
    }

    public Integer getProcessingOrders() {
        return processingOrders;
    }

    public void setProcessingOrders(Integer processingOrders) {
        this.processingOrders = processingOrders;
    }

    public Integer getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Integer pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Integer getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(Integer shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public Integer getProducts() {
        return products;
    }

    public void setProducts(Integer products) {
        this.products = products;
    }

    public Integer getCategories() {
        return categories;
    }

    public void setCategories(Integer categories) {
        this.categories = categories;
    }


}

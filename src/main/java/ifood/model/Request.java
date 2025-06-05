package ifood.model;

import java.time.LocalDateTime;

public class Request {
    private int id;
    private double totalValue;
    private double deliveryValue;
    private Client client;
    private Restaurant restaurant;
    private Address addressDelivery;
    private StatusRequest statusRequest;
    private TypePayment typePayment;
    private StatusPayment statusPayment;
    private LocalDateTime requestDate;
    private LocalDateTime deliveryDate;
    private int deliveryTimeInMinutes;

    public Request() {}

    public Request(int id, double totalValue, double deliveryValue, Client client, Restaurant restaurant,
                   Address addressDelivery, StatusRequest statusRequest, TypePayment typePayment,
                   StatusPayment statusPayment, LocalDateTime requestDate, LocalDateTime deliveryDate,
                   int deliveryTimeInMinutes) {
        this.id = id;
        this.totalValue = totalValue;
        this.deliveryValue = deliveryValue;
        this.client = client;
        this.restaurant = restaurant;
        this.addressDelivery = addressDelivery;
        this.statusRequest = statusRequest;
        this.typePayment = typePayment;
        this.statusPayment = statusPayment;
        this.requestDate = requestDate;
        this.deliveryDate = deliveryDate;
        this.deliveryTimeInMinutes = deliveryTimeInMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getDeliveryValue() {
        return deliveryValue;
    }

    public void setDeliveryValue(double deliveryValue) {
        this.deliveryValue = deliveryValue;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Address getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(Address addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

    public StatusRequest getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(StatusRequest statusRequest) {
        this.statusRequest = statusRequest;
    }

    public TypePayment getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(TypePayment typePayment) {
        this.typePayment = typePayment;
    }

    public StatusPayment getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(StatusPayment statusPayment) {
        this.statusPayment = statusPayment;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getDeliveryTimeInMinutes() {
        return deliveryTimeInMinutes;
    }

    public void setDeliveryTimeInMinutes(int deliveryTimeInMinutes) {
        this.deliveryTimeInMinutes = deliveryTimeInMinutes;
    }
}
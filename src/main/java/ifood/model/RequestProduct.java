package ifood.model;

public class RequestProduct {
    private Request request;
    private Product product;
    private String notes;
    private int quantity;
    private double itemPrice;

    public RequestProduct() {}

    public RequestProduct(Request request, Product product, String notes, int quantity, double itemPrice) {
        this.request = request;
        this.product = product;
        this.notes = notes;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}

package ifood.model;

public class Restaurant {
    private int id;
    private String name;
    private String description;
    private String openingHours;
    private Address address;

    public Restaurant() {}

    public Restaurant(int id, String name, String description, String openingHours, Address address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openingHours = openingHours;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

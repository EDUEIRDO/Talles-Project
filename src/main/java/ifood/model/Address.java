package ifood.model;

public class Address {
    private int id;
    private String street;
    private String district;
    private String city;
    private String state;
    private int houseNumber;

    Address() {}

    Address(int id, String street, String district, String city, String state, int houseNumber) {
        this.id = id;
        this.street = street;
        this.district = district;
        this.city = city;
        this.state = state;
        this.houseNumber = houseNumber;
    }

    public int getId() {
        return id;

    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return street;
    }

}

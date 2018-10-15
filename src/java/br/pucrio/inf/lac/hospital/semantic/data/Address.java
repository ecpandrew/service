package br.pucrio.inf.lac.hospital.semantic.data;

public class Address {

    private long addressID;
    private String neighborhood;
    private String city;
    private String state;
    private String street;
    private int number;
    private String zipcode;
    private String additionalInfo;

    public Address() {
    }

    public Address(long addressID, String neighborhood, String city, String state, String street, int number) {
        this.addressID = addressID;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.street = street;
        this.number = number;
    }
    
    public Address(String neighborhood, String city, String state, String street, int number, String zipcode, String additionalInfo) {
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.street = street;
        this.number = number;
        this.zipcode = zipcode;
        this.additionalInfo = additionalInfo;
    }

    public Address(long addressID, String neighborhood, String city, String state, String street, int number, String zipcode, String additionalInfo) {
        this.addressID = addressID;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.street = street;
        this.number = number;
        this.zipcode = zipcode;
        this.additionalInfo = additionalInfo;
    }

    public long getAddressID() {
        return addressID;
    }

    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "Address{" + "addressID=" + addressID + ", neighborhood=" + neighborhood + ", city=" + city + ", state=" + state + ", street=" + street + ", number=" + number + ", zipcode=" + zipcode + ", additionalInfo=" + additionalInfo + '}';
    }

    
}

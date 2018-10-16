package br.pucrio.inf.lac.hospital.semantic.data;

public class City {

    private long cityID;
    private String cityName;

    public City() {
    }

    public City(long cityID, String cityName) {
        this.cityID = cityID;
        this.cityName = cityName;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "City{" + "cityID=" + cityID + ", cityName=" + cityName + '}';
    }
    
}

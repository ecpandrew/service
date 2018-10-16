package br.pucrio.inf.lac.hospital.semantic.data;

public class Building {

    private long buildingID;
    private String buildingName;
    private long cityID;

    public Building() {
    }

    public Building(long buildingID, String buildingName, long cityID) {
        this.buildingID = buildingID;
        this.buildingName = buildingName;
        this.cityID = cityID;
    }

    public long getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(long buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }
    
    @Override
    public String toString() {
        return "Building{" + "buildingID=" + buildingID + ", buildingName=" + buildingName + ", cityID=" + cityID + '}';
    }
    
}

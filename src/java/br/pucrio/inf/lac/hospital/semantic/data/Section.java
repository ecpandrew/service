package br.pucrio.inf.lac.hospital.semantic.data;

public class Section {

    private long sectionID;
    private String sectionName;
    private long buildingID;

    public Section() {
    }

    public Section(long sectionID, String sectionName, long buildingID) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.buildingID = buildingID;
    }

    public long getSectionID() {
        return sectionID;
    }

    public void setSectionID(long sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public long getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(long buildingID) {
        this.buildingID = buildingID;
    }
    
    @Override
    public String toString() {
        return "Section{" + "sectionID=" + sectionID + ", sectionName=" + sectionName + ", buildingID=" + buildingID + '}';
    }
    
}

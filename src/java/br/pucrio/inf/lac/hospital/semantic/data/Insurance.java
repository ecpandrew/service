package br.pucrio.inf.lac.hospital.semantic.data;

public class Insurance {
    long insuranceID;
    String insuranceName;

    public Insurance() {
    }

    public Insurance(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public Insurance(long insuranceID, String insuranceName) {
        this.insuranceID = insuranceID;
        this.insuranceName = insuranceName;
    }

    public long getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(long insuranceID) {
        this.insuranceID = insuranceID;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    @Override
    public String toString() {
        return "Insurance{" + "insuranceID=" + insuranceID + ", insuranceName=" + insuranceName + '}';
    }
    
}

package br.pucrio.inf.lac.hospital.semantic.data;

public class Hospital {
    private long hospitalID;//Modified to long
    private String hospitalName;
    private long addressID;//Added
    double latitude;//Added
    double longitude;//Added
    //Removed address atributes

    public Hospital() {
    }

    public Hospital(String hospitalName, long addressID, double latitude, double longitude) {
        this.hospitalName = hospitalName;
        this.addressID = addressID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Hospital(long hospitalID, String hospitalName, long addressID, double latitude, double longitude) {
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
        this.addressID = addressID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public long getAddressID() {
        return addressID;
    }

    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Hospital{" + "hospitalID=" + hospitalID + ", hospitalName=" + hospitalName + ", addressID=" + addressID + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
    
}

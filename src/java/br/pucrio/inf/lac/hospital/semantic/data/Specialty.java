package br.pucrio.inf.lac.hospital.semantic.data;

public class Specialty {

    private long specialtyID;
    private String specialtyname;

    public Specialty() {
    }

    public Specialty(String specialtyname) {
        this.specialtyname = specialtyname;
    }

    public Specialty(long specialtyID, String specialtyname) {
        this.specialtyID = specialtyID;
        this.specialtyname = specialtyname;
    }

    public long getSpecialtyID() {
        return specialtyID;
    }

    public void setSpecialtyID(long specialtyID) {
        this.specialtyID = specialtyID;
    }

    public String getSpecialtyname() {
        return specialtyname;
    }

    public void setSpecialtyname(String specialtyname) {
        this.specialtyname = specialtyname;
    }

    @Override
    public String toString() {
        return "Specialty{" + "specialtyID=" + specialtyID + ", specialtyname=" + specialtyname + '}';
    }
    
}

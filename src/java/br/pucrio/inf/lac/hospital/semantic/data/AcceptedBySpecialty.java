package br.pucrio.inf.lac.hospital.semantic.data;

import java.sql.Date;

public class AcceptedBySpecialty {

    private long acceptedBySpecialtyID;
    private long hospitalID;
    private long insuranceID;
    private long specialtyID;
    private Date begin;
    private Date end;

    public AcceptedBySpecialty() {
    }

    public AcceptedBySpecialty(long hospitalID, long insuranceID, long specialtyID, Date begin, Date end) {
        this.hospitalID = hospitalID;
        this.insuranceID = insuranceID;
        this.specialtyID = specialtyID;
        this.begin = begin;
        this.end = end;
    }

    public AcceptedBySpecialty(long acceptedBySpecialtyID, long hospitalID, long insuranceID, long specialtyID, Date begin, Date end) {
        this.acceptedBySpecialtyID = acceptedBySpecialtyID;
        this.hospitalID = hospitalID;
        this.insuranceID = insuranceID;
        this.specialtyID = specialtyID;
        this.begin = begin;
        this.end = end;
    }

    public long getAcceptedBySpecialtyID() {
        return acceptedBySpecialtyID;
    }

    public void setAcceptedBySpecialtyID(long acceptedBySpecialtyID) {
        this.acceptedBySpecialtyID = acceptedBySpecialtyID;
    }

    public long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    public long getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(long insuranceID) {
        this.insuranceID = insuranceID;
    }

    public long getSpecialtyID() {
        return specialtyID;
    }

    public void setSpecialtyID(long specialtyID) {
        this.specialtyID = specialtyID;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "AcceptedBySpecialty{" + "acceptedBySpecialtyID=" + acceptedBySpecialtyID + ", hospitalID=" + hospitalID + ", insuranceID=" + insuranceID + ", specialtyID=" + specialtyID + ", begin=" + begin + ", end=" + end + '}';
    }

    
}

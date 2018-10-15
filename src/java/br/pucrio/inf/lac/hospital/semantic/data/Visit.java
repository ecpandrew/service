package br.pucrio.inf.lac.hospital.semantic.data;

import java.sql.Date;
import java.util.UUID;

public class Visit {

    private long visitID;
    private long patientMHubID;
    private long hospitalID;
    private long specialtyID;
    private Date date;
    private byte hospitalScore;
    private byte specialtyScore;
    private String diagnostic;
    private String reportedSymptoms;

    public Visit() {
    }

    public Visit(long patientMHubID, long hospitalID, long specialtyID, Date date, byte hospitalScore, byte specialtyScore) {
        this.patientMHubID = patientMHubID;
        this.hospitalID = hospitalID;
        this.specialtyID = specialtyID;
        this.date = date;
        this.hospitalScore = hospitalScore;
        this.specialtyScore = specialtyScore;
    }

    public Visit(long patientMHubID, long hospitalID, long specialtyID, Date date, byte hospitalScore, byte specialtyScore, String diagnostic, String reportedSymptoms) {
        this.patientMHubID = patientMHubID;
        this.hospitalID = hospitalID;
        this.specialtyID = specialtyID;
        this.date = date;
        this.hospitalScore = hospitalScore;
        this.specialtyScore = specialtyScore;
        this.diagnostic = diagnostic;
        this.reportedSymptoms = reportedSymptoms;
    }

    public Visit(long visitID, long patientMHubID, long hospitalID, long specialtyID, Date date, byte hospitalScore, byte specialtyScore, String diagnostic, String reportedSymptoms) {
        this.visitID = visitID;
        this.patientMHubID = patientMHubID;
        this.hospitalID = hospitalID;
        this.specialtyID = specialtyID;
        this.date = date;
        this.hospitalScore = hospitalScore;
        this.specialtyScore = specialtyScore;
        this.diagnostic = diagnostic;
        this.reportedSymptoms = reportedSymptoms;
    }

    public long getVisitID() {
        return visitID;
    }

    public void setVisitID(long visitID) {
        this.visitID = visitID;
    }

    public long getPatientMHubID() {
        return patientMHubID;
    }

    public void setPatientMHubID(long patientMHubID) {
        this.patientMHubID = patientMHubID;
    }

    public long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    public long getSpecialtyID() {
        return specialtyID;
    }

    public void setSpecialtyID(long specialtyID) {
        this.specialtyID = specialtyID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte getHospitalScore() {
        return hospitalScore;
    }

    public void setHospitalScore(byte hospitalScore) {
        this.hospitalScore = hospitalScore;
    }

    public byte getSpecialtyScore() {
        return specialtyScore;
    }

    public void setSpecialtyScore(byte specialtyScore) {
        this.specialtyScore = specialtyScore;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getReportedSymptoms() {
        return reportedSymptoms;
    }

    public void setReportedSymptoms(String reportedSymptoms) {
        this.reportedSymptoms = reportedSymptoms;
    }

    @Override
    public String toString() {
        return "Visit{" + "visitID=" + visitID + ", patientMHubID=" + patientMHubID + ", hospitalID=" + hospitalID + ", specialtyID=" + specialtyID + ", date=" + date + ", hospitalScore=" + hospitalScore + ", specialtyScore=" + specialtyScore + ", diagnostic=" + diagnostic + ", reportedSymptoms=" + reportedSymptoms + '}';
    }

    
    
}

package br.pucrio.inf.lac.hospital.semantic.data;

import java.sql.Date;
import java.util.UUID;

public class PatientMHub {
    
    private long patientMHubID;
    private UUID mhubID;
    private String name;
    private Date birth;

    public PatientMHub() {
    }

    public PatientMHub(UUID mhubID, String name, Date birth) {
        this.mhubID = mhubID;
        this.name = name;
        this.birth = birth;
    }

    public PatientMHub(long patientMHubID, UUID mhubID, String name, Date birth) {
        this.patientMHubID = patientMHubID;
        this.mhubID = mhubID;
        this.name = name;
        this.birth = birth;
    }

    public long getPatientMHubID() {
        return patientMHubID;
    }

    public void setPatientMHubID(long patientMHubID) {
        this.patientMHubID = patientMHubID;
    }

    public UUID getMhubID() {
        return mhubID;
    }

    public void setMhubID(UUID mhubID) {
        this.mhubID = mhubID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "PatientMHub{" + "patientMHubID=" + patientMHubID + ", mhubID=" + mhubID + ", name=" + name + ", birth=" + birth + '}';
    }

    
    
}

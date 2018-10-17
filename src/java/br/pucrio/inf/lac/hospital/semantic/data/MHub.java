package br.pucrio.inf.lac.hospital.semantic.data;

import java.util.UUID;

public class MHub {
    private UUID mhubID;

    public MHub() {
    }

    public MHub(UUID mhubID) {
        this.mhubID = mhubID;
    }
    
    public UUID getMHubID() {
        return mhubID;
    }

    public void setMHubID(UUID mhubID) {
        this.mhubID = mhubID;
    }
    
    @Override
    public String toString() {
        return "MHub{" + "mhubID=" + mhubID + '}';
    }
    
}

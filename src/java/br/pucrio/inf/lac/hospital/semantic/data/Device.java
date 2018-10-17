package br.pucrio.inf.lac.hospital.semantic.data;

import java.util.UUID;

public class Device {

    private long deviceID;
    private String manufacturer;
    private String model;
    private UUID mhubID;
    private UUID thingID;

    public Device() {
    }

    public Device(long deviceID, String manufacturer, String model, UUID mhubID, UUID thingID) {
        this.deviceID = deviceID;
        this.manufacturer = manufacturer;
        this.model = model;
        this.mhubID = mhubID;
        this.thingID = thingID;
    }
    
    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public UUID getMhubID() {
        return mhubID;
    }

    public void setMhubID(UUID mhubID) {
        this.mhubID = mhubID;
    }

    public UUID getThingID() {
        return thingID;
    }

    public void setThingID(UUID thingID) {
        this.thingID = thingID;
    }
    
    @Override
    public String toString() {
        return "Device{" + "deviceID=" + deviceID + ", manufacturer=" + manufacturer + ", model=" + model + ", mhubID=" + mhubID + ", thingID=" + thingID + '}';
    }
    
}

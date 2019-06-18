package br.ufma.lsdi.smartlab.service.data;

import java.util.UUID;

public class Device {

    private long deviceID;
    private String manufacturer;
    private String model;
    private UUID mhubID;
    private UUID thingID;
    private UUID uuID;

    public Device() {
    }

    public Device(long deviceID, String manufacturer, String model, UUID mhubID, UUID thingID) {
        this.deviceID = deviceID;
        this.manufacturer = manufacturer;
        this.model = model;
        this.mhubID = mhubID;
        this.thingID = thingID;
    }
    
    public Device(UUID uuID, String manufacturer) {
        this.uuID = uuID;
        this.manufacturer = manufacturer;
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

    public UUID getMHubID() {
        return mhubID;
    }

    public void setMHubID(UUID mhubID) {
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

    public UUID getUuID() {
        return uuID;
    }

    public void setUuID(UUID uuID) {
        this.uuID = uuID;
    }
    
}

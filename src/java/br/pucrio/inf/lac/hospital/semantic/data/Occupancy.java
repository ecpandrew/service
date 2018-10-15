package br.pucrio.inf.lac.hospital.semantic.data;

public class Occupancy {
    private int nPatientsNow;
    private double avgWaitTime;

    public Occupancy() {
    }

    public Occupancy(int nPatientsNow, double avgWaitTime) {
        this.nPatientsNow = nPatientsNow;
        this.avgWaitTime = avgWaitTime;
    }

    public int getnPatientsNow() {
        return nPatientsNow;
    }

    public void setnPatientsNow(int nPatientsNow) {
        this.nPatientsNow = nPatientsNow;
    }

    public double getAvgWaitTime() {
        return avgWaitTime;
    }

    public void setAvgWaitTime(double avgWaitTime) {
        this.avgWaitTime = avgWaitTime;
    }
}

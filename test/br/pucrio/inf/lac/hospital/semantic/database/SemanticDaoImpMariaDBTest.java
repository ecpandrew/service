package br.pucrio.inf.lac.hospital.semantic.database;
import br.pucrio.inf.lac.hospital.semantic.data.*;
import java.sql.Date;

import java.util.UUID;
public class SemanticDaoImpMariaDBTest {
    
    private static SemanticDaoImpMariaDB dao = new SemanticDaoImpMariaDB();
    
    long adID, hID, rID, bID, pID, sID, vID, iID, acID;

    public SemanticDaoImpMariaDBTest() {
    }
    
    public static void main(String[] args) {
        SemanticDaoImpMariaDBTest mariaDBTest = new SemanticDaoImpMariaDBTest();
        System.out.println("\n*************Insert Test*************\n");
        mariaDBTest.testInsert();
        System.out.println("\n*************Get Test*************\n");
        mariaDBTest.testGet();
        System.out.println("\n*************Specific Get Test*************\n");
        mariaDBTest.testSpecificGet();
        System.out.println("\n*************Update Test*************\n");
        mariaDBTest.testUpdate();
        System.out.println("\n*************Delete Test*************\n");
        mariaDBTest.testDelete();
    }

    public void testInsert(){
        Address ad = new Address("neighborhood", "city", "st", "street", 10, "87943-749", "addtitionalInfo");
        adID = dao.insertAddress(ad);
        System.out.println("Insert Address = "+adID);
        
        Hospital h = new Hospital("hospital Name", adID, 58.982, 32.932);
        hID = dao.insertHospital(h);
        System.out.println("Insert Hospital = "+hID);
        
        Room r = new Room("rooName", "roomType", hID);
        rID = dao.insertRoom(r);
        System.out.println("Insert Room = "+rID);
        
        Beacon b = new Beacon(rID, UUID.randomUUID(), true, new Date(System.currentTimeMillis()));
        bID = dao.insertBeacon(b);
        System.out.println("Insert Beacon = "+bID);
        
        PatientMHub p = new PatientMHub(UUID.randomUUID(), "patientName", new Date(System.currentTimeMillis()));
        pID = dao.insertPatientMHub(p);
        System.out.println("Insert PatientMHub = "+pID);
        
        Specialty s = new Specialty("Specialty"+UUID.randomUUID());
        sID = dao.insertSpecialty(s);
        System.out.println("Insert Specialty = "+sID);
        
        Visit v = new Visit(pID,hID, sID, new Date(System.currentTimeMillis()), (byte)5, (byte)5, "diagnostic", "simptoms");
        vID = dao.insertVisit(v);
        System.out.println("Insert Visit = "+vID);
        
        Insurance i = new Insurance("Insurance"+UUID.randomUUID());
        iID = dao.insertInsurance(i);
        System.out.println("Insert Insurance = "+iID);
        
        AcceptedBySpecialty ac = new AcceptedBySpecialty(hID, iID, sID, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        acID = dao.insertAcceptedBySpecialty(ac);
        System.out.println("Insert AcceptedBySpecialty = "+acID);
    }
    
    public void testUpdate(){
        Beacon b = new Beacon(bID, rID, UUID.randomUUID(), true, new Date(System.currentTimeMillis()));
        System.out.println("Update Beacon = "+dao.updateBeacon(b));
        
        Room r = new Room(rID, "rooName2", "roomType2", hID);
        System.out.println("Update Room = "+dao.updateRoom(r));
        
        Visit v = new Visit(vID,pID,hID, sID, new Date(System.currentTimeMillis()), (byte)5, (byte)5, null, null);
        System.out.println("Update Visit = "+dao.updateVisit(v));
        
        PatientMHub p = new PatientMHub(pID,UUID.randomUUID(), "patientName", new Date(System.currentTimeMillis()));
        System.out.println("Update PatientMHub = "+dao.updatePatientMHub(p));
        
        AcceptedBySpecialty ac = new AcceptedBySpecialty(acID, hID, iID, sID, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        System.out.println("Update AcceptedBySpecialty = "+dao.updateAcceptedBySpecialty(ac));
        
        Insurance i = new Insurance(iID,"Plano C");
        System.out.println("Update Insurance = "+dao.updateInsurance(i));
        
        Specialty s = new Specialty(sID, "Specialty"+UUID.randomUUID());
        System.out.println("Update Specialty = "+dao.updateSpecialty(s));
        
        Hospital h = new Hospital(hID, "hospital", adID, 58.982, 32.932);
        System.out.println("Update Hospital = "+dao.updateHospital(h));
        
        Address ad = new Address(adID, "neigh", "city", "st", "street", 10, "87943-749", "quadra 12");
        System.out.println("Update Address = "+dao.updateAddress(ad));
    }
    
    public void testGet(){
        System.out.println("Beacon:\n");
        System.out.println(dao.getBeacons());
        
        System.out.println("\n\nRoom:\n");
        System.out.println(dao.getRooms());
        
        System.out.println("\n\nVisit:\n");
        System.out.println(dao.getVisits());
        
        System.out.println("\n\nPatientMHub:\n");
        System.out.println(dao.getPatientMHubs());
        
        System.out.println("\n\nAcceptedBySpecialty:\n");
        System.out.println(dao.getAcceptedBySpecialties());
        
        System.out.println("\n\nInsurance:\n");
        System.out.println(dao.getInsurances());
                
        System.out.println("\n\nSpecialty:\n");
        System.out.println(dao.getSpecialties());
                
        System.out.println("\n\nHospital:\n");
        System.out.println(dao.getHospitals());
                
        System.out.println("\n\nAddress:\n");
        System.out.println(dao.getAddresses());
    }
    
    public void testSpecificGet(){
        System.out.println("Hospital by City\n");
        System.out.println(dao.getHospitalsByCity("city"));
        System.out.println("\n\nHospital by hospitalID\n");
        System.out.println(dao.getHospital(hID));
        System.out.println("\n\nHospital by City and Specialty\n");
        System.out.println(dao.getHospitalsByCityAndSpecialty("city", sID));
        System.out.println("\n\nHospital by City, Specialty and Insurance\n");
        System.out.println(dao.getHospitalsByCityAndSpecialtyAndInsurance("city", sID, iID));
        System.out.println("\n\nBeacons by roomID\n");
        System.out.println(dao.getBeaconsByRoom(rID));
        System.out.println("\n\nBeacons by hospitalID\n");
        System.out.println(dao.getBeaconsByHospital(hID));
        System.out.println("\n\nAceptedBySpeciaty by hospitalID\n");
        System.out.println(dao.getAcceptedBySpecialtyByHospital(hID));
        System.out.println("\n\nAddress by addressID\n");
        System.out.println(dao.getAddress(adID));
        System.out.println("\n\nIsurance by insuranceID\n");
        System.out.println(dao.getInsurance(iID));
        System.out.println("\n\nSpecialty by specialtyID\n");
        System.out.println(dao.getSpecialty(sID));
    }
    
    public void testDelete(){
        System.out.println("Delete Beacon = "+dao.deleteBeacon(bID));
        System.out.println("Delete Room = "+dao.deleteRoom(rID));
        System.out.println("Delete Visit = "+dao.deleteVisit(vID));
        System.out.println("Delete PatientMHub = "+dao.deletePatientMHub(pID));
        System.out.println("Delete AcceptedBySpecialty = "+dao.deleteAcceptedBySpecialty(acID));
        System.out.println("Delete Insurance = "+dao.deleteInsurance(iID));
        System.out.println("Delete Specialty = "+dao.deleteSpecialty(sID));
        System.out.println("Delete Hospital = "+dao.deleteHospital(hID));
        System.out.println("Delete Address = "+dao.deleteAddress(adID));
    }
}

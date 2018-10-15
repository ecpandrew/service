package br.pucrio.inf.lac.hospital.semantic.database;

import br.pucrio.inf.lac.hospital.semantic.data.AcceptedBySpecialty;
import br.pucrio.inf.lac.hospital.semantic.data.Address;
import br.pucrio.inf.lac.hospital.semantic.data.Beacon;
import br.pucrio.inf.lac.hospital.semantic.data.Hospital;
import br.pucrio.inf.lac.hospital.semantic.data.Insurance;
import br.pucrio.inf.lac.hospital.semantic.data.PatientMHub;
import br.pucrio.inf.lac.hospital.semantic.data.Room;
import br.pucrio.inf.lac.hospital.semantic.data.Specialty;
import br.pucrio.inf.lac.hospital.semantic.data.Visit;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SemanticDaoImpMariaDB implements SemanticDao{
    private Connection conn;
    
    public SemanticDaoImpMariaDB(){
        connectToDB();
    }
    
    public void connectToDB(){
        String url = "jdbc:mariadb://localhost/h4";
        Properties props = new Properties();
        props.setProperty("user","root");
        props.setProperty("password","semantic");
        props.setProperty("ssl","false");
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isConnectionActive(){
        if(conn == null)
            return false;
        try {
            return (!conn.isClosed());
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void disconnectToDB(){
        try {
            if(conn == null || conn.isClosed())
                return;
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Process a select sql and return a set of results
     * @param sql the full SELECT sql
     * @param nColumns the number of Columns selected
     * @return A LinkedList of String arrays where each array is a row result of
     * the sql and each array element is a column of that row. Return null in case of error
     */
    public LinkedList<String[]> processSelectQuery(String sql, int nColumns){
        LinkedList<String[]> result = new LinkedList<>();
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String[] columns = new String[nColumns];
                for(int i=0; i<nColumns; i++)
                    columns[i] = rs.getString(i);
                result.add(columns);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }
    
    /**
     * Process a select sql and return a set of results
     * @param sql the full SELECT sql
     * @param columnName a set of Strings were each string is the name of a column
     * to return
     * @return A LinkedList of Maps were each map is a row result of
     * the sql and each Map element has key=(column name) and
     * value = (the respective column value). Returns null in case of error.
     */
    public LinkedList<Map<String, String>> processSelectQuery(String sql, Set<String>columnName){
        LinkedList<Map<String, String>> result = new LinkedList<>();
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                HashMap<String, String> columnMap = new HashMap<>();
                for(String columnNamei : columnName)
                    columnMap.put(columnNamei, rs.getString(columnNamei));
                result.add(columnMap);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }
    
    /**
     * Process any sql that will update the database
     * You should use the processInsert() method to process a INSERT sql
     * @param sql the full UPDATE OR DELETE sql
     * @return return the number of rows that were updated or -1 in case of error
     */
    public int processUpdate(String sql){
        Statement st;
        int rowsUpdated = 0;
        try {
            st = conn.createStatement();
            rowsUpdated = st.executeUpdate(sql);
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return rowsUpdated;
    }
    
    /**
     * Process a INSERT sql and return the generated key
     * @param sql the full INSERT sql
     * @param keyColumnName the key's column name at the inserted table
     * @return an long integer with the generated key or -1 in case of error
     */
    public long processInsert(String sql, String keyColumnName){
        Statement st;
        long generatedKey = 0;
        try {
            st = conn.createStatement();
            st.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            generatedKey = rs.getLong(keyColumnName);
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(SemanticDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return (generatedKey==0)? -1 : generatedKey;
    }
    
    @Override
    public long insertHospital(Hospital h){
        String sql;
        sql = "INSERT INTO Hospital (hospitalName, addressID, "
                + "latitude, longitude) VALUES (\'"
                + h.getHospitalName()+"\', "
                + h.getAddressID()+", "
                + h.getLatitude()+", "
                + h.getLongitude()+")";
        return processInsert(sql, "hospitalID");
    }
    

    @Override
    public long insertBeacon(Beacon b) {
        String sql = "INSERT INTO Beacon (thingID, RoomID, "
                   + "active, lastBateryChange) "
                   + "VALUES (\'"
                   + b.getThingID()+"\', "
                   + b.getRoomID()+", ";
        sql += (b.isActive()) ?"TRUE, \'" :"FALSE, \'";
        sql+= b.getLastBateryChange()+"\')";
        return processInsert(sql, "beaconID");
    }

    @Override
    public long insertRoom(Room r) {
        String sql = "INSERT INTO Room (roomName, roomType, hospitalID) "
                   + "VALUES (\'"
                   + r.getRoomName()+"\', \'"
                   + r.getRoomType()+"\', "
                   + r.getHospitalID()+")";
        return processInsert(sql, "roomID");
    }

    @Override
    public long insertAcceptedBySpecialty(AcceptedBySpecialty a) {
        String sql = "INSERT INTO AcceptedBySpecialty(hospitalID, insuranceID, "
                + "specialtyID, `begin`, `end`) "
                + "VALUES ("+a.getHospitalID()+", "
                +a.getInsuranceID()+", "
                +a.getSpecialtyID()+", \'"
                +a.getBegin()+"\', \'"
                +a.getEnd()+"\')";
        return processInsert(sql, "acceptedBySpecialtyID");
    }

    @Override
    public long insertAddress(Address a) {
        String sql = "INSERT INTO Address (neighborhood, city, state, street, number";
        
        //Check if there is a zipcode
        sql += (a.getZipcode()!=null) ? ", zipcode" : "";
        
        //Check if there is addtional info
        sql += (a.getAdditionalInfo()!=null) ? ", additionalInfo) " : ") ";
        
        sql += "VALUES (\'"+a.getNeighborhood()+"\', \'"
             + a.getCity()+"\', \'"
             + a.getState()+"\', \'"
             + a.getStreet()+"\', "
             + a.getNumber();
        
        //Check if there is a zipcode
        sql += (a.getZipcode()!=null) ? ", \'"+a.getZipcode()+"\'" : "";
        
        //Check if there is addtional info
        sql += (a.getAdditionalInfo()!=null) ? ", \'"+a.getAdditionalInfo()+"\')" : ")";
        
        return processInsert(sql, "addressID");
    }
    
    @Override
    public long insertInsurance(Insurance i) {
        String sql = "INSERT INTO Insurance(insuranceName) "
                + "VALUES (\'"+i.getInsuranceName()+"\')";
        return processInsert(sql, "insutanceID");
    }

    @Override
    public long insertPatientMHub(PatientMHub p) {
        String sql = "INSERT INTO PatientMHub (mhubID, name, birth) "
                   + "VALUES (\'"+p.getMhubID()+"\', \'"
                   + p.getName()+"\', \'"
                   + p.getBirth()+"\')";
        return processInsert(sql, "patientMHubID");
    }

    @Override
    public long insertSpecialty(Specialty s) {
        String sql = "INSERT INTO Specialty (specialtyName) "
                   + "VALUES (\'"+s.getSpecialtyname()+"\')";
        
        return processInsert(sql, "specialtyID");
    }

    @Override
    public long insertVisit(Visit v) {
        String sql = "INSERT INTO Visit(patientMHubID, hospitalID, "
                   + "specialtyID, date, hospitalScore, "
                   + "specialityScore";
        
        //Check if there is a diagnostic
        sql += (v.getDiagnostic()!=null) ?", diagnostic" :"";
        
        //Check if there is are reported symptoms
        sql += (v.getReportedSymptoms()!=null) ?", reportedSymptoms) " : ") ";
        
        sql += "VALUES ("+v.getPatientMHubID()+", "
             + v.getHospitalID()+", "
             + v.getSpecialtyID()+", \'"
             + v.getDate()+"\', "
             + v.getHospitalScore()+", "
             + v.getSpecialtyScore();
        //Check if there is a diagnostic
        sql += (v.getDiagnostic()!=null) ? ", \'"+v.getDiagnostic()+"\'" : "";
        
        //Check if there is are reported symptoms
        sql += (v.getReportedSymptoms()!=null) ?", \'"+ v.getReportedSymptoms()+"\')" :")";
        return processInsert(sql, "visitID");
    }
    
    @Override
    public boolean deleteBeacon(long beaconID) {
        String sql = "DELETE FROM Beacon "
                   + "WHERE beaconID = \'"+beaconID+"\'";
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteHospital(long hospitalID) {
        String sql = "DELETE FROM Hospital "
                   + "WHERE hospitalID = "+hospitalID;
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteRoom(long roomID) {
        String sql = "DELETE FROM Room "
                   + "WHERE roomID = "+roomID;
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean deleteAcceptedBySpecialty(long acceptedBySpecialtyID) {
        String sql = "DELETE FROM AcceptedBySpecialty "
                   + "WHERE acceptedBySpecialtyID = \'"+acceptedBySpecialtyID+"\'";
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteAddress(long addressID) {
        String sql = "DELETE FROM Address "
                   + "WHERE addressID = \'"+addressID+"\'";
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean deleteInsurance(long insuranceID) {
        String sql = "DELETE FROM Insurance "
                   + "WHERE insuranceID = \'"+insuranceID+"\'";
        return (processUpdate(sql)>0);
    }
    
    
    @Override
    public boolean deletePatientMHub(long patientMHubID) {
        String sql = "DELETE FROM PatientMHub "
                   + "WHERE patientMHubID = "+patientMHubID;
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteSpecialty(long specialtyID) {
        String sql = "DELETE FROM Specialty "
                   + "WHERE specialtyID = \'"+specialtyID+"\'";
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteVisit(long visitID) {
        String sql = "DELETE FROM Visit "
                   + "WHERE visitID = \'"+visitID+"\'";
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updateBeacon(Beacon b) {
        String sql = "UPDATE Beacon "
                   + "SET thingID = \'"+b.getThingID()+"\', "
                   + "roomID = "+b.getRoomID()+", active = ";
        sql += (b.isActive()) ?"TRUE, " :"FALSE, ";
        sql+= "lastBateryChange = \'"+b.getLastBateryChange()+"\' "
            + "WHERE beaconID = "+b.getBeaconID();
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updateHospital(Hospital h) {
        String sql = "UPDATE Hospital "
                   + "SET hospitalName = \'"+h.getHospitalName()+"\', "
                   + "addressID = "+h.getAddressID()+", "
                   + "latitude = "+h.getLatitude()+", "
                   + "longitude = "+h.getLongitude()+" "
                   + "WHERE hospitalID = "+h.getHospitalID();
       
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updateRoom(Room r) {
        String sql = "UPDATE Room "
                   + "SET roomName = \'"+r.getRoomName()+"\', "
                   + "roomType = \'"+r.getRoomType()+"\', "
                   + "hospitalID = "+r.getHospitalID()+" "
                   + "WHERE roomID = "+r.getRoomID();
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updateAcceptedBySpecialty(AcceptedBySpecialty a) {
        String sql = "UPDATE AcceptedBySpecialty "
                   + "SET hospitalID = "+a.getHospitalID()+", "
                   + "insuranceID = "+a.getInsuranceID()+", "
                   + "specialtyID = "+a.getSpecialtyID()+", "
                   + "begin = \'"+a.getBegin()+"\', "
                   + "end = \'"+a.getEnd()+"\' "
                   + "WHERE acceptedBySpecialtyID = "+a.getAcceptedBySpecialtyID();
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updateAddress(Address a) {
        String sql = "UPDATE Address "
                  + "SET neighborhood = \'"+a.getNeighborhood()+"\', "
                  + "city = \'"+a.getCity()+"\', "
                  + "state = \'"+a.getState()+"\', "
                  + "street = \'"+a.getStreet()+"\', "
                  + "number = "+a.getNumber()+", "
                  + "zipcode = ";
        //Check if there is a zipcode
        sql += (a.getZipcode()!=null) ?"\'"+a.getZipcode()+"\', " :"NULL, ";
        
        sql += "additionalInfo = ";
        
        //Check if there is addtional info
        sql += (a.getAdditionalInfo()!=null) ?"\'"+a.getAdditionalInfo()+"\' " 
                                             :"NULL ";
        sql += "WHERE addressID = "+a.getAddressID();
        return (processUpdate(sql)>0);
    }
    @Override
    public boolean updateInsurance(Insurance i) {
        String sql = "UPDATE Insurance "
                   + "SET insuranceName = \'"+i.getInsuranceName()+"\' "
                   + "WHERE insuranceID = "+i.getInsuranceID();
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updatePatientMHub(PatientMHub p) {
        String sql = "UPDATE PatientMHub "
                   + "SET mhubID = \'"+p.getMhubID()+"\', "
                   + "name = \'"+p.getName()+"\', "
                   + "birth = \'"+p.getBirth()+"\' "
                   + "WHERE patientMHubID = "+p.getPatientMHubID();
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updateSpecialty(Specialty s) {
        String sql = "UPDATE Specialty "
                   + "SET specialtyName = \'"+s.getSpecialtyname()+"\' "
                   + "WHERE specialtyID = "+s.getSpecialtyID();
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updateVisit(Visit v) {
        String sql = "UPDATE Visit "
                   + "SET hospitalID = "+v.getHospitalID()+", "
                   + "specialtyID = "+v.getSpecialtyID()+", "
                   + "date = \'"+v.getDate()+"\', "
                   + "hospitalScore = "+v.getHospitalScore()+", "
                   + "specialityScore = "+v.getSpecialtyScore()+", "
                   + "diagnostic = ";
        //Check if there is a diagnostic
        sql += (v.getDiagnostic()!=null) ?"\'"+v.getDiagnostic()+"\', "
                                         :"NULL, ";
        //Check if there is are reported symptoms
        sql += "reportedSymptoms = ";
        sql += (v.getReportedSymptoms()!=null) ?"\'"+v.getReportedSymptoms()+"\' "
                                               :"NULL ";
        sql += "WHERE visitID = "+v.getVisitID();
        return (processUpdate(sql)>0);
    }

    @Override
    public Set<Beacon> getBeacons() {
        HashSet<Beacon> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Beacon";
        
        HashSet<String> columnName = new HashSet<>();
        columnName.add("beaconID");
        columnName.add("roomID");
        columnName.add("thingID");
        columnName.add("active");
        columnName.add("lastBateryChange");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        for (Map<String, String> resulti : selectResult) {
            Beacon b = new Beacon(Long.parseLong(resulti.get("beaconID")),
                    Long.parseLong(resulti.get("roomID")),
                    UUID.fromString(resulti.get("thingID")),
                    ("1".equals(resulti.get("active"))),
                    Date.valueOf(resulti.get("lastBateryChange")));
            resultSet.add(b);
        }
        return resultSet;
    }

    @Override
    public Set<Hospital> getHospitals() {
        HashSet<Hospital> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Hospital";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Hospital h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
            resultSet.add(h);
        }
        return resultSet;
    }

    @Override
    public Set<Room> getRooms() {
        HashSet<Room> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Room";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("roomID");
        columnName.add("roomName");
        columnName.add("roomType");
        columnName.add("hospitalID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Room r = new Room(Long.parseLong(resulti.get("roomID")),
                    resulti.get("roomName"),
                    resulti.get("roomType"),
                    Long.parseLong(resulti.get("hospitalID")));
            resultSet.add(r);
        }
        return resultSet;
    }

    @Override
    public Set<AcceptedBySpecialty> getAcceptedBySpecialties() {
        HashSet<AcceptedBySpecialty> resultSet = new HashSet<>();
        String sql = "SELECT * FROM AcceptedBySpecialty";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("acceptedBySpecialtyID");
        columnName.add("hospitalID");
        columnName.add("insuranceID");
        columnName.add("specialtyID");
        columnName.add("begin");
        columnName.add("end");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            AcceptedBySpecialty a = new AcceptedBySpecialty(Long.parseLong(resulti.get("acceptedBySpecialtyID")),
                    Long.parseLong(resulti.get("hospitalID")),
                    Long.parseLong(resulti.get("insuranceID")),
                    Long.parseLong(resulti.get("specialtyID")),
                    Date.valueOf(resulti.get("begin")),
                    Date.valueOf(resulti.get("end")));
            resultSet.add(a);
        }
        return resultSet;
    }

    @Override
    public Set<Address> getAddresses() {
        HashSet<Address> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Address";
        
        HashSet<String> columnName = new HashSet<>();
        columnName.add("addressID");
        columnName.add("neighborhood");
        columnName.add("city");
        columnName.add("state");
        columnName.add("street");
        columnName.add("number");
        columnName.add("zipcode");
        columnName.add("additionalInfo");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        for (Map<String, String> resulti : selectResult) {
            Address a = new Address(Long.parseLong(resulti.get("addressID")),
                    resulti.get("neighborhood"),
                    resulti.get("city"),
                    resulti.get("state"),
                    resulti.get("street"),
                    Integer.parseInt(resulti.get("number")),
                    resulti.get("zipcode"),
                    resulti.get("additionalInfo"));
            resultSet.add(a);
        }
        return resultSet;
    }
    
    @Override
    public Set<Insurance> getInsurances() {
        HashSet<Insurance> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Insurance";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("insuranceID");
        columnName.add("insuranceName");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Insurance i = new Insurance(Long.parseLong(resulti.get("insuranceID")),
                    resulti.get("insuranceName"));
            resultSet.add(i);
        }
        return resultSet;
    }

    @Override
    public Set<PatientMHub> getPatientMHubs() {
        HashSet<PatientMHub> resultSet = new HashSet<>();
        String sql = "SELECT * FROM PatientMHub";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("patientMHubID");
        columnName.add("mhubID");
        columnName.add("name");
        columnName.add("birth");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            PatientMHub p = new PatientMHub(Long.parseLong(resulti.get("patientMHubID")),
                    UUID.fromString(resulti.get("mhubID")),
                    resulti.get("name"),
                    Date.valueOf(resulti.get("birth")));
            resultSet.add(p);
        }
        return resultSet;
    }

    @Override
    public Set<Specialty> getSpecialties() {
        HashSet<Specialty> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Specialty";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("specialtyID");
        columnName.add("specialtyName");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Specialty s = new Specialty(Long.parseLong(resulti.get("specialtyID")),
                    resulti.get("specialtyName"));
            resultSet.add(s);
        }
        return resultSet;
    }

    @Override
    public Set<Visit> getVisits() {
        HashSet<Visit> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Visit";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("visitID");
        columnName.add("patientMHubID");
        columnName.add("hospitalID");
        columnName.add("specialtyID");
        columnName.add("date");
        columnName.add("hospitalScore");
        columnName.add("specialityScore");
        columnName.add("diagnostic");
        columnName.add("reportedSymptoms");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Visit v = new Visit(Long.parseLong(resulti.get("visitID")),
                    Long.parseLong(resulti.get("patientMHubID")),
                    Long.parseLong(resulti.get("hospitalID")),
                    Long.parseLong(resulti.get("specialtyID")),
                    Date.valueOf(resulti.get("date")),
                    Byte.parseByte(resulti.get("hospitalScore")),
                    Byte.parseByte(resulti.get("specialityScore")),
                    resulti.get("diagnostic"),
                    resulti.get("reportedSymptoms"));
            resultSet.add(v);
        }
        return resultSet;
    }

    @Override
    public Set<Hospital> getHospitalsByCity(String city) {
        HashSet<Hospital> resultSet = new HashSet<>();
        String sql = "SELECT h.hospitalID, h.hospitalName, h.addressID, "
                   + "h.latitude, h.longitude "
                   + "FROM Hospital h JOIN Address a on h.addressID = a.addressID " 
                   + "WHERE a.city = \'"+city+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Hospital h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
            resultSet.add(h);
        }
        return resultSet;
    }

    @Override
    public Hospital getHospital(long hospitalID) {
        String sql = "SELECT * FROM Hospital "
                   + "WHERE hospitalID = "+hospitalID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Hospital h = null;
        for (Map<String, String> resulti : selectResult) {
            h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
        }
        return h;
    }
    
    @Override
    public Set<Hospital> getHospitalsByCityAndSpecialty(String city, long specialtyID) {
        HashSet<Hospital> resultSet = new HashSet<>();
        String sql = "SELECT h.hospitalID, h.hospitalName, h.addressID, "
                + "h.latitude, h.longitude " 
                + "FROM Hospital h JOIN Address ad ON h.addressID = ad.addressID "
                + "JOIN AcceptedBySpecialty ac ON h.hospitalID = ac.hospitalID "
                + "WHERE ad.city = \'"+city+"\' "
                + "AND ac.specialtyID = "+specialtyID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Hospital h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
            resultSet.add(h);
        }
        return resultSet;
    }
    
    @Override
    public Set<Hospital> getHospitalsByCityAndInsurance(String city, long insuranceID) {
        HashSet<Hospital> resultSet = new HashSet<>();
        String sql = "SELECT h.hospitalID, h.hospitalName, h.addressID, "
                + "h.latitude, h.longitude " 
                + "FROM Hospital h JOIN Address ad ON h.addressID = ad.addressID "
                + "JOIN AcceptedBySpecialty ac ON h.hospitalID = ac.hospitalID "
                + "WHERE ad.city = \'"+city+"\' "
                + "AND ac.insuranceID = "+insuranceID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Hospital h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
            resultSet.add(h);
        }
        return resultSet;
    }

    @Override
    public Set<Hospital> getHospitalsByCityAndSpecialtyAndInsurance(String city, long specialtyID, long insuranceID) {
        HashSet<Hospital> resultSet = new HashSet<>();
        String sql = "SELECT h.hospitalID, h.hospitalName, h.addressID, "
                + "h.latitude, h.longitude " 
                + "FROM Hospital h JOIN Address ad ON h.addressID = ad.addressID "
                + "JOIN AcceptedBySpecialty ac ON h.hospitalID = ac.hospitalID "
                + "WHERE ad.city = \'"+city+"\' "
                + "AND ac.specialtyID = "+specialtyID+" "
                + "AND ac.insuranceID = "+insuranceID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hospitalID");
        columnName.add("hospitalName");
        columnName.add("addressID");
        columnName.add("latitude");
        columnName.add("longitude");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Hospital h = new Hospital(Long.parseLong(resulti.get("hospitalID")),
                    resulti.get("hospitalName"),
                    Long.parseLong(resulti.get("addressID")),
                    Double.parseDouble(resulti.get("latitude")),
                    Double.parseDouble(resulti.get("longitude")));
            resultSet.add(h);
        }
        return resultSet;
    }

    @Override
    public Set<Beacon> getBeaconsByRoom(long roomID){
        Set<Beacon> resultSet = new HashSet<>();
        //Get the beacons information from the database
        String sql = "SELECT beaconID, thingID, active, lastBateryChange "
                   + "FROM Beacon "
                   + "WHERE roomID = "+roomID;
        HashSet<String> columnName = new HashSet<>();
        columnName.add("beaconID");
        columnName.add("thingID");
        columnName.add("active");
        columnName.add("lastBateryChange");
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        //Add the beacons to the resultSet
        for(Map<String, String> resulti : selectResult){
            Beacon b = new Beacon(Long.parseLong(resulti.get("beaconID")),
                                  roomID,
                                  UUID.fromString(resulti.get("thingID")),
                                  Boolean.parseBoolean(resulti.get("active")),
                                  Date.valueOf(resulti.get("lastBateryChange")));
            resultSet.add(b);
        }
        
        return resultSet;
    }
    
    @Override
    public Set<Beacon> getBeaconsByHospital(long hospitalID){
        Set<Beacon> resultSet = new HashSet<>();
        //Get the beacons information from the database
        String sql = "SELECT * " 
                   + "FROM Beacon b JOIN Room r ON b.roomID = r.roomID " 
                   + "WHERE r.hospitalID = "+hospitalID;
        HashSet<String> columnName = new HashSet<>();
        columnName.add("beaconID");
        columnName.add("roomID");
        columnName.add("thingID");
        columnName.add("active");
        columnName.add("lastBateryChange");
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        //Add the beacons to the resultSet
        for(Map<String, String> resulti : selectResult){
            Beacon b = new Beacon(Long.parseLong(resulti.get("beaconID")),
                                  Long.parseLong(resulti.get("roomID")),
                                  UUID.fromString(resulti.get("thingID")),
                                  Boolean.parseBoolean(resulti.get("active")),
                                  Date.valueOf(resulti.get("lastBateryChange")));
            resultSet.add(b);
        }
        
        return resultSet;
    }

    @Override
    public Set<AcceptedBySpecialty> getAcceptedBySpecialtyByHospital(long hospitalID) {
        HashSet<AcceptedBySpecialty> resultSet = new HashSet<>();
        String sql = "SELECT * FROM AcceptedBySpecialty "
                   + "WHERE hospitalID = "+hospitalID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("acceptedBySpecialtyID");
        columnName.add("hospitalID");
        columnName.add("insuranceID");
        columnName.add("specialtyID");
        columnName.add("begin");
        columnName.add("end");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            AcceptedBySpecialty a = new AcceptedBySpecialty(Long.parseLong(resulti.get("acceptedBySpecialtyID")),
                    Long.parseLong(resulti.get("hospitalID")),
                    Long.parseLong(resulti.get("insuranceID")),
                    Long.parseLong(resulti.get("specialtyID")),
                    Date.valueOf(resulti.get("begin")),
                    Date.valueOf(resulti.get("end")));
            resultSet.add(a);
        }
        return resultSet;
    }

    @Override
    public Insurance getInsurance(long insuranceID) {
        String sql = "SELECT * FROM Insurance "
                   + "WHERE insuranceID = "+insuranceID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("insuranceID");
        columnName.add("insuranceName");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        Insurance i = null;
        for (Map<String, String> resulti : selectResult) {
            i = new Insurance(Long.parseLong(resulti.get("insuranceID")),
                    resulti.get("insuranceName"));
        }
        return i;
    }

    @Override
    public Specialty getSpecialty(long specialtyID) {
        String sql = "SELECT * FROM Specialty "
                   + "WHERE specialtyID = "+specialtyID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("specialtyID");
        columnName.add("specialtyName");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Specialty s = null;
        for (Map<String, String> resulti : selectResult) {
            s = new Specialty(Long.parseLong(resulti.get("specialtyID")),
                    resulti.get("specialtyName"));
        }
        return s;
    }

    @Override
    public Address getAddress(long addressID) {
        String sql = "SELECT * FROM Address "
                   + "WHERE addressID = "+addressID;
        
        HashSet<String> columnName = new HashSet<>();
        columnName.add("addressID");
        columnName.add("neighborhood");
        columnName.add("city");
        columnName.add("state");
        columnName.add("street");
        columnName.add("number");
        columnName.add("zipcode");
        columnName.add("additionalInfo");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        Address a = null;
        for (Map<String, String> resulti : selectResult) {
            a = new Address(Long.parseLong(resulti.get("addressID")),
                    resulti.get("neighborhood"),
                    resulti.get("city"),
                    resulti.get("state"),
                    resulti.get("street"),
                    Integer.parseInt(resulti.get("number")),
                    resulti.get("zipcode"),
                    resulti.get("additionalInfo"));
        }
        return a;
    }
}



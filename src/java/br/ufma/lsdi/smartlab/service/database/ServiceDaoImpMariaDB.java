package br.ufma.lsdi.smartlab.service.database;

import br.ufma.lsdi.smartlab.service.data.Beacon;
import br.ufma.lsdi.smartlab.service.data.Device;
import br.ufma.lsdi.smartlab.service.data.HasA;
import br.ufma.lsdi.smartlab.service.data.MHub;
import br.ufma.lsdi.smartlab.service.data.Person;
import br.ufma.lsdi.smartlab.service.data.PhysicalSpace;
import br.ufma.lsdi.smartlab.service.data.Thing;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
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
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceDaoImpMariaDB implements ServiceDao{
    private Connection conn;
    private final String semantic = "http://smartlab.lsdi.ufma.br/semantic/api/";
    
    public ServiceDaoImpMariaDB(){
        //connectToDB();
    }
    
    public void connectToDB(){
        String url = "jdbc:mariadb://localhost/service";
        Properties props = new Properties();
        props.setProperty("user","semantic");
        props.setProperty("password","semantic");
        props.setProperty("ssl","false");
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isConnectionActive(){
        if(conn == null)
            return false;
        try {
            return (!conn.isClosed());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void disconnectToDB(){
        try {
            if(conn == null || conn.isClosed())
                return;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
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
                for(String columnNamei : columnName){
                    String s = rs.getString(columnNamei);
                    if (rs.wasNull()) {
                        s = "";
                    }
                    columnMap.put(columnNamei, s);
                }
                result.add(columnMap);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return (generatedKey==0)? -1 : generatedKey;
    }
    
    @Override
    public long insertBeacon(Beacon b){
        String sql;
        sql = "INSERT INTO Beacon (thingID, active) "
                + "VALUES (\'"
                + b.getThingID()+"\', ";
        sql += (b.isActive()) ?"TRUE)" :"FALSE)";
        return processInsert(sql, "thingID");
    }

    @Override
    public long insertDevice(Device d) {
        String sql = "INSERT INTO Device(deviceID, manufacturer, "
                + "model, mhubID, thingID) VALUES ("
                +d.getDeviceID()+", \'"
                +d.getManufacturer()+"\', \'"
                +d.getModel()+"\', \'"
                +d.getMHubID()+"\', \'"
                +d.getThingID()+"\')";
        return processInsert(sql, "deviceID");
    }

    @Override
    public long insertHasA(HasA h) {
        String sql = "INSERT INTO HasA (hasAID, deviceID, "
                + "personID, roomID) VALUES ("
                +h.getHasAID()+", "
                +h.getDeviceID()+", "
                +h.getPersonID()+", "
                +h.getRoomID()+")";
        return processInsert(sql, "hasAID");
    }
    
    @Override
    public long insertMHub(MHub m) {
        String sql = "INSERT INTO MHub(mhubID) "
                + "VALUES (\'"+m.getMHubID()+"\')";
        return processInsert(sql, "mhubID");
    }

    @Override
    public long insertPerson(Person p) {
        String sql = "INSERT INTO Person (personID, personName, personEmail) "
                   + "VALUES ("+p.getPersonID()+", \'"
                   + p.getShortName()+"\', \'"
                   + p.getPersonEmail()+"\')";
        return processInsert(sql, "personID");
    }

    @Override
    public long insertPhysicalSpace(PhysicalSpace r) {
        String sql = "INSERT INTO Room (roomID, roomName, sectionID) "
                   + "VALUES ("+r.getRoomID()+", \'"
                   + r.getRoomName()+"\', "
                   + r.getSectionID()+")";
        return processInsert(sql, "roomID");
    }
    
    @Override
    public boolean deleteBeacon(UUID thingID) {
        String sql = "DELETE FROM Beacon "
                   + "WHERE beaconID = \'"+thingID+"\'";
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteBuilding(long buildingID) {
        String sql = "DELETE FROM Building "
                   + "WHERE buildingID = "+buildingID;
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteCity(long cityID) {
        String sql = "DELETE FROM City "
                   + "WHERE cityID = "+cityID;
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean deleteDevice(long deviceID) {
        String sql = "DELETE FROM Device "
                   + "WHERE deviceID = "+deviceID;
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deleteHasA(long hasAID) {
        String sql = "DELETE FROM HasA "
                   + "WHERE hasAID = "+hasAID;
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean deleteMHub(UUID mhubID) {
        String sql = "DELETE FROM MHub "
                   + "WHERE mhubID = \'"+mhubID+"\'";
        return (processUpdate(sql)>0);
    }
    
    
    @Override
    public boolean deletePerson(long personID) {
        String sql = "DELETE FROM Person "
                   + "WHERE personID = "+personID;
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean deletePhysicalSpace(long roomID) {
        String sql = "DELETE FROM Room "
                   + "WHERE roomID = "+roomID;
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updateBeacon(Beacon b) {
        String sql = "UPDATE Beacon "
                   + "SET active = ";
        sql += (b.isActive()) ?"TRUE " :"FALSE ";
        sql += "WHERE thingID = "+b.getThingID();
        return (processUpdate(sql)>0);
    }
    
    @Override
    public boolean updateDevice(Device d) {
        String sql = "UPDATE Device "
                   + "SET manufacturer = \'"+d.getManufacturer()+"\', "
                   + "model = \'"+d.getModel()+"\', "
                   + "mhubID = \'"+d.getMHubID()+"\', "
                   + "thingID = \'"+d.getThingID()+"\' "
                   + "WHERE deviceID = "+d.getDeviceID();
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updateHasA(HasA h) {
        String sql = "UPDATE HasA "
                   + "SET deviceID = "+h.getDeviceID()+", "
                   + "personID = "+h.getPersonID()+", "
                   + "roomID = "+h.getRoomID()+" "
                   + "WHERE hasAID = "+h.getHasAID();
        return (processUpdate(sql)>0);
    }
    /*
    @Override
    public boolean updateMHub(MHub m) {
        String sql = "UPDATE MHub "
                   + "SET insuranceName = \'"+i.getInsuranceName()+"\' "
                   + "WHERE mhubID = "+m.getMHubID();
        return (processUpdate(sql)>0);
    }*/
    
    @Override
    public boolean updatePerson(Person p) {
        String sql = "UPDATE Person "
                   + "SET name = \'"+p.getShortName()+"\', "
                   + "email = \'"+p.getPersonEmail()+"\' "
                   + "WHERE persomID = "+p.getPersonID();
        return (processUpdate(sql)>0);
    }

    @Override
    public boolean updatePhysicalSpace(PhysicalSpace r) {
        String sql = "UPDATE Room "
                   + "SET roomName = \'"+r.getRoomName()+"\' "
                   + "WHERE roomID = "+r.getRoomID();
        return (processUpdate(sql)>0);
    }

    @Override
    public Set<Beacon> getBeacons() {
        HashSet<Beacon> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Beacon";
        
        HashSet<String> columnName = new HashSet<>();
        columnName.add("thingID");
        columnName.add("active");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        for (Map<String, String> resulti : selectResult) {
            Beacon b = new Beacon(UUID.fromString(resulti.get("thingID")),
                    ("1".equals(resulti.get("active"))));
            resultSet.add(b);
        }
        return resultSet;
    }

    @Override
    public Set<Device> getDevices() {
        HashSet<Device> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Device";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("deviceID");
        columnName.add("manufacturer");
        columnName.add("model");
        columnName.add("mhubID");
        columnName.add("thingID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Device d = new Device(Long.parseLong(resulti.get("deviceID")),
                    resulti.get("manufacturer"),
                    resulti.get("model"),
                    UUID.fromString(resulti.get("mhubID")),
                    UUID.fromString(resulti.get("thingID")));
            resultSet.add(d);
        }
        return resultSet;
    }

    @Override
    public Set<HasA> getHasAs() {
        HashSet<HasA> resultSet = new HashSet<>();
        String sql = "SELECT * FROM HasA";
        
        HashSet<String> columnName = new HashSet<>();
        columnName.add("hasAID");
        columnName.add("deviceID");
        columnName.add("personID");
        columnName.add("roomID");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        for (Map<String, String> resulti : selectResult) {
            HasA h = new HasA(Long.parseLong(resulti.get("hasAID")),
                    Long.parseLong(resulti.get("deviceID")),
                    Long.parseLong(resulti.get("personID")),
                    Long.parseLong(resulti.get("roomID")));
            resultSet.add(h);
        }
        return resultSet;
    }
    
    @Override
    public Set<MHub> getMHubs() {
        HashSet<MHub> resultSet = new HashSet<>();
        String sql = "SELECT * FROM MHub";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("mhubID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            MHub m = new MHub(UUID.fromString(resulti.get("mhubID")), "");
            resultSet.add(m);
        }
        return resultSet;
    }

    @Override
    public Set<Person> getPersons() {
        HashSet<Person> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Person";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("personID");
        columnName.add("personName");
        columnName.add("personEmail");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            Person p = new Person(Long.parseLong(resulti.get("personID")),
                    resulti.get("personName"),
                    resulti.get("personEmail"));
            resultSet.add(p);
        }
        return resultSet;
    }

    @Override
    public Set<PhysicalSpace> getPhysicalSpaces() {
        HashSet<PhysicalSpace> resultSet = new HashSet<>();
        String sql = "SELECT * FROM Room";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("roomID");
        columnName.add("roomName");
        columnName.add("sectionID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);

        for (Map<String, String> resulti : selectResult) {
            PhysicalSpace r = new PhysicalSpace(Long.parseLong(resulti.get("roomID")),
                    resulti.get("roomName"),
                    Long.parseLong(resulti.get("sectionID")));
            resultSet.add(r);
        }
        return resultSet;
    }
    
    @Override
    public PhysicalSpace getPhysicalSpace(long roomID) {
        String url;
        String returnedJson;
        JSONObject data = null;
        PhysicalSpace r = null;
        
        url = semantic+ "physical_spaces/"+roomID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //text = new JSONObject(returnedJson);

                r = new PhysicalSpace(roomID,
                        data.getString("name"),
                        data.getString("description"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
    @Override
    public PhysicalSpace getPhysicalSpaceByName(String room) {
        String sql = "SELECT * FROM Room "
                   + "WHERE roomName = \'"+room+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("roomID");
        columnName.add("roomName");
        columnName.add("sectionID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        PhysicalSpace r = null;
        for (Map<String, String> resulti : selectResult) {
            r = new PhysicalSpace(Long.parseLong(resulti.get("roomID")),
                    resulti.get("roomName"),
                    Long.parseLong(resulti.get("sectionID")));
        }
        return r;
    }
    
    @Override
    public Set<Device> getThingsByRoom(long roomID){
        Set<Device> resultSet = new HashSet<>();
        
        String url;
        String returnedJson;
        JSONArray data;
        PhysicalSpace r = null;
        
        url = semantic+ "physical_spaces/"+roomID+"/descendant/things";
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONArray(returnedJson);
        
            for(int i = 0; i < data.length(); i++){
                JSONObject text = data.getJSONObject(i);

                Device d = new Thing(UUID.fromString(text.getString("uuid")),
                                  text.getString("description"));
                resultSet.add(d);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
    
    @Override
    public Set<Device> getMHubsByRoom(long roomID){
        Set<Device> resultSet = new HashSet<>();
        
        String url;
        String returnedJson;
        JSONArray data;
        PhysicalSpace r = null;
        
        url = semantic+ "physical_spaces/"+roomID+"/descendant/mhubs";
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONArray(returnedJson);
        
            for(int i = 0; i < data.length(); i++){
                JSONObject text = data.getJSONObject(i);

                Device d = new MHub(UUID.fromString(text.getString("uuid")),
                                  text.getString("description"));
                resultSet.add(d);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
    
    @Override
    public Set<Device> getThingsByPerson(long personID){
        Set<Device> resultSet = new HashSet<>();
        
        String url;
        String returnedJson;
        JSONArray data;
        PhysicalSpace r = null;
        
        url = semantic+ "persons/"+personID+"/things";
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONArray(returnedJson);
        
            if(data.length() != 0){
                JSONObject text = data.getJSONObject(0);

                Device d = new Thing(UUID.fromString(text.getString("uuid")),
                                  text.getString("description"));
                resultSet.add(d);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
    
    @Override
    public Set<Device> getMHubsByPerson(long personID){
        Set<Device> resultSet = new HashSet<>();
        
        String url;
        String returnedJson;
        JSONArray data;
        PhysicalSpace r = null;
        
        url = semantic+ "persons/"+personID+"/mhubs";
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONArray(returnedJson);
        
            if(data.length() != 0){
                JSONObject text = data.getJSONObject(0);

                Device d = new MHub(UUID.fromString(text.getString("uuid")),
                                  text.getString("description"));
                resultSet.add(d);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
    
    @Override
    public HasA getHasAByDevice(long deviceID){
        String sql = "SELECT * FROM HasA "
                   + "WHERE deviceID = "+deviceID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("hasAID");
        columnName.add("deviceID");
        columnName.add("personID");
        columnName.add("roomID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        HasA h = null;
        for (Map<String, String> resulti : selectResult) {
            h = new HasA(Long.parseLong(resulti.get("hasAID")),
                    deviceID,
                    resulti.get("personID").isEmpty()?0:Long.parseLong(resulti.get("personID")),
                    resulti.get("roomID").isEmpty()?0:Long.parseLong(resulti.get("roomID")));
        }
        return h;
    }
    
    @Override
    public Set<HasA> getHasAByPerson(long personID){
        Set<HasA> resultSet = new HashSet<>();
        String sql = "SELECT * FROM HasA "
                   + "WHERE personID = "+personID;
        HashSet<String> columnName = new HashSet<>();
        columnName.add("hasAID");
        columnName.add("deviceID");
        columnName.add("personID");
        columnName.add("roomID");
        
        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        
        //Add the beacons to the resultSet
        for(Map<String, String> resulti : selectResult){
            //if(resulti.get("personID").isEmpty())
            HasA h = new HasA(Long.parseLong(resulti.get("hasAID")),
                                  Long.parseLong(resulti.get("deviceID")),
                                  personID,
                                  0);
            resultSet.add(h);
        }
        
        return resultSet;
    }
    
    @Override
    public Device getDevice(long deviceID){
        String sql = "SELECT * FROM Device "
                   + "WHERE deviceID = "+deviceID;

        HashSet<String> columnName = new HashSet<>();
        columnName.add("deviceID");
        columnName.add("manufacturer");
        columnName.add("model");
        columnName.add("mhubID");
        columnName.add("thingID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Device d = null;
        for (Map<String, String> resulti : selectResult) {
            d = new Device(Long.parseLong(resulti.get("deviceID")),
                    resulti.get("manufacturer"),
                    resulti.get("model"),
                    resulti.get("mhubID").isEmpty()?null:UUID.fromString(resulti.get("mhubID")),
                    resulti.get("thingID").isEmpty()?null:UUID.fromString(resulti.get("thingID")));
        }
        return d;
    }
    
    @Override
    public Device getDeviceByMHub(UUID mhubID){
        String sql = "SELECT * FROM Device "
                   + "WHERE mhubID = \'"+mhubID+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("deviceID");
        columnName.add("manufacturer");
        columnName.add("model");
        columnName.add("mhubID");
        columnName.add("thingID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Device d = null;
        for (Map<String, String> resulti : selectResult) {
            d = new Device(Long.parseLong(resulti.get("deviceID")),
                    resulti.get("manufacturer"),
                    resulti.get("model"),
                    resulti.get("mhubID").isEmpty()?null:UUID.fromString(resulti.get("mhubID")),
                    resulti.get("thingID").isEmpty()?null:UUID.fromString(resulti.get("thingID")));
        }
        return d;
    }
    
    @Override
    public Device getDeviceByThing(UUID thingID){
        String sql = "SELECT * FROM Device "
                   + "WHERE thingID = \'"+thingID+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("deviceID");
        columnName.add("manufacturer");
        columnName.add("model");
        columnName.add("mhubID");
        columnName.add("thingID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Device d = null;
        for (Map<String, String> resulti : selectResult) {
            d = new Device(Long.parseLong(resulti.get("deviceID")),
                    resulti.get("manufacturer"),
                    resulti.get("model"),
                    resulti.get("mhubID").isEmpty()?null:UUID.fromString(resulti.get("mhubID")),
                    resulti.get("thingID").isEmpty()?null:UUID.fromString(resulti.get("thingID")));
        }
        return d;
    }
    
    @Override
    public Beacon getBeacon(UUID thingID){
        String sql = "SELECT * FROM Beacon "
                   + "WHERE thingID = \'"+thingID+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("thingID");
        columnName.add("active");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Beacon b = null;
        for (Map<String, String> resulti : selectResult) {
            b = new Beacon(UUID.fromString(resulti.get("thingID")),
                    ("1".equals(resulti.get("active"))));
        }
        return b;
    }
    
    @Override
    public MHub getMHub(UUID mhubID){
        String sql = "SELECT * FROM MHub "
                   + "WHERE mhubID = \'"+mhubID+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("mhubID");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        MHub m = null;
        for (Map<String, String> resulti : selectResult) {
            m = new MHub(UUID.fromString(resulti.get("mhubID")), "");
        }
        return m;
    }
    
    @Override
    public Person getPerson(long personID){
        String url;
        String returnedJson;
        JSONObject data;
        Person p = null;
        
        url = semantic+ "persons/"+personID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //JSONObject text = data.getJSONObject(0);

                p = new Person(personID,
                        data.getString("shortName"),
                        data.getString("email"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Override
    public Person getPerson_(long personID) {
        return null;
    }

    @Override
    public Person getPersonByEmail(String personEmail){
        String sql = "SELECT * FROM Person "
                   + "WHERE personEmail = \'"+personEmail+"\'";

        HashSet<String> columnName = new HashSet<>();
        columnName.add("personID");
        columnName.add("personName");
        columnName.add("personEmail");

        LinkedList<Map<String, String>> selectResult = processSelectQuery(sql, columnName);
        Person p = null;
        for (Map<String, String> resulti : selectResult) {
            p = new Person(Long.parseLong(resulti.get("personID")),
                    resulti.get("personName"),
                    personEmail);
        }
        return p;
    }
    
    @Override
    public Person getPersonByMHub(UUID mhubID) {
        String url;
        String returnedJson;
        JSONObject data;
        Person p = null;
        
        url = semantic+ "mhubs/"+mhubID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //JSONObject text = data.getJSONObject(0);
                //JSONArray data2 = data.getJSONArray("holder");
                JSONObject text2 = data.getJSONObject("holder");
                
                url = semantic+"persons/"+text2.getLong("id");
                
                returnedJson = REST.sendGet(url, "GET");
                if(returnedJson == null) return null;
                data = new JSONObject(returnedJson);
                if(data.length() != 0){
                //text = data.getJSONObject(0);
                
                p = new Person(text2.getLong("id"),
                        data.getString("shortName"),
                        data.getString("email"));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    @Override
    public Person getPersonByThing(UUID thingID) {
        String url;
        String returnedJson;
        JSONObject data;
        Person p = null;
        
        url = semantic+ "things/"+thingID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //JSONObject text = data.getJSONObject(0);
                //JSONArray data2 = text.getJSONArray("holder");
                JSONObject text2 = data.getJSONObject("holder");
                
                url = semantic+"persons/"+text2.getLong("id");
                
                returnedJson = REST.sendGet(url, "GET");
                if(returnedJson == null) return null;
                data = new JSONObject(returnedJson);
                if(data.length() != 0){
                //text = data.getJSONObject(0);
                
                p = new Person(text2.getLong("id"),
                        data.getString("shortName"),
                        data.getString("email"));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    @Override
    public PhysicalSpace getPhysicalSpaceByMHub(UUID mhubID) {
        String url;
        String returnedJson;
        JSONObject data;
        PhysicalSpace r = null;
        
        url = semantic+ "mhubs/"+mhubID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //JSONObject text = data.getJSONObject(0);
                //JSONArray data2 = text.getJSONArray("holder");
                JSONObject text2 = data.getJSONObject("holder");
                
                url = semantic+"physical_spaces/"+text2.getLong("id");
                
                returnedJson = REST.sendGet(url, "GET");
                if(returnedJson == null) return null;
                data = new JSONObject(returnedJson);
                if(data.length() != 0){
                //text = data.getJSONObject(0);
                
                String desc = "";
                if(!data.isNull("description")){
                    desc = data.getString("description");
                }
                
                r = new PhysicalSpace(text2.getLong("id"),
                        data.getString("name"),
                        desc);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
    @Override
    public PhysicalSpace getPhysicalSpaceByThing(UUID thingID) {
        String url;
        String returnedJson;
        JSONObject data;
        PhysicalSpace r = null;
        
        url = semantic+ "things/"+thingID;
        try {
            returnedJson = REST.sendGet(url, "GET");
            if(returnedJson == null) return null;
            data = new JSONObject(returnedJson);
        
            if(data.length() != 0){
                //JSONObject text = data.getJSONObject(0);
                //JSONArray data2 = text.getJSONArray("holder");
                JSONObject text2 = data.getJSONObject("holder");
                
                url = semantic+"physical_spaces/"+text2.getLong("id");
                
                returnedJson = REST.sendGet(url, "GET");
                if(returnedJson == null) return null;
                data = new JSONObject(returnedJson);
                if(data.length() != 0){
                //text = data.getJSONObject(0);
                
                String desc = "";
                if(!data.isNull("description")){
                    desc = data.getString("description");
                }
                
                r = new PhysicalSpace(text2.getLong("id"),
                        data.getString("name"),
                        desc);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
    public static String convert(String s){
        String r = null;
        try{
            r = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        }catch(UnsupportedEncodingException ex){
            Logger.getLogger(ServiceDaoImpMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}



package br.ufma.lsdi.smartlab.service.database;

import br.ufma.lsdi.smartlab.service.data.Device;
import br.ufma.lsdi.smartlab.service.data.MHub;
import br.ufma.lsdi.smartlab.service.data.Person;
import br.ufma.lsdi.smartlab.service.data.PhysicalSpace;
import br.ufma.lsdi.smartlab.service.data.Thing;
import br.ufma.lsdi.smartlab.service.data.Beacon;
import br.ufma.lsdi.smartlab.service.data.HasA;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceDaoImpSemantic implements ServiceDao{
    private Connection conn;
    private final String semantic = "http://smartlab.lsdi.ufma.br/semantic/api/";
    
    public ServiceDaoImpSemantic(){
        //connectToDB();
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Override
    public Person getPerson_(long personID) {
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
                        data.getString("email"),
                        data.getString("roles"));
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceDaoImpSemantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    @Override
    public long insertBeacon(Beacon b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertDevice(Device d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertHasA(HasA h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertMHub(MHub m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertPerson(Person p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertPhysicalSpace(PhysicalSpace r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteBeacon(UUID thingID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteBuilding(long buildingID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteCity(long cityID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteDevice(long deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteHasA(long hasAID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteMHub(UUID mhubID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deletePerson(long personID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deletePhysicalSpace(long roomID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateBeacon(Beacon b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateDevice(Device d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateHasA(HasA h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updatePerson(Person p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updatePhysicalSpace(PhysicalSpace r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Beacon> getBeacons() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Device> getDevices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<HasA> getHasAs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<MHub> getMHubs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Person> getPersons() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<PhysicalSpace> getPhysicalSpaces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhysicalSpace getPhysicalSpaceByName(String room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HasA getHasAByDevice(long deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<HasA> getHasAByPerson(long personID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Device getDevice(long deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Device getDeviceByMHub(UUID mhubID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Device getDeviceByThing(UUID thingID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Beacon getBeacon(UUID thingID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MHub getMHub(UUID mhubID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Person getPersonByEmail(String personEmail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}



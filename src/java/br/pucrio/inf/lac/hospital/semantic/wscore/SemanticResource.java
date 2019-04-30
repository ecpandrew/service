package br.pucrio.inf.lac.hospital.semantic.wscore;

import br.pucrio.inf.lac.hospital.horys.protocol.HORYSProtocol;
import br.pucrio.inf.lac.hospital.semantic.data.Beacon;
import br.pucrio.inf.lac.hospital.semantic.data.Building;
import br.pucrio.inf.lac.hospital.semantic.data.City;
import br.pucrio.inf.lac.hospital.semantic.data.Device;
import br.pucrio.inf.lac.hospital.semantic.data.GroupRendezvous;
import br.pucrio.inf.lac.hospital.semantic.data.HasA;
import br.pucrio.inf.lac.hospital.semantic.data.MHub;
import br.pucrio.inf.lac.hospital.semantic.data.Person;
import br.pucrio.inf.lac.hospital.semantic.data.Rendezvous;
import br.pucrio.inf.lac.hospital.semantic.data.Room;
import br.pucrio.inf.lac.hospital.semantic.data.Section;
import br.pucrio.inf.lac.hospital.semantic.database.SemanticDao;
import br.pucrio.inf.lac.hospital.semantic.database.SemanticDaoImpMariaDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 * REST Web Service
 *
 */
@Path("get")
public class SemanticResource {

    private static SemanticDao dao = new SemanticDaoImpMariaDB();
    
    private ObjectMapper mapper;
    
    /** The  Horys restport. */
    private final String HORYS = "http://localhost:7981";
    private final String sHORYS = "http://localhost:8080/service/webresources/simulatedhoriz";
    
    /** The Service Mode: */
    /** 0: Room has a beacon, Person has a mhub */
    /** 1: Room has a mhub, Person has a beacon */
    private final Integer MODE = 0;

    @Context
    private UriInfo context;
    
    //Compares two hospital by the nuber of patients at the moment
    /*
    private Comparator<Hospital> hospitalComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital h1, Hospital h2){
            Occupancy oc1 = null;
            try {
                oc1 = getHospitalOccupancy(h1);
            } catch (Exception ex) {
                Logger.getLogger(SemanticResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer patients1 = oc1.getnPatientsNow();
            
            Occupancy oc2 = null;
            try {
                oc2 = getHospitalOccupancy(h2);
            } catch (Exception ex) {
                Logger.getLogger(SemanticResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer patients2 = oc2.getnPatientsNow();
            
            return patients1.compareTo(patients2);
        }
    };*/

    /**
     * Creates a new instance of SemanticResource
     */
    public SemanticResource() {
        mapper = new ObjectMapper();
    }

    /**
     * Retrieves representation of an instance of
     * br.pucrio.inf.lac.hospital.semanticws.core.SemanticResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "{\n"
                + "  \"personID\" : 1,\n"
                + "  \"personName\" : \"Daniel\",\n"
                + "  \"personEmail\" : \"daniel@lsdi.ufma.br\",\n"
                + "  \"mhubID\" : \"399eba78-2346-4c20-81ba-0a8cad5fdd22\",\n"
                + "  \"thingID\": \"d4d70f9c-b5fa-4a47-8441-e90c8942f6ff\",\n"
                + "  \"roomName\" : \"Sala das ETs\",\n"
                + "  \"sectionName\" : \"LSDi\",\n"
                + "  \"buildingName\" : \"Prédio da Pós\",\n"
                + "  \"cityName\" : \"São Luís\",\n"
                + "  \"duration\" : 3600, //Em segundos\n"
                + "}";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/byroom/{room}/")
    public String getPersonByRoom(@PathParam("room") String room) throws Exception {
        Room r = dao.getRoomByName(room);
        if(r == null) return "[]";
        Set<HasA> hasASet = dao.getHasAByRoom(r.getRoomID());
        Set<Device> deviceSet = new HashSet<>();
        for (HasA h : hasASet) {
            deviceSet.add(dao.getDevice(h.getDeviceID()));
        }

        String returnJson = "[";
        //String returnJson = "{ "
        //        + "\"persons\": "
        //        + "[";
        Set<Rendezvous> rendezvousSet = null;
        for (Device d : deviceSet) {
            if(MODE == 0){
                rendezvousSet = getDurationByThing(d.getThingID());  //duration/thing/{thingID}
            }else if(MODE == 1){
                rendezvousSet = getDurationByMHub(d.getMhubID());   //duration/mhub/{mhubID}
            }
            if(rendezvousSet == null) return "[]";
            for (Rendezvous re: rendezvousSet) {
                Device d2 = null;
                if(MODE == 0){
                    d2 = dao.getDeviceByMHub(re.getMhubID());
                }else if(MODE == 1){
                    d2 = dao.getDeviceByThing(re.getThingID());
                }
                HasA h = dao.getHasAByDevice(d2.getDeviceID());
                Person p = dao.getPerson(h.getPersonID());
                returnJson += "{\"name\": \"" + p.getPersonName() + "\", "
                        + "\"email\": \"" + p.getPersonEmail() + "\", ";
                
                if(MODE == 0){
                    returnJson += "\"mhubID\": \"" + re.getMhubID() + "\", ";
                }else if(MODE == 1){
                    returnJson += "\"thingID\": \"" + re.getThingID() + "\", ";
                }
                
                returnJson += "\"duration\": " + re.getDuration() + "}, ";
            }
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/byroomandtime/{room}/{Q}/{W}/")
    public String getPersonByRoomAndTime(@PathParam("room") String room, @PathParam("Q") long Q, @PathParam("W") long W) throws Exception {
        Room r = dao.getRoomByName(room);
        if(r == null) return "[]";
        Set<HasA> hasASet = dao.getHasAByRoom(r.getRoomID());
        Set<Device> deviceSet = new HashSet<>();
        for (HasA h : hasASet) {
            deviceSet.add(dao.getDevice(h.getDeviceID()));
        }

        String returnJson = "[";
        //String returnJson = "{ "
        //        + "\"persons\": "
        //        + "[";
        Set<Rendezvous> rendezvousSet = null;
        for (Device d : deviceSet) {
            if(MODE == 0){
                rendezvousSet = getRendezvousByThingAndTime(d.getThingID(), Q, W);  //rendezvous/thingandtime/{thingID}/{Q}/{W}
            }else if(MODE == 1){
                rendezvousSet = getRendezvousByMHubAndTime(d.getMhubID(), Q, W);   //duration/mhubandtime/{mhubID}/{Q}/{W}
            }
            if(rendezvousSet == null) return "[]";
            for (Rendezvous re: rendezvousSet) {
                Device d2 = null;
                if(MODE == 0){
                    d2 = dao.getDeviceByMHub(re.getMhubID());
                }else if(MODE == 1){
                    d2 = dao.getDeviceByThing(re.getThingID());
                }
                HasA h = dao.getHasAByDevice(d2.getDeviceID());
                Person p = dao.getPerson(h.getPersonID());
                returnJson += "{\"name\": \"" + p.getPersonName() + "\", "
                        + "\"email\": \"" + p.getPersonEmail() + "\", ";
                
                if(MODE == 0){
                    returnJson += "\"mhubID\": \"" + re.getMhubID() + "\", ";
                }else if(MODE == 1){
                    returnJson += "\"thingID\": \"" + re.getThingID() + "\", ";
                }
                
                returnJson += "\"arrive\": " + re.getArrive() + ", ";
                returnJson += "\"depart\": " + re.getDepart() + "}, ";
            }
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/rendezvous/{Q}/{W}/{emails: .*}/")
    public String getPersonRendezvous(@PathParam("Q") long Q, @PathParam("W") long W, @PathParam("emails") List<PathSegment> emails) throws Exception {
        boolean flag = false;
        Set<Person> personGroup = new HashSet<>();
        Set<Rendezvous> reSet = new HashSet<>();
        ArrayList<GroupRendezvous> gReSet = new ArrayList<>();
        for (PathSegment email: emails) {
            if(dao.getPersonByEmail(email.getPath()) == null) return "[]";
            personGroup.add(dao.getPersonByEmail(email.getPath()));
        }
        if(personGroup.size() < 2 || personGroup.size() != emails.size()) return "[]";
        for (Person p: personGroup){
            Set<HasA> hasASet = dao.getHasAByPerson(p.getPersonID());
            Set<Device> deviceSet = new HashSet<>();
            for (HasA h : hasASet) {
                deviceSet.add(dao.getDevice(h.getDeviceID()));
            }

            Set<Rendezvous> rendezvousSet = null;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = getRendezvousByMHubAndTime(d.getMhubID(), Q, W);   //rendezvous/mhubandtime/{mhubID}/{Q}/{W}
                }else if(MODE == 1){
                    rendezvousSet = getRendezvousByThingAndTime(d.getThingID(), Q, W);  //rendezvous/thingandtime/{thingID}/{Q}/{W}
                }
                if(rendezvousSet == null) return "[]";
                for (Rendezvous re: rendezvousSet) {
                    Device d2 = null;
                    if(MODE == 0){
                        d2 = dao.getDeviceByThing(re.getThingID());
                    }else if(MODE == 1){
                        d2 = dao.getDeviceByMHub(re.getMhubID());
                    }
                    HasA h = dao.getHasAByDevice(d2.getDeviceID());
                    Room r = dao.getRoom(h.getRoomID());
                    reSet.add(new Rendezvous(p.getPersonName(), r.getRoomName(), re.getArrive(), re.getDepart()));
                }
            }
        }
        
        for (Rendezvous re1 : reSet) {
            GroupRendezvous gr = new GroupRendezvous(re1.getRoomName(), re1.getArrive(), re1.getDepart());
            gr.getPersonGroup().add(re1.getPersonName());
            for (Rendezvous re2 : reSet) {
                if(!gr.getPersonGroup().contains(re2.getPersonName())){
                    if(gr.getRoomName().equals(re2.getRoomName()) && gr.getArrive() <= re2.getDepart() && gr.getDepart() >= re2.getArrive()){
                        gr.getPersonGroup().add(re2.getPersonName());
                        if(gr.getArrive() < re2.getArrive()) gr.setArrive(re2.getArrive());
                        if(gr.getDepart() > re2.getDepart()) gr.setDepart(re2.getDepart());
                    }
                }
            }
            if(gr.getPersonGroup().size() >= 2) {
                if(gReSet.isEmpty()){
                    gReSet.add(gr);
                }else if(!(containsGroupRendezvous(gReSet, gr))){
                    gReSet.add(gr);
                }
            }
        }

        String returnJson = "[";
        //String returnJson = "{ "
        //        + "\"rendezvous\": "
        //        + "[";
        for (GroupRendezvous gr : gReSet) {
            returnJson += "{\"room\": \"" + gr.getRoomName() + "\", ";
            returnJson += "\"person\": [";
            for (String s : gr.getPersonGroup()) {
                returnJson += "{\"name\": \"" + s + "\"}, ";
            }
            returnJson = returnJson.substring(0, returnJson.length() - 2);
            returnJson += "], ";
            returnJson += "\"arrive\": " + gr.getArrive() + ", ";
            returnJson += "\"depart\": " + gr.getDepart() + "}, ";
        }
         //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("room/byperson/{emails: .*}/")
    public String getRoomByPerson(@PathParam("emails") List<PathSegment> emails) throws Exception {
        Set<Person> personGroup = new HashSet<>();
        for (PathSegment email: emails) {
            personGroup.add(dao.getPersonByEmail(email.getPath()));
        }
        if(personGroup.size() != emails.size()) return "[]";
        
        String returnJson = "[";
        for (Person p: personGroup){
            Set<HasA> hasASet = dao.getHasAByPerson(p.getPersonID());
            Set<Device> deviceSet = new HashSet<>();
            for (HasA h : hasASet) {
                deviceSet.add(dao.getDevice(h.getDeviceID()));
            }

            Set<Rendezvous> rendezvousSet = null;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = getDurationByMHub(d.getMhubID());   //duration/mhub/{mhubID}
                }else if(MODE == 1){
                    rendezvousSet = getDurationByThing(d.getThingID());  //duration/thing/{thingID}
                }
                if(rendezvousSet == null) return "[]";
                for (Rendezvous re: rendezvousSet) {
                    Device d2 = null;
                    if(MODE == 0){
                        d2 = dao.getDeviceByThing(re.getThingID());
                    }else if(MODE == 1){
                        d2 = dao.getDeviceByMHub(re.getMhubID());
                    }
                    HasA h = dao.getHasAByDevice(d2.getDeviceID());
                    Room r = dao.getRoom(h.getRoomID());
                    
                    returnJson += "{\"name\": \"" + p.getPersonName() + "\", "
                        + "\"room\": \"" + r.getRoomName() + "\", ";
                    if(MODE == 0){
                        returnJson += "\"thingID\": \"" + re.getThingID() + "\", ";
                    }else if(MODE == 1){
                        returnJson += "\"mhubID\": \"" + re.getMhubID() + "\", ";
                    }
                    returnJson += "\"duration\": " + re.getDuration() + "}, ";
                }
            }
        }
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("room/bypersonandtime/{Q}/{W}/{emails: .*}/")
    public String getRoomByPersonAndTime(@PathParam("Q") long Q, @PathParam("W") long W, @PathParam("emails") List<PathSegment> emails) throws Exception {
        Set<Person> personGroup = new HashSet<>();
        for (PathSegment email: emails) {
            personGroup.add(dao.getPersonByEmail(email.getPath()));
        }
        if(personGroup.size() != emails.size()) return "[]";
        
        String returnJson = "[";
        for (Person p: personGroup){
            Set<HasA> hasASet = dao.getHasAByPerson(p.getPersonID());
            Set<Device> deviceSet = new HashSet<>();
            for (HasA h : hasASet) {
                deviceSet.add(dao.getDevice(h.getDeviceID()));
            }

            Set<Rendezvous> rendezvousSet = null;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = getRendezvousByMHubAndTime(d.getMhubID(), Q, W);   //rendezvous/mhubandtime/{mhubID}/{Q}/{W}
                }else if(MODE == 1){
                    rendezvousSet = getRendezvousByThingAndTime(d.getThingID(), Q, W);  //rendezvous/thingandtime/{thingID}/{Q}/{W}
                }
                if(rendezvousSet == null) return "[]";
                for (Rendezvous re: rendezvousSet) {
                    Device d2 = null;
                    if(MODE == 0){
                        d2 = dao.getDeviceByThing(re.getThingID());
                    }else if(MODE == 1){
                        d2 = dao.getDeviceByMHub(re.getMhubID());
                    }
                    HasA h = dao.getHasAByDevice(d2.getDeviceID());
                    Room r = dao.getRoom(h.getRoomID());
                    
                    returnJson += "{\"name\": \"" + p.getPersonName() + "\", "
                        + "\"room\": \"" + r.getRoomName() + "\", ";
                    if(MODE == 0){
                        returnJson += "\"thingID\": \"" + re.getThingID() + "\", ";
                    }else if(MODE == 1){
                        returnJson += "\"mhubID\": \"" + re.getMhubID() + "\", ";
                    }
                    returnJson += "\"arrive\": " + re.getArrive() + ", ";
                    returnJson += "\"depart\": " + re.getDepart() + "}, ";
                }
            }
        }
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        
        return returnJson;
    }
    
    // duration/thing/{thingID}
    private Set<Rendezvous> getDurationByThing(UUID thingID) throws Exception {
        UUID mhubID = null;
        long duration = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = null;
        
        //Get Average Duration
        url = HORYS
                + "/api/duration/thing/"+thingID;
        returnedJson = sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        if(data.length() != 0){
            JSONObject text = data.getJSONObject(0);

            rendezvousSet = new HashSet<>();
            mhubID = UUID.fromString(text.getString("mhubID"));
            duration = text.getLong("duration");
            rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));
        }
        
        return rendezvousSet;
    }
    
    // duration/mhub/{mhubID}
    private Set<Rendezvous> getDurationByMHub(UUID mhubID) throws Exception {
        UUID thingID = null;
        long duration = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = null;
        
        //Get Average Duration
        url = HORYS
                + "/api/duration/mhub/"+mhubID;
        returnedJson = sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        if(data.length() != 0){
            JSONObject text = data.getJSONObject(0);

            rendezvousSet = new HashSet<>();
            thingID = UUID.fromString(text.getString("thingID"));
            duration = text.getLong("duration");
            rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));
        }
        
        return rendezvousSet;
    }
    
    // rendezvous/thingandtime/{thingID}/{W}/{W}
    private Set<Rendezvous> getRendezvousByThingAndTime(UUID thingID, long Q, long W) throws Exception {
        UUID mhubID = null;
        long arrive = 0;
        long depart = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        
        //Get Average Duration
        url = HORYS
                + "/api/rendezvous/thingandtime/"+thingID+"/"+Q+"/"+W;
        returnedJson = sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        for(int i = 0; i < data.length(); i++){
            JSONObject text = data.getJSONObject(i);

            mhubID = UUID.fromString(text.getString("mhubID"));
            arrive = text.getLong("arrive");
            depart = text.getLong("depart");
            rendezvousSet.add(new Rendezvous(mhubID, thingID, arrive, depart));
        }
        
        return rendezvousSet;
    }
    
    // rendezvous/mhubandtime/{mhubID}/{W}/{W}
    private Set<Rendezvous> getRendezvousByMHubAndTime(UUID mhubID, long Q, long W) throws Exception {
        UUID thingID = null;
        long arrive = 0;
        long depart = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        
        //Get Average Duration
        url = HORYS
                + "/api/rendezvous/mhubandtime/"+mhubID+"/"+Q+"/"+W;
        returnedJson = sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
       for(int i = 0; i < data.length(); i++){
            JSONObject text = data.getJSONObject(i);

            thingID = UUID.fromString(text.getString("thingID"));
            arrive = text.getLong("arrive");
            depart = text.getLong("depart");
            rendezvousSet.add(new Rendezvous(mhubID, thingID, arrive, depart));
        }
        
        return rendezvousSet;
    }
    
    /*
    private Map<String, Set<String>> getInsurancesAndSpecialties(Hospital h) {
        Map<String, Set<String>> returnMap = new HashMap<>();

        Set<String> insurances = new HashSet<>();
        Set<String> specialties = new HashSet<>();

        Set<AcceptedBySpecialty> accSet;
        accSet = dao.getAcceptedBySpecialtyByHospital(h.getHospitalID());
        if (accSet != null) {
            for (AcceptedBySpecialty acc : accSet) {
                Insurance i = dao.getInsurance(acc.getInsuranceID());
                insurances.add("\"" + i.getInsuranceName() + "\"");
                Specialty s = dao.getSpecialty(acc.getSpecialtyID());
                specialties.add("\"" + s.getSpecialtyname() + "\"");
            }
        }

        returnMap.put("insurances", insurances);
        returnMap.put("specialties", specialties);

        return returnMap;
    }
    */
    
    private String sendGet(String url, String method) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(method);

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }
    
    private boolean containsGroupRendezvous(ArrayList<GroupRendezvous> grs, GroupRendezvous gr){
        for (GroupRendezvous g : grs) {
            if(g.getRoomName().equals(gr.getRoomName()) && g.getArrive() == gr.getArrive() && g.getDepart() == gr.getDepart()) {
                return true;
            }
        }
        return false;
    }
}

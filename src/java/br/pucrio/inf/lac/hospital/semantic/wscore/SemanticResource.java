package br.pucrio.inf.lac.hospital.semantic.wscore;

import br.pucrio.inf.lac.hospital.horys.protocol.HORYSProtocol;
import br.pucrio.inf.lac.hospital.semantic.data.Beacon;
import br.pucrio.inf.lac.hospital.semantic.data.Building;
import br.pucrio.inf.lac.hospital.semantic.data.City;
import br.pucrio.inf.lac.hospital.semantic.data.Device;
import br.pucrio.inf.lac.hospital.semantic.data.HasA;
import br.pucrio.inf.lac.hospital.semantic.data.MHub;
import br.pucrio.inf.lac.hospital.semantic.data.Person;
import br.pucrio.inf.lac.hospital.semantic.data.Rendezvous;
import br.pucrio.inf.lac.hospital.semantic.data.Room;
import br.pucrio.inf.lac.hospital.semantic.data.Section;
import br.pucrio.inf.lac.hospital.semantic.database.SemanticDao;
import br.pucrio.inf.lac.hospital.semantic.database.SemanticDaoImpMariaDB;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * REST Web Service
 *
 */
@Path("get")
public class SemanticResource {

    private static SemanticDao dao = new SemanticDaoImpMariaDB();
    
    private ObjectMapper mapper;
    
    /** The  Horys restport. */
    private final String HORYS = "http://lsdi.ufma.br:7981";
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
        Room r = dao.getRoom(room);
        Set<HasA> hasASet = dao.getHasAByRoom(r.getRoomID());
        Set<Device> deviceSet = new HashSet<>();
        for (HasA h : hasASet) {
            deviceSet.add(dao.getDevice(h.getDeviceID()));
        }

        String returnJson = "{ "
                + "\"persons\": "
                + "[";
        Set<Rendezvous> rendezvousSet = null;
        for (Device d : deviceSet) {
            if(MODE == 0){
                rendezvousSet = getDurationByThing(d.getThingID());  //duration/thing/{thingID}
            }else if(MODE == 1){
                rendezvousSet = getDurationByMHub(d.getMhubID());   //duration/mhub/{mhubID}
            }
            for (Rendezvous re: rendezvousSet) {
                Device d2 = null;
                if(MODE == 0){
                    d2 = dao.getDeviceByMHub(re.getMhubID());
                }else if(MODE == 1){
                    d2 = dao.getDeviceByThing(re.getThingID());
                }
                HasA h = dao.getHasAByDevice(d2.getDeviceID());
                Person p = dao.getPerson(h.getPersonID());
                returnJson += "\n{\"name\': \'" + p.getPersonName() + "\', "
                        + "\"email\": " + p.getPersonEmail() + ", ";
                
                if(MODE == 0){
                    returnJson += "\"mhubID\": " + re.getMhubID() + ", ";
                }else if(MODE == 1){
                    returnJson += "\"thingID\": " + re.getThingID() + ", ";
                }
                
                returnJson += "\"duration\": " + re.getDuration() + "}, ";
            }
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }
    
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/bycity/{city}")
    public String getHospitalByCity(@PathParam("city") String city) throws Exception {

        Set<Hospital> hospitalSet = dao.getHospitalsByCity(city);

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        //Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalSet) {
            ad = dao.getAddress(h.getAddressID());
            //oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    //+ " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    //+ "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/byid/{id}")
    public String getHospitalByID(@PathParam("id") long id) throws Exception {

        Hospital h = dao.getHospital(id);

        String returnJson;
        //Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        ad = dao.getAddress(h.getAddressID());
        //oc = getHospitalOccupancy(h);
        insurancesAndSpecialties = getInsurancesAndSpecialties(h);
        returnJson = "{\"name\': \'" + h.getHospitalName() + "\', "
                //+ " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                //+ "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                + "\"acceptedInsurances\": "
                + insurancesAndSpecialties.get("insurances") + ", "
                + "\"specialities\": "
                + insurancesAndSpecialties.get("specialties") + ", "
                + "\"lat\": " + h.getLatitude() + ", "
                + "\"long\": " + h.getLongitude() + ", "
                + "\"address\": {"
                + "\"state\": \"" + ad.getState() + "\", "
                + "\"city\": \"" + ad.getCity() + "\", "
                + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                + "\"zipcode\": ";

        //Check if there is a zipcode
        returnJson += (ad.getZipcode() != null)
                ? "\"" + ad.getZipcode() + "\", "
                : "null, ";

        returnJson += "\"street\": " + ad.getStreet() + ", "
                + "\"number\": " + ad.getNumber() + ", "
                + "\"AdditionalInfo\": ";

        //Check if there is Additional Info
        returnJson += (ad.getAdditionalInfo() != null)
                ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                : "null}}";

        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/byspecialtyandcity/{specialtyID}/{city}")
    public String getHospitalBySpeciatyAndCity(@PathParam("specialtyID") long sID, @PathParam("city") String city) throws Exception {

        Set<Hospital> hospitalSet = dao.getHospitalsByCityAndSpecialty(city, sID);

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalSet) {
            ad = dao.getAddress(h.getAddressID());
            oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    + " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    + "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/byinsuraceandcity/{insuranceID}/{city}")
    public String getHospitalByInsuraceAndCity(@PathParam("insuranceID") long iID, @PathParam("city") String city) throws Exception {

        Set<Hospital> hospitalSet = dao.getHospitalsByCityAndInsurance(city, iID);

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalSet) {
            ad = dao.getAddress(h.getAddressID());
            oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    + " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    + "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/byspecialtyandinsuranceandcity/{specialtyID}/{insuranceID}/{city}")
    public String getHospitalBySpeciatyAndCityAndInsurance(@PathParam("specialtyID") long sID, @PathParam("insuranceID") long iID, @PathParam("city") String city) throws Exception {

        Set<Hospital> hospitalSet = dao.getHospitalsByCityAndSpecialtyAndInsurance(city, sID, iID);

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalSet) {
            ad = dao.getAddress(h.getAddressID());
            oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    + " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    + "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/top5")
    public String getHospitalTop5() throws Exception {
        
        LinkedList<Hospital> hospitalComplete = new LinkedList(dao.getHospitals());
        LinkedList<Hospital> hospitalTop5 = new LinkedList<>();
        
        //Sort the Hospitals
        Collections.sort(hospitalComplete, hospitalComparator);
        
        //Get the top5
        for(int i=0; i<5; i++){
            if(hospitalComplete.size()>i)
                hospitalTop5.add(hospitalComplete.get(i));
            else
                break;
        }
        

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalTop5) {
            ad = dao.getAddress(h.getAddressID());
            oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    + " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    + "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/top5/byspecialtyandcity/{specialtyID}/{city}")
    public String getHospitalTop5BySpeciatyAndCity(@PathParam("specialtyID") long sID, @PathParam("city") String city) throws Exception {
        Set<Hospital> hospitalSet = dao.getHospitalsByCityAndSpecialty(city, sID);
        LinkedList<Hospital> hospitalComplete = new LinkedList(hospitalSet);
        LinkedList<Hospital> hospitalTop5 = new LinkedList<>();
        
        //Sort the Hospitals
        Collections.sort(hospitalComplete, hospitalComparator);
        
        //Get the top5
        for(int i=0; i<5; i++){
            if(hospitalComplete.size()>i)
                hospitalTop5.add(hospitalComplete.get(i));
            else
                break;
        }
        

        String returnJson = "{ "
                + "\"hospitals\": "
                + "[";
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        for (Hospital h : hospitalTop5) {
            ad = dao.getAddress(h.getAddressID());
            oc = getHospitalOccupancy(h);
            insurancesAndSpecialties = getInsurancesAndSpecialties(h);
            returnJson += "\n{\"name\': \'" + h.getHospitalName() + "\', "
                    + " \"nPatientsNow\": " + oc.getnPatientsNow() + ", "
                    + "\"avgWaitTime\": " + oc.getAvgWaitTime() + ", "
                    + "\"acceptedInsurances\": "
                    + insurancesAndSpecialties.get("insurances") + ", "
                    + "\"specialities\": "
                    + insurancesAndSpecialties.get("specialties") + ", "
                    + "\"lat\": " + h.getLatitude() + ", "
                    + "\"long\": " + h.getLongitude() + ", "
                    + "\"address\": {"
                    + "\"state\": \"" + ad.getState() + "\", "
                    + "\"city\": \"" + ad.getCity() + "\", "
                    + "\"neighborhood\": \"" + ad.getNeighborhood() + "\", "
                    + "\"zipcode\": ";

            //Check if there is a zipcode
            returnJson += (ad.getZipcode() != null)
                    ? "\"" + ad.getZipcode() + "\", "
                    : "null, ";

            returnJson += "\"street\": " + ad.getStreet() + ", "
                    + "\"number\": " + ad.getNumber() + ", "
                    + "\"AdditionalInfo\": ";

            //Check if there is Additional Info
            returnJson += (ad.getAdditionalInfo() != null)
                    ? "\"" + ad.getAdditionalInfo() + "\"}}, "
                    : "null}}, ";
        }
        //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]}";
        return returnJson;
    }
    */
    // duration/thing/{thingID}
    private Set<Rendezvous> getDurationByThing(UUID thingID) throws Exception {
        UUID mhubID = null;
        long duration = 0;
        String url;
        String returnedJson;
        HORYSProtocol hp;
        Map<String, Object> parameters;
        ArrayList<UUID> connectedMHubs;
        /*
        //Get Average Duration
        url = HORYS
                + "/api/avgrendezvousduration/"+thingID;
        returnedJson = sendGet(url, "GET");
        hp = mapper.readValue(returnedJson, HORYSProtocol.class);
        parameters = hp.getParameters();
        avgDuration += (Double)parameters.get("average");

        //Get connectedMHubs
        url = HORYS
                + "/api/connectedmhubs/"+thingID;
        returnedJson = sendGet(url, "GET");
        hp = mapper.readValue(returnedJson, HORYSProtocol.class);
        parameters = hp.getParameters();
        connectedMHubs = (ArrayList<UUID>)parameters.get("response");
        nPatientsNow += connectedMHubs.size();
        */
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));

        return rendezvousSet;
    }
    
    // duration/mhub/{mhubID}
    private Set<Rendezvous> getDurationByMHub(UUID mhubID) throws Exception {
        UUID thingID = null;
        long duration = 0;
        String url;
        String returnedJson;
        HORYSProtocol hp;
        Map<String, Object> parameters;
        ArrayList<UUID> connectedMHubs;
        /*
        //Get Average Duration
        url = HORYS
                + "/api/avgrendezvousduration/"+mhubID;
        returnedJson = sendGet(url, "GET");
        hp = mapper.readValue(returnedJson, HORYSProtocol.class);
        parameters = hp.getParameters();
        avgDuration += (Double)parameters.get("average");

        //Get connectedMHubs
        url = HORYS
                + "/api/connectedmhubs/"+thingID;
        returnedJson = sendGet(url, "GET");
        hp = mapper.readValue(returnedJson, HORYSProtocol.class);
        parameters = hp.getParameters();
        connectedMHubs = (ArrayList<UUID>)parameters.get("response");
        nPatientsNow += connectedMHubs.size();
        */
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));

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
    // HTTP GET request
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
}

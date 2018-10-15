package br.pucrio.inf.lac.hospital.semantic.wscore;

import br.pucrio.inf.lac.hospital.horys.protocol.HORYSProtocol;
import br.pucrio.inf.lac.hospital.semantic.data.AcceptedBySpecialty;
import br.pucrio.inf.lac.hospital.semantic.data.Address;
import br.pucrio.inf.lac.hospital.semantic.data.Beacon;
import br.pucrio.inf.lac.hospital.semantic.data.Hospital;
import br.pucrio.inf.lac.hospital.semantic.data.Insurance;
import br.pucrio.inf.lac.hospital.semantic.data.Occupancy;
import br.pucrio.inf.lac.hospital.semantic.data.Specialty;
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
    private final int HORYS_PORT = 30000;

    @Context
    private UriInfo context;
    
    //Compares two hospital by the nuber of patients at the moment
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
    };

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
                + "  \"name\" : \"Nome do Hopital\",\n"
                + "  \"nPatientsNow\" : 50,\n"
                + "  \"avgWaitTime\" : 180, //Em segundos\n"
                + "  \"acceptedInsurances\" : [\"Plano1\", \"Plano2\"],\n"
                + "  \"specialities\": [\"Clínico Geral\",\"Ortopedia\"],\n"
                + "  \"lat\" : -22.981128,\n"
                + "  \"long\" : -43.235839,\n"
                + "  \"address\" : {\n"
                + "    \"state\" : \"RJ\", //Pensei em apenas dois caracteres em Maiúsculo pra estados\n"
                + "    \"city\" : \"Rio de Janeiro\",\n"
                + "    \"neighborhood\" : \"Gávea\",\n"
                + "    \"zipcode\" : \"22451-041\",\n"
                + "    \"street\" : \"R. Marquês de São Vicente\",\n"
                + "    \"number\" : 331,\n"
                + "    \"AdditionalInfo\" : \"Quadra tal, Perto de ...\"\n"
                + "  }\n"
                + "}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hospital/bycity/{city}")
    public String getHospitalByCity(@PathParam("city") String city) throws Exception {

        Set<Hospital> hospitalSet = dao.getHospitalsByCity(city);

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
    @Path("hospital/byid/{id}")
    public String getHospitalByID(@PathParam("id") long id) throws Exception {

        Hospital h = dao.getHospital(id);

        String returnJson;
        Occupancy oc;
        Map<String, Set<String>> insurancesAndSpecialties;
        Address ad;
        ad = dao.getAddress(h.getAddressID());
        oc = getHospitalOccupancy(h);
        insurancesAndSpecialties = getInsurancesAndSpecialties(h);
        returnJson = "{\"name\': \'" + h.getHospitalName() + "\', "
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

    private Occupancy getHospitalOccupancy(Hospital h) throws Exception {
        double avgDuration = 0.0;
        int nPatientsNow = 0;
        String urlGetAvgDuration;
        String urlGetConnectedMHubs;
        String returnedJson;
        HORYSProtocol hp;
        Map<String, Object> parameters;
        ArrayList<UUID> connectedMHubs;
        
        //Get the beacons in the Hospital
        Set<Beacon> beacons = dao.getBeaconsByHospital(h.getHospitalID());
        
        //Get info from Horiz
        for(Beacon b : beacons){
            //Get Average Duration
            urlGetAvgDuration = "http://localhost:"+HORYS_PORT
                    + "/api/avgrendezvousduration/"+b.getThingID();
            returnedJson = sendGet(urlGetAvgDuration, "GET");
            hp = mapper.readValue(returnedJson, HORYSProtocol.class);
            parameters = hp.getParameters();
            avgDuration += (Double)parameters.get("average");
            
            //Get connectedMHubs
            urlGetConnectedMHubs = "http://localhost:"+HORYS_PORT
                    + "/api/connectedmhubs/"+b.getThingID();
            returnedJson = sendGet(urlGetConnectedMHubs, "GET");
            hp = mapper.readValue(returnedJson, HORYSProtocol.class);
            parameters = hp.getParameters();
            connectedMHubs = (ArrayList<UUID>)parameters.get("response");
            nPatientsNow += connectedMHubs.size();
        }
        
        if(beacons.size()>0)
            avgDuration = avgDuration / beacons.size();
        
        Occupancy oc = new Occupancy(nPatientsNow, avgDuration);

        return oc;
    }

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

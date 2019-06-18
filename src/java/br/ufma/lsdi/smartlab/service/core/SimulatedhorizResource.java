package br.ufma.lsdi.smartlab.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author fbeneditovm
 */
@Path("simulatedhoriz")
public class SimulatedhorizResource {

    @Context
    private UriInfo context;
    
    private ObjectMapper mapper;

    /**
     * Creates a new instance of SimulatedhorizResource
     */
    public SimulatedhorizResource() {
        mapper = new ObjectMapper();
    }

    /**
     * Retrieves representation of an instance of br.pucrio.inf.lac.hospital.semantic.wscore.SimulatedhorizResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/rendezvous/mhubandtime/{mhubid}/{Q}/{W}")
    public String getRendezvousByMHubAndTime(@PathParam("mhubid") String strMHubID, @PathParam("Q") long Q, @PathParam("W") long W) throws JsonProcessingException, JSONException {
        UUID mhubID = UUID.fromString(strMHubID);
        
        JSONObject element = new JSONObject();
        JSONObject element2 = new JSONObject();
        JSONArray elements = new JSONArray();
        JSONArray reply = new JSONArray();
        
        element.put("appID", "f9d6ec17-53fc-4d39-9d15-c5e49badf3da");
        element.put("mhubID", "a246df10-7902-3715-9abc-e4148bb97788");
        element.put("thingID", "f80638aa-e7d7-3f8b-b538-aa8f78cce93b");
        element.put("start", 1542031200); //11/12/2018 @ 2:00pm
        element.put("end", 1542042000);   //11/12/2018 @ 5:00pm
        
        element2.put("appID", "f9d6ec17-53fc-4d39-9d15-c5e49badf3da");
        element2.put("mhubID", "4729ce72-09eb-4307-845e-eb9957e9b490");
        element2.put("thingID", "f80638aa-e7d7-3f8b-b538-aa8f78cce93b");
        element2.put("start", 1542038400); //11/12/2018 @ 4:00pm
        element2.put("end", 1542045600);   //11/12/2018 @ 6:00pm
        
        elements.put(element);
        elements.put(element2);
        
        for (int i = 0; i < elements.length(); i++) {
            JSONObject e = elements.getJSONObject(i);
            if(e.getLong("start") <= W && e.getLong("end") >= Q){
                reply.put(e);
            }
        }
        
        return reply.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/rendezvous/thingandtime/{thingid}/{Q}/{W}")
    public String getRendezvousByThingAndTime(@PathParam("thingid") String strThingID, @PathParam("Q") long Q, @PathParam("W") long W) throws JsonProcessingException, JSONException {
        UUID thingID = UUID.fromString(strThingID);
        
        JSONObject element = new JSONObject();
        JSONObject element2 = new JSONObject();
        JSONArray elements = new JSONArray();
        JSONArray reply = new JSONArray();
        
        element.put("appID", "f9d6ec17-53fc-4d39-9d15-c5e49badf3da");
        element.put("mhubID", "a246df10-7902-3715-9abc-e4148bb97788");
        element.put("thingID", "f80638aa-e7d7-3f8b-b538-aa8f78cce93b");
        element.put("start", 1542031200); //11/12/2018 @ 2:00pm
        element.put("end", 1542042000);   //11/12/2018 @ 5:00pm
        
        element2.put("appID", "f9d6ec17-53fc-4d39-9d15-c5e49badf3da");
        element2.put("mhubID", "4729ce72-09eb-4307-845e-eb9957e9b490");
        element2.put("thingID", "f80638aa-e7d7-3f8b-b538-aa8f78cce93b");
        element2.put("start", 1542038400); //11/12/2018 @ 4:00pm
        element2.put("end", 1542045600);   //11/12/2018 @ 6:00pm
        
        elements.put(element);
        elements.put(element2);
        
        for (int i = 0; i < elements.length(); i++) {
            JSONObject e = elements.getJSONObject(i);
            if(e.getLong("start") <= W && e.getLong("end") >= Q){
                reply.put(e);
            }
        }
        
        return reply.toString();
    }
    
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("avgrendevouzduration/{thingid}")
    public String getAverageRendezvousDuration(@PathParam("thingid") String strThingID) throws JsonProcessingException {
        UUID thingID = UUID.fromString(strThingID);
        
        Map<String, Object> replyParameters = new HashMap<>();
        
        Map<String, Double> elementsMap = new HashMap<>();
        
        elementsMap.put(UUID.randomUUID().toString(), 201.0);
        elementsMap.put(UUID.randomUUID().toString(), 160.0);
        
        replyParameters.put("average", 180.5);
        replyParameters.put("elements", elementsMap);
        replyParameters.put("thingID", thingID);
        
        HORYSProtocol hp = new HORYSProtocol(HORYSProtocol.Mode.REPLY, HORYSProtocol.Operation.GETAVGRENDEZVOUSDURATION, replyParameters);
        
        return mapper.writeValueAsString(hp);
    }*/
    /* 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("connectedmhubs/{thingid}")
    public String getConnectedMHubs(@PathParam("thingid") String strThingID) throws JsonProcessingException {
        UUID thingID = UUID.fromString(strThingID);
        
        Set<UUID> connectedMHubs = new HashSet<>();
        
        for(int i=0; i<5; i++)
            connectedMHubs.add(UUID.randomUUID());
        
        Map<String, Object> replyParameters = new HashMap<>();
        replyParameters.put("thingID", thingID);
        replyParameters.put("response", connectedMHubs);
        
        HORYSProtocol hp = new HORYSProtocol(HORYSProtocol.Mode.REPLY, HORYSProtocol.Operation.GETCONNECTEDMHUBS, replyParameters);
        
        return mapper.writeValueAsString(hp);
    }
    */
}

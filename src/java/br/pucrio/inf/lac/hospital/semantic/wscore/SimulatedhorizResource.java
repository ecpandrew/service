package br.pucrio.inf.lac.hospital.semantic.wscore;

import br.pucrio.inf.lac.hospital.horys.protocol.HORYSProtocol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    }
    
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
}

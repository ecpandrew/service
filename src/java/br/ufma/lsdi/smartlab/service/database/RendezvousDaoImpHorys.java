package br.ufma.lsdi.smartlab.service.database;

import br.ufma.lsdi.smartlab.service.data.Rendezvous;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class RendezvousDaoImpHorys implements RendezvousDao{
    /** The  Horys restport. */
    private final String HORYS = "http://smartlab.lsdi.ufma.br/horys/api/";
    
    @Override
    public Set<Rendezvous> getDurationByThing(UUID thingID) throws Exception {
        UUID mhubID = null;
        long duration = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        
        //Get Average Duration
        url = HORYS
                + "duration/thing/"+thingID;
        returnedJson = REST.sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        for(int i = 0; i < data.length(); i++){
            JSONObject text = data.getJSONObject(i);

            mhubID = UUID.fromString(text.getString("mhubID"));
            duration = text.getLong("duration");
            if(duration != 0)
               rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));
        }
        
        return rendezvousSet;
    }
    
    @Override
    public Set<Rendezvous> getDurationByMHub(UUID mhubID) throws Exception {
        UUID thingID = null;
        long duration = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        
        //Get Average Duration
        url = HORYS
                + "duration/mhub/"+mhubID;
        returnedJson = REST.sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        for(int i = 0; i < data.length(); i++){
            JSONObject text = data.getJSONObject(i);

            thingID = UUID.fromString(text.getString("thingID"));
            duration = text.getLong("duration");
            if(duration != 0)
                rendezvousSet.add(new Rendezvous(mhubID, thingID, duration));
        }
        
        return rendezvousSet;
    }
    
    @Override
    public Set<Rendezvous> getDurationByThing(UUID thingID, long Q, long W) throws Exception {
        UUID mhubID = null;
        long arrive = 0;
        long depart = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        long delta = W - Q;
        
        //Get Average Duration
        url = HORYS
                + "duration/thing/"+thingID+"/"+W+"/"+delta;
        returnedJson = REST.sendGet(url, "GET");
        
        JSONArray data = new JSONArray(returnedJson);
        for(int i = 0; i < data.length(); i++){
            JSONObject text = data.getJSONObject(i);

            mhubID = UUID.fromString(text.getString("mhubID"));
            arrive = text.getLong("arrive");
            depart = text.getLong("depart");
            if(arrive != depart)
                rendezvousSet.add(new Rendezvous(mhubID, thingID, arrive, depart));
        }
        
        return rendezvousSet;
    }
    
    @Override
    public Set<Rendezvous> getDurationByMHub(UUID mhubID, long Q, long W) throws Exception {
        UUID thingID = null;
        long arrive = 0;
        long depart = 0;
        String url;
        String returnedJson;
        Set<Rendezvous> rendezvousSet = new HashSet<>();
        long delta = W - Q;
        
        //Get Average Duration
        url = HORYS
                + "duration/mhub/"+mhubID+"/"+W+"/"+delta;
        returnedJson = REST.sendGet(url, "GET");
        
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
}

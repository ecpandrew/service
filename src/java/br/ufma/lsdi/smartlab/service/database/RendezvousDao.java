package br.ufma.lsdi.smartlab.service.database;

import br.ufma.lsdi.smartlab.service.data.Rendezvous;
import java.util.Set;
import java.util.UUID;

public interface RendezvousDao {
    /**
     * Retrieve the rendezvous duration of a given thing to mobile hubs
     * @param thingID
     * @return a set of rendezvous duration
     * @throws java.lang.Exception
     */
    public Set<Rendezvous> getDurationByThing(UUID thingID) throws Exception;
    
    /**
     * Retrieve the rendezvous duration of a given mobile hub to things
     * @param mhubID
     * @return a set of rendezvous duration
     * @throws java.lang.Exception
     */
    public Set<Rendezvous> getDurationByMHub(UUID mhubID) throws Exception;
    
    /**
     * Retrieve the rendezvous duration of a given thing to mobile hubs
     * @param thingID
     * @param Q final timestamp
     * @param W delta
     * @return a set of rendezvous duration
     * @throws java.lang.Exception
     */
    // duration/thing/{thingID}/{W}/delta
    public Set<Rendezvous> getDurationByThing(UUID thingID, long Q, long W) throws Exception;
    
    /**
     * Retrieve the rendezvous duration of a given mobile hub to things
     * @param mhubID
     * @param Q final timestamp
     * @param W delta
     * @return a set of rendezvous duration
     * @throws java.lang.Exception
     */
    // duration/mhub/{mhubID}/{W}/{delta}
    public Set<Rendezvous> getDurationByMHub(UUID mhubID, long Q, long W) throws Exception;
    
}

package br.pucrio.inf.lac.hospital.semantic.wscore;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;

public class SimulatedhorizTest {
    public static void main(String[] args) throws JsonProcessingException {
        SimulatedhorizResource shr = new SimulatedhorizResource();
        
        String srtThingID = UUID.randomUUID().toString();
        
        System.out.println(shr.getAverageRendezvousDuration(srtThingID));
        System.out.println(shr.getConnectedMHubs(srtThingID));
    }
    
    
}

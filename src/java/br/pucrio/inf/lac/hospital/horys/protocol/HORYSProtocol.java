package br.pucrio.inf.lac.hospital.horys.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * HORYS API Protocol 0.1
 * 
 * ========================================
 * - mode: 1 (request) or 2 (reply)
 * - operation:  
 *    can be 0 = PUTRENDEZVOUS
 *           1 = GETCONNECTEDTHINGS
 *           2 = GETCONNECTEDMHUBS
 *           3 = GETAVGRENDEZVOUSDURATION
 * - parameters: set of k,v pairs
 *    { key : value }
 * =========================================
 *
 * @author Marcos Roriz <mroriz@inf.puc-rio.br>
 *
 */
public class HORYSProtocol implements Serializable {
    private Mode mode;
    private Operation operation;
    private Map<String, Object> parameters;
    
    // No-arg constructor for reflection
    public HORYSProtocol() {}
    
    public HORYSProtocol(Mode mode, Operation operation) {
        this(mode, operation, null);
    }

    public HORYSProtocol(Mode mode, Operation operation, Map<String, Object> parameters) {
        this.mode = mode;
        this.operation = operation;
        this.parameters = parameters;
    }
    
    //////////////////////////////////////////////////////////////////////////
    // Protocol Type and Operations
    //////////////////////////////////////////////////////////////////////////
    
    // Message Mode (Request or Reply)?
    public enum Mode { 
        REQUEST(1), 
        REPLY (2);
        
        private final int type; 
        
        private Mode(int type) {
            this.type = type;
        }
        
        public int getType() {
            return type;
        }
    }
    
    // Supported Operations
    public enum Operation {
        PUTRENDEZVOUS(0),
        GETCONNECTEDTHINGS(1),
        GETCONNECTEDMHUBS(2),
        GETAVGRENDEZVOUSDURATION(3);
        
        private final int opID;
        
        private Operation(int op) {
            this.opID = op;
        }
        
        public int getOpID() {
            return opID;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////
    // GET and SETS
    //////////////////////////////////////////////////////////////////////////

    public Mode getMode() {
        return mode;
    }


    public Operation getOperation() {
        return operation;
    }


    public Map<String, Object> getParameters() {
        return parameters;
    }


    public void setMode(Mode mode) {
        this.mode = mode;
    }


    public void setOperation(Operation operation) {
        this.operation = operation;
    }


    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}

package br.ufma.lsdi.smartlab.service.core;

import br.ufma.lsdi.smartlab.service.data.Device;
import br.ufma.lsdi.smartlab.service.data.GroupRendezvous;
import br.ufma.lsdi.smartlab.service.data.Person;
import br.ufma.lsdi.smartlab.service.data.Rendezvous;
import br.ufma.lsdi.smartlab.service.data.PhysicalSpace;
import br.ufma.lsdi.smartlab.service.database.ServiceDao;
//import br.ufma.lsdi.smartlab.service.database.ServiceDaoImpMariaDB;
import br.ufma.lsdi.smartlab.service.database.ServiceDaoImpSemantic;
import br.ufma.lsdi.smartlab.service.database.RendezvousDao;
import br.ufma.lsdi.smartlab.service.database.RendezvousDaoImpHorys;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 * REST Web Service
 *
 * @author carvalhodaniels
 */
@Path("/")
public class ServiceResource {

    private static ServiceDao dao = new ServiceDaoImpSemantic();
    private static RendezvousDao rendezvous = new RendezvousDaoImpHorys();
    
    private ObjectMapper mapper;
    
    /** The Service Mode: */
    /** 0: Room has a beacon, Person has a mhub */
    /** 1: Room has a mhub, Person has a beacon */
    private final Integer MODE = 0;


    /**
     * Creates a new instance of SemanticResource
     */
    public ServiceResource() {
        mapper = new ObjectMapper();
    }

    /**
     * Retrieves representation of an instance of
 br.pucrio.inf.lac.hospital.semanticws.core.ServiceResource
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
                + "  \"duration\" : 3600\n"
                + "}";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("physical_spaces/{ids: .*}/persons/")
    public Response getPersonByPhysicalSpace(@PathParam("ids") List<PathSegment> ids) throws Exception {
        Set<PhysicalSpace> physicalSpaceGroup = new HashSet<>();
        for (PathSegment id: ids) {
            PhysicalSpace r = dao.getPhysicalSpace(Long.parseLong(id.getPath()));
            if(r == null) Response.status(404).build();
            physicalSpaceGroup.add(r);
        }
        
        String returnJson = "[";
        for (PhysicalSpace r: physicalSpaceGroup){
            Set<Device> deviceSet;
            if(MODE == 0){
                deviceSet = dao.getThingsByRoom(r.getRoomID());
            }else{
                deviceSet = dao.getMHubsByRoom(r.getRoomID());
            }

            Set<Rendezvous> rendezvousSet = null;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = rendezvous.getDurationByThing(d.getUuID());  //duration/thing/{thingID}
                }else if(MODE == 1){
                    rendezvousSet = rendezvous.getDurationByMHub(d.getUuID());   //duration/mhub/{mhubID}
                }
                if(rendezvousSet == null || rendezvousSet.isEmpty()){
                        returnJson += "";
                }else{
                    for (Rendezvous re: rendezvousSet) {
                        Person p;
                        PhysicalSpace r2;
                        if(MODE == 0){
                            p = dao.getPersonByMHub(re.getMhubID());
                            r2 = dao.getPhysicalSpaceByThing(re.getThingID());
                        }else{
                            p = dao.getPersonByThing(re.getThingID());
                            r2 = dao.getPhysicalSpaceByMHub(re.getMhubID());
                        }
                        
                        if(p != null){

                        returnJson += "{\"shortName\": \"" + p.getShortName() + "\", "
                                + "\"email\": \"" + p.getPersonEmail() + "\", "
                                + "\"physical_space\": \"" + r2.getRoomName() + "\", "
                                + "\"duration\": " + re.getDuration() + "}, ";
                        }
                    }
                }
            }
        }
        //Removes the last coma and space
        if(returnJson.length() > 2)
            returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return Response.ok(returnJson).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("physical_spaces/{ids: .*}/persons/{Q}/{W}/")
    public Response getPersonByPhysicalSpaceAndTime(@PathParam("ids") List<PathSegment> ids, @PathParam("Q") long Q, @PathParam("W") long W) throws Exception {
        Set<PhysicalSpace> physicalSpaceGroup = new HashSet<>();
        for (PathSegment id: ids) {
            PhysicalSpace r = dao.getPhysicalSpace(Long.parseLong(id.getPath()));
            if(r == null) Response.status(404).build();
            physicalSpaceGroup.add(r);
        }
        
        String returnJson = "[";
        for (PhysicalSpace r: physicalSpaceGroup){
            Set<Device> deviceSet;
            if(MODE == 0){
                deviceSet = dao.getThingsByRoom(r.getRoomID());
            }else{
                deviceSet = dao.getMHubsByRoom(r.getRoomID());
            }

            Set<Rendezvous> rendezvousSet = null;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = rendezvous.getDurationByThing(d.getUuID(), Q, W);  //duration/thing/{thingID}/{W}/{delta}
                }else if(MODE == 1){
                    rendezvousSet = rendezvous.getDurationByMHub(d.getUuID(), Q, W);   //duration/mhub/{mhubID}/{W}/{delta}
                }
                if(rendezvousSet == null || rendezvousSet.isEmpty()){
                        returnJson += "";
                }else{
                    for (Rendezvous re: rendezvousSet) {
                        Person p;
                        PhysicalSpace r2;
                        if(MODE == 0){
                            p = dao.getPersonByMHub(re.getMhubID());
                            r2 = dao.getPhysicalSpaceByThing(re.getThingID());
                        }else{
                            p = dao.getPersonByThing(re.getThingID());
                            r2 = dao.getPhysicalSpaceByMHub(re.getMhubID());
                        }
                        //HasA h = dao.getHasAByDevice(d2.getDeviceID());
                        //Person p = dao.getPerson(h.getPersonID());
                        if(p != null){

                        returnJson += "{\"shortName\": \"" + p.getShortName() + "\", "
                                + "\"email\": \"" + p.getPersonEmail() + "\", "
                                + "\"physical_space\": \"" + r2.getRoomName() + "\", "
                                + "\"arrive\": " + re.getArrive() + ", "
                                + "\"depart\": " + re.getDepart() + "}, ";
                        }
                    }
                }
            }
        }
        //Removes the last coma and space
        if(returnJson.length() > 2)
            returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return Response.ok(returnJson).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persons/{ids: .*}/rendezvous/{Q}/{W}/")
    public Response getPersonRendezvous(@PathParam("ids") List<PathSegment> ids, @PathParam("Q") long Q, @PathParam("W") long W) throws Exception {
        boolean flag = false;
        Set<Person> personGroup = new HashSet<>();
        Set<Rendezvous> reSet = new HashSet<>();
        ArrayList<GroupRendezvous> gReSet = new ArrayList<>();
        for (PathSegment id: ids) {
            Person p = dao.getPerson(Long.parseLong(id.getPath()));
            if(p == null) return Response.status(404).build();
            personGroup.add(p);
        }
        if(personGroup.size() < 2 || personGroup.size() != ids.size()) return Response.status(404).build();
        for (Person p: personGroup){
            //Set<HasA> hasASet = dao.getHasAByPerson(p.getPersonID());
            Set<Device> deviceSet;
            if(MODE == 0){
                deviceSet = dao.getMHubsByPerson(p.getPersonID());
            }else{
                deviceSet = dao.getThingsByPerson(p.getPersonID());
            }

            Set<Rendezvous> rendezvousSet;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = rendezvous.getDurationByMHub(d.getUuID(), Q, W);   //duration/thing/{thingID}/{W}/{delta}
                }else{
                    rendezvousSet = rendezvous.getDurationByThing(d.getUuID(), Q, W);  //duration/mhub/{mhubID}/{W}/{delta}
                }
                if(rendezvousSet != null && !rendezvousSet.isEmpty()){
                    for (Rendezvous re: rendezvousSet) {
                        PhysicalSpace r;
                        if(MODE == 0){
                            r = dao.getPhysicalSpaceByThing(re.getThingID());
                        }else{
                            r = dao.getPhysicalSpaceByMHub(re.getMhubID());
                        }

                        reSet.add(new Rendezvous(p.getShortName(), r.getRoomName(), re.getArrive(), re.getDepart()));
                    }
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
        if(gReSet.isEmpty()) return Response.ok("[]").build();

        String returnJson = "[";
        //String returnJson = "{ "
        //        + "\"rendezvous\": "
        //        + "[";
        for (GroupRendezvous gr : gReSet) {
            returnJson += "{\"physical_space\": \"" + gr.getRoomName() + "\", ";
            returnJson += "\"persons\": [";
            for (String s : gr.getPersonGroup()) {
                returnJson += "{\"shortName\": \"" + s + "\"}, ";
            }
            returnJson = returnJson.substring(0, returnJson.length() - 2);
            returnJson += "], ";
            returnJson += "\"arrive\": " + gr.getArrive() + ", ";
            returnJson += "\"depart\": " + gr.getDepart() + "}, ";
        }
         //Removes the last coma and space
        returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        return Response.ok(returnJson).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persons/{ids: .*}/physical_spaces/")
    public Response getPhysicalSpaceByPerson(@PathParam("ids") List<PathSegment> ids) throws Exception {
        Set<Person> personGroup = new HashSet<>();
        for (PathSegment id: ids) {
            Person p = dao.getPerson(Long.parseLong(id.getPath()));
            if(p == null) Response.status(404).build();
            personGroup.add(p);
        }
        //if(personGroup.size() < 2 || personGroup.size() != ids.size()) return "[]";
        
        String returnJson = "[";
        for (Person p: personGroup){
            //Set<HasA> hasASet = dao.getHasAByPerson(p.getPersonID());
            Set<Device> deviceSet;
            if(MODE == 0){
                deviceSet = dao.getMHubsByPerson(p.getPersonID());
            }else{
                deviceSet = dao.getThingsByPerson(p.getPersonID());
            }

            Set<Rendezvous> rendezvousSet;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = rendezvous.getDurationByMHub(d.getUuID());   //duration/mhub/{mhubID}
                }else{
                    rendezvousSet = rendezvous.getDurationByThing(d.getUuID());  //duration/thing/{thingID}
                }
                if(rendezvousSet == null || rendezvousSet.isEmpty()){
                    returnJson += "";
                }else{
                    for (Rendezvous re: rendezvousSet) {
                        PhysicalSpace r;
                        if(MODE == 0){
                            r = dao.getPhysicalSpaceByThing(re.getThingID());
                        }else{
                            r = dao.getPhysicalSpaceByMHub(re.getMhubID());
                        }
                        if(r != null){

                        returnJson += "{\"shortName\": \"" + p.getShortName() + "\", "
                            + "\"physical_space\": \"" + r.getRoomName() + "\", "
                            + "\"description\": \"" + r.getRoomDescription() + "\", "
                            + "\"duration\": " + re.getDuration() + "}, ";
                        }
                    }
                }
            }
        }
        if(returnJson.length() > 2)
            returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        
        return Response.ok(returnJson).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("persons/{ids: .*}/physical_spaces/{Q}/{W}/")
    public Response getPhysicalSpaceByPersonAndTime(@PathParam("ids") List<PathSegment> ids, @PathParam("Q") long Q, @PathParam("W") long W) throws Exception {
        Set<Person> personGroup = new HashSet<>();
        for (PathSegment id: ids) {
            Person p = dao.getPerson(Long.parseLong(id.getPath()));
            if(p == null) Response.status(404).build();
            personGroup.add(p);
        }
        //if(personGroup.size() < 2 || personGroup.size() != ids.size()) return "[]";
        
        String returnJson = "[";
        for (Person p: personGroup){
            Set<Device> deviceSet;
            if(MODE == 0){
                deviceSet = dao.getMHubsByPerson(p.getPersonID());
            }else{
                deviceSet = dao.getThingsByPerson(p.getPersonID());
            }

            Set<Rendezvous> rendezvousSet;
            for (Device d : deviceSet) {
                if(MODE == 0){
                    rendezvousSet = rendezvous.getDurationByMHub(d.getUuID(), Q, W);   //duration/thing/{thingID}/{W}/{delta}
                }else{
                    rendezvousSet = rendezvous.getDurationByThing(d.getUuID(), Q, W);  //duration/mhub/{mhubID}/{W}/{delta}
                }
                if(rendezvousSet == null || rendezvousSet.isEmpty()){
                    returnJson += "";
                }else{
                    for (Rendezvous re: rendezvousSet) {
                        PhysicalSpace r;
                        if(MODE == 0){
                            r = dao.getPhysicalSpaceByThing(re.getThingID());
                        }else{
                            r = dao.getPhysicalSpaceByMHub(re.getMhubID());
                        }
                        if(r != null){

                        returnJson += "{\"shortName\": \"" + p.getShortName() + "\", "
                            + "\"physical_space\": \"" + r.getRoomName() + "\", "
                            + "\"description\": \"" + r.getRoomDescription() + "\", "
                            + "\"arrive\": " + re.getArrive() + ", "
                            + "\"depart\": " + re.getDepart() + "}, ";
                        }
                    }
                }
            }
        }
        if(returnJson.length() > 2)
            returnJson = returnJson.substring(0, returnJson.length() - 2);
        returnJson += "]";
        
        return Response.ok(returnJson).build();
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

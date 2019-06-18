package br.pucrio.inf.lac.hospital.semantic.wscore;

import br.ufma.lsdi.smartlab.service.core.ServiceResource;

public class SemanticResourceTest {
    public static void main(String[] args) throws Exception {
        ServiceResource sr = new ServiceResource();
        System.out.println(sr.getHospitalByCity("city"));
        System.out.println(sr.getHospitalByID(1));
        System.out.println(sr.getHospitalByInsuraceAndCity(1, "city"));
        System.out.println(sr.getHospitalBySpeciatyAndCity(1, "city"));
        System.out.println(sr.getHospitalBySpeciatyAndCityAndInsurance(1, 1, "city"));
        System.out.println(sr.getHospitalTop5());
        System.out.println(sr.getHospitalTop5BySpeciatyAndCity(1, "city"));
    }
    
}

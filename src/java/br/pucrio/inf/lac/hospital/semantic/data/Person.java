package br.pucrio.inf.lac.hospital.semantic.data;

public class Person {

    private long personID;
    private String personName;
    private String personEmail;

    public Person() {
    }

    public Person(long personID, String personName, String personEmail) {
        this.personID = personID;
        this.personName = personName;
        this.personEmail = personEmail;
    }

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }
    
    @Override
    public String toString() {
        return "Person{" + "personID=" + personID + ", personName=" + personName + ", personEmail=" + personEmail + '}';
    }
    
}

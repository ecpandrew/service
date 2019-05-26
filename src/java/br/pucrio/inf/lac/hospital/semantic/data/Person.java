package br.pucrio.inf.lac.hospital.semantic.data;

public class Person {

    private long personID;
    private String shortName;
    private String personEmail;
    private String fullName;

    public Person() {
    }

    public Person(long personID, String shortName, String personEmail) {
        this.personID = personID;
        this.shortName = shortName;
        this.personEmail = personEmail;
    }
    
    public Person(long personID, String shortName, String personEmail, String fullName) {
        this.personID = personID;
        this.shortName = shortName;
        this.personEmail = personEmail;
        this.fullName = fullName;
    }

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override
    public String toString() {
        return "Person{" + "personID=" + personID + ", shortName=" + shortName + ", personEmail=" + personEmail + '}';
    }
    
}

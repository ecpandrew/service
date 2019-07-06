package br.ufma.lsdi.smartlab.service.data;

public class Person {

    private long personID;
    private String shortName;
    private String personEmail;
    private String fullName;
    private String roles;

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
    public Person(long personID, String shortName, String personEmail, String fullName, String roles) {
        this.personID = personID;
        this.shortName = shortName;
        this.personEmail = personEmail;
        this.fullName = fullName;
        this.roles = roles;
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

    public void setRoles(String roles) { this.roles = roles; }

    public String getRoles() { return roles; }

    @Override
    public String toString() {
        return "Person{" + "personID=" + personID + ", shortName=" + shortName + ", personEmail=" + personEmail + '}';
    }
    
}

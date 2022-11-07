public class Person {
    //String value for jobType of Person
    DatabaseManager databaseManager;
    private String jobType;

    // String value for name of Person
    private String name;

    // String value for telNumber of Person
    private String telNumber;

    // Value for table name
    private String tableName;

    // String value for email address of Person
    private String emailAddress;

    //String value for physical address of Person
    private String physicalAddress;

    // Constructor
    public Person(String jobType, String name, String telNumber, String emailAddress,
                  String physicalAddress) {
        this.jobType = jobType;
        this.name = name;
        tableName = jobType + " ";
        this.telNumber = telNumber;
        this.emailAddress = emailAddress;
        this.physicalAddress = physicalAddress;
    }
    //The method returns the name of the Person object.
    public String getName() {
        return name;
    }

    //The method returns the job type of the Person object.
    public String getJobType() {
        return jobType;
    }

    //The method returns the tel number of the Person object.
    public String getTelNumber() {
        return telNumber;
    }

    //The method returns the email address of the Person object.
    public String getEmailAddress() {
        return emailAddress;
    }

    //The method returns the physical of the Person object.
    public String getPhysicalAddress() {
        return physicalAddress;
    }
    //The method sets the tel number of the Person object to
    public void setTelNumber(String newTelNumber) {
        databaseManager.updatePerson(tableName, name, "Tel", newTelNumber);
        this.telNumber = newTelNumber;
    }
    //The method sets the email address of the Person object to a new given value.
    public void setEmailAddress(String newEmailAddress) {
        databaseManager.updatePerson(tableName, name, "Email", newEmailAddress);
        this.emailAddress = newEmailAddress;
    }

    public void setPhysicalAddress(String newAddress) {
        databaseManager.updatePerson(tableName, name, "Address", newAddress);
        this.physicalAddress = newAddress;
    }
    public void setName(String newName) {
        databaseManager.updatePersonName(tableName, name, this, newName);
        this.name = newName;
    }
}

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.io.File;
public class Project {

    //Person object for the architect of the project
    private Person architect;

    // Person object for contractor of the project
    private Person contractor;

    //Person object for the customer of the project
    private Person customer;

    //value for project number
    private String projectNumber;

    //String value for name of project
    private String name;

    //String value for Building Type of project
    private String buildingType;

    //String value for address of project
    private String address;

    //String value for ERF Number of project
    private String erfNumber;

    //LocalDate value for deadline of project
    private LocalDate deadline;

    //LocalDate value for completion date of project
    private LocalDate completionDate;

    //Float value for total fees of project
    private float totalProjectFees;

    //Float value for total fees paid of project
    private float amountPaidToDate;

    //Boolean value to show if project is finalised
    private boolean isFinalised;
    private String completionDateOnly;
    private boolean finalisedOnly;


    //Constructor
    public Project(String projectNumber, String name, String buildingType,
                   String address, String erfNumber, float totalProjectFees,
                   float amountPaidToDate, LocalDate deadline, Person architect,
                   Person contractor, Person customer) {

        this.projectNumber = projectNumber;
        this.name = name;
        this.setBuildingType(buildingType);
        this.setAddress(address);
        this.setErfNumber(erfNumber);
        this.totalProjectFees = totalProjectFees;
        this.deadline = deadline;
        this.amountPaidToDate = amountPaidToDate;
        this.architect = architect;
        this.contractor = contractor;
        this.customer = customer;
    }


    //The method finalises the project by changing the isFinalised
    public void finaliseProject() {
        setCompletionDate(LocalDate.now());
        if(getTotalProjectFees() - getAmountPaidToDate() > 0) {
            generateInvoice();
        }
        setFinalised(true);
        System.out.println("Project finalised.");
    }

    //Generate invoice to be saved to text file.
    public void generateInvoice() {
        String invoiceDirectory = "invoices";
        float amountToBePaid = getTotalProjectFees() - getAmountPaidToDate();
        FileWriter fileWriter = null;
        if(amountToBePaid > 0) {
            // Create Invoice.
            String invoice = "Customer: " + getCustomer().getName();
            invoice += "\nTel number: " + getCustomer().getTelNumber();
            invoice += "\nEmail: " + getCustomer().getEmailAddress();
            invoice += "\nCompletion Date: " + getCompletionDate();
            invoice += "\n\nTotal amount to be paid: R" + amountToBePaid;
            System.out.println(invoice);
            try {
                // Create file and save invoice to file
                new File(invoiceDirectory).mkdirs();
                invoiceDirectory += "\\";
                invoiceDirectory += name + ".txt";
                File invoiceFile = new File(invoiceDirectory);
                fileWriter = new FileWriter(invoiceFile);
                fileWriter.write(invoice + "\n");

            } catch (NullPointerException | IOException e) {
                System.out.println("File error ");
                e.printStackTrace();
            } finally {
                if(fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        System.out.println("File error.");
                    }
                }
            }
        }
    }

    //Print data to console in readable format
    public String toString() {
        return  ("-----------------------------------------------------------------------------"
                + "\nProject name: " + name
                + "\nProject number: " + projectNumber
                + "\nBuilding type: " + getBuildingType()
                + "\nAddress: " + getAddress()
                + "\nERF Number: " + getErfNumber()
                + "\nDue date: " + deadline
                + "\nArchitect: " + getArchitect().getName()
                + "\n\tTel number:" + getArchitect().getTelNumber()
                + "\n\tEmail:" + getArchitect().getEmailAddress()
                + "\nContractor: " + contractor.getName()
                + "\n\tTel number:" + contractor.getTelNumber()
                + "\n\tEmail:" + contractor.getEmailAddress()
                + "\nCustomer: " + getCustomer().getName()
                + "\n\tTel number:" + getCustomer().getTelNumber()
                + "\n\tEmail:" + getCustomer().getEmailAddress()
                + "\nTotal project fee: R" + getTotalProjectFees()
                + "\nTotal paid to date: R" + getAmountPaidToDate()
                + "\nProject finalised: " + isFinalised
                + "\nCompletion date: " + getCompletionDate()
                + "\n--------------------------------------------------------------------------------");
    }


    //The method returns the name of the Project object.
    public String getName() { return name; }

    // The method returns the project number of the Project object.
    public String getProjectNumber() { return projectNumber;}

    //The method returns the boolean value for the isFinalised  attribute
    public boolean getIsFinalised() {
        return isFinalised;
    }

    //The method returns the deadline of the Project object.
    public LocalDate getDeadline() {return deadline;}

    //The method returns the contractor for the Project object.
    public Person getContractor() { return contractor; }


    // The method returns the architect for the Project object.
    public Person getArchitect() { return architect; }


    //The method returns the customer of the Project object.
    public Person getCustomer() { return customer; }

    //The method returns the building type of the Project object.
    public String getBuildingType() { return buildingType; }


    //The method returns the address of the Project object.
    public String getAddress() { return address; }

    //The method returns the ERF number of the Project object.
    public String getErfNumber() { return erfNumber; }


    //The method returns the completion date of the Project object.
    public LocalDate getCompletionDate() { return completionDate; }


    //The method returns the total fee of the Project object.
    public float getTotalProjectFees() { return totalProjectFees; }


    //The method returns the total amount paid to date
    public float getAmountPaidToDate() { return amountPaidToDate; }


    //The method sets the amount paid to date of the Project object
    public void setAmountPaidToDate(float newAmount) {
        this.amountPaidToDate = newAmount;
    }

    //The method sets the Project number to a new given number.
    public void setProjectNumber(String number){
        this.projectNumber = number;
    }
    public void setDeadline(LocalDate newDeadline) {
        this.deadline = newDeadline;
    }

    //The method sets the amount paid to date of the Project object to a new given value.
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    // The method sets the amount paid to date of the Project object to a new given value.
    public void setFinalised(boolean finalised) {
        this.isFinalised = finalised;
    }

    //The method sets the Building type of the Project object to a new given building type.
    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }


    //The method sets theAddress of the Project object to a new Address.
    public void setAddress(String address) {
        this.address = address;
    }
    public void setErfNumber(String erfNumber) {
        this.erfNumber = erfNumber;
    }
    public void setCompletionDateOnly(String completionDateOnly) {
        this.completionDateOnly = completionDateOnly;
    }

    public String getCompletionDateOnly() {

        return completionDateOnly;
    }

    public void setFinalisedOnly(boolean finalisedOnly) {
        this.finalisedOnly = finalisedOnly;
    }

    public boolean isFinalisedOnly() {
        return finalisedOnly;
    }
}
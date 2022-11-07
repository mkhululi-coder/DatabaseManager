import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 
import java.util.Scanner;
import java.sql.*;

public class ProjectManager {

    //Class array variable to store all projects
    //List of Project objects that stores all projects
    static List<Project> allProjects = new ArrayList<>();
    static DatabaseManager databaseManager;

    //List of Project objects that stores all incomplete projects
    static List<Project> incompleteProjects = new ArrayList<>();
    //List of Project objects that stores all overdue projects
    static List<Project> overDueProjects = new ArrayList<>();
    //static Statement statement;
    private static ResultSet result;

    //Main loop allows user to view or add a project.
    //When a user chooses to add a project they will be asked
    //to input all necessary information for the creation of a Project() object.


    public static void main(String[] args) {
        //Objects
        Scanner input = new Scanner(System.in);
        databaseManager = DatabaseManager.getInstance();

        //Variables
        String menuOption = " ";

        //retrieveProjects();

        while(!menuOption.equals("0")) {

            databaseManager.SignIn("root", "50801202");
            System.out.println("User successfully logged into database!");


            int selectedProject;

            System.out.println("""
              Project Manager
              1: View All Projects
              2: View Incomplete Projects
              3: View Late Projects
              4: Search for Project
              5: Add Project
              0: Quit""");

            menuOption = input.next();
            input.nextLine();

            // View project
            if (menuOption.equals("1")) {
                // View all Tasks
                while (true) {

                    while (true) {
                        try {
                            // Allow user ot select a task.
                            // Create connection
                            Statement statement = DatabaseManager.connection();
                            System.out.println();
                            System.out.println("Please choose a project(Type '0'to go back):");
                            retrieveProjects();
                            System.out.println();
                            selectedProject = Integer.parseInt(input.nextLine()) - 1;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("No option selected!");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(selectedProject == -1) {
                        break;
                    }

                    processProject(input, allProjects.get(selectedProject));
                }
            } else if (menuOption.equals("2")){
                // View Incomplete Tasks
                while (true) {
                    while (true) {
                        try {
                            int index = 1;
                            System.out.println("All Incomplete Projects: \n");
                            System.out.println("Please choose a project(Type '0'to go back):");
                            for (Project project : allProjects) {
                                if (project != null && !project.getIsFinalised()) {
                                    System.out.println(index + " " + project.getName());
                                    incompleteProjects.add(project);
                                    index++;
                                }
                            }
                            System.out.println();
                            selectedProject = Integer.parseInt(input.nextLine()) - 1;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("No option selected!");
                        }
                    }
                    if (selectedProject == -1) {
                        break;
                    }
                    processProject(input,incompleteProjects.get(selectedProject));
                }
            } else if (menuOption.equals("3")) {
                // View Overdue Tasks
                while (true) {
                    while (true) {
                        try {
                            int index = 1;
                            System.out.println("All Overdue Projects: \n");
                            System.out.println("Please choose a project(Type '0'to go back):");
                            for (Project project : allProjects) {
                                if (project != null
                                        && Date.valueOf(LocalDate.now()).after(
                                        Date.valueOf(project.getDeadline()))) {
                                    System.out.println(index + " " + project.getName());
                                    overDueProjects.add(project);
                                    index++;
                                }
                            }
                            System.out.println();
                            selectedProject = Integer.parseInt(input.nextLine()) - 1;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("No option selected!");
                        }
                    }
                    if (selectedProject == -1) {
                        break;
                    }
                    processProject(input,overDueProjects.get(selectedProject));
                }
            } else if (menuOption.equals("4")){
                // Search project by Name or Number
                Project searchedProject = searchProject(input, allProjects);
                if (searchedProject != null) {
                    processProject(input, searchedProject);
                } else {
                    System.out.println("No Project Found!");
                }

            } else if (menuOption.equals("5")) {
                // Add project
                allProjects.add(createProject());
                System.out.println("Added project: "+ allProjects.get(allProjects.size()-1).getName());
            }
        }
        input.close();
    }

    //The method retrieves all the projects and there data from a text file

    private static void retrieveProjects() throws SQLException {
        List<Project>projectList= new ArrayList<>();

        Statement statement = DatabaseManager.connection();
        //Retrieve all projects from text file.
        Scanner fileScanner = null;
        try {
            result = statement.executeQuery("select * from projects");
            while (result.next()) {
                System.out.println("\nNumber " +
                        result.getString("Name") + "\n "
                        + result.getString("Address")
                        + result.getString("BuildingType")
                        + result.getFloat("TotalFee")
                        + result.getFloat("PaidtoDate")
                        + result.getDate("Deadline")
                        + result.getDate("CompletionDate")
                        + result.getString("ContractorName")
                        + result.getString("ArchitectName")
                        + result.getString("c")
                        + result.getString("Finalised"));
                String projectNumber = result.getString("Number");
                String projectName = result.getString("Name");
                String BuildingType = result.getString("BuildingType");
                Float TotalFee = result.getFloat("TotalFee");
                Float PaidToDate = result.getFloat("paidToDate");
                Date Deadline = result.getDate("deadline");
                Date CompletionDate = result.getDate("completionDate");
                String ContractorName = result.getString("contractorName");
                String ArchitectName = result.getString("architectName");
                String CustomerName = result.getString("customerName");
                String Finalised = result.getString("Finalised");


                System.out.println(projectNumber + " " + projectName);

            }

        }catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
    //The method saves all the projects and data

    //The method searches for a project by name or number

    private static Project searchProject(Scanner input, List<Project> allProjects) {
        System.out.println("Enter the project name or number: ");
        String userInput = input.nextLine();
        // Check if user entered project exists and return project
        for (Project project : allProjects) {
            if (project != null && (userInput.equals(project.getName())
                    || userInput.equals(project.getProjectNumber()))) {
                return project;
            }
        }
        return null;
    }


    //The method allows the user to edit a chosen project
    //Allows then to change a range of attributes of the project

    private static void processProject(Scanner input, Project project) {
        while (project != null) {
            // Allow user to edit information of the selected task.
            System.out.println("What would you like to edit on the project "
                    + project.getName()
                    + "\n1: Due date.\n2: Total paid to date."
                    + "\n3: Number \n4: ERF Number"
                    + "\n5: Building type \n6: Address"
                    + "\n7: Contractors contact details."
                    + "\n8: Architect contact details."
                    + "\n9: Customer contact details.\n"
                    + "final : Finalise project.\n0: Go back.");
            String userInput = input.next().toLowerCase();
            input.nextLine();

            switch (userInput) {
                case "1" -> project.setDeadline(changeDueDate(input));
                case "2" -> changeAmountPaid(input, project);
                case "3" -> {
                    System.out.println("Enter new project number: \n");
                    project.setProjectNumber(input.next());
                }
                case "4" -> {
                    System.out.println("Enter new ERF number: \n");
                    project.setErfNumber(input.next());
                }
                case "5" -> {
                    System.out.println("Enter new Building type: \n");
                    project.setBuildingType(input.next());
                }
                case "6" -> project.setAddress(input.next());
                case "7" -> changePersonContacts(input, project.getContractor());
                case "8" -> changePersonContacts(input, project.getArchitect());
                case "9" -> changePersonContacts(input, project.getCustomer());
                case "final" -> {
                    project.finaliseProject();
                    userInput = "0";
                }
            }
            if (userInput.equals("0")) {
                break;
            }

        }
    }


    //The method allows the user to edit the contact details

    private static void changePersonContacts(Scanner input, Person person) {
        // Request new contact information from user and update
        // information of Person object.
        System.out.println("Enter new phone number:");
        String number = input.next();
        input.nextLine();
        System.out.println("Enter new Email address:");
        String email = input.next();
        input.nextLine();
        person.setTelNumber(number);
        person.setEmailAddress(email);
        System.out.println(person.getJobType() + " details updated!");
    }

    // The method allows the user to edit the amount paid of the project

    private static void changeAmountPaid(Scanner input, Project project) {
        // Request new Total amount from user and update
        // Project object Amount paid.
        while (true) {
            try {
                System.out.println("Please enter new total amount paid:");
                float amount = Float.parseFloat(input.nextLine());
                project.setAmountPaidToDate(amount);
                System.out.println("Amount paid to date updated!");
                break;
            } catch (NullPointerException | NumberFormatException e) {
                System.out.println("Please enter number only.");
            }
        }
    }


    // The method allows the user to edit the deadline of the project

    private static LocalDate changeDueDate(Scanner input) {
        //Request new Due date from user and update
        //due date of project object.
        LocalDate deadline;
        String[] dateArr;
        String dateStr;

        while (true) {
            int year;
            int month;
            int day;

            System.out.println("Enter the project deadline(YYYY/MM/DD): ");
            dateStr = input.nextLine();
            dateStr = dateStr.replace(" ", "/");
            dateArr = dateStr.split("/");

            try {
                year = Integer.parseInt(dateArr[0]);
                month = Integer.parseInt(dateArr[1]);
                day = Integer.parseInt(dateArr[2]);
                deadline = LocalDate.of(year, month, day);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Date format is incorrect.");
            }
        }
        return deadline;
    }

    public static Project createProject() {
        //Method that requests all necessary information from user to create a
        //project object.

        //Objects
        Scanner input = new Scanner(System.in);
        Person architect = null;
        Person contractor = null;
        Person customer = null;
        LocalDate deadline;

        //Variables
        String projectNumber;
        String name;
        String buildingType;
        String address;
        String erfNumber;
        float totalProjectFees;
        float amountPaidToDate;

        while (true) {
            boolean exists = false;
            System.out.println("Enter the project number: ");
            projectNumber = input.nextLine();
            for (Project project : allProjects) {
                if (projectNumber.equals(project.getProjectNumber())) {
                    exists = true;
                    System.out.println("Number in use.");
                    break;
                } else {
                    exists = false;
                }
            }
            if (!exists) {
                break;
            }
        }

        while (true) {
            boolean exists = false;
            System.out.println("Enter the project name: ");
            name = input.nextLine();

            for (Project project : allProjects) {
                if (name.equals(project.getName())) {
                    exists = true;
                    System.out.println("Project name retrieved.");
                    break;
                } else {
                    exists = false;
                }
            }
            if (!exists) {
                break;
            }
        }
        System.out.println("Enter the building type: ");
        buildingType = input.nextLine();
        drawLine();
        while (true) {
            try {
                System.out.println("Enter the project address: ");
                address = input.nextLine();
                drawLine();
                System.out.println("Enter the project ERF number: ");
                erfNumber = input.nextLine();
                drawLine();
                databaseManager.addERFNumber(address, erfNumber);
                break;
            } catch (SQLException e) {
                System.out.println("Address already exists.");
            }
        }


        //method to prevent the code from crashing
        while (true) {
            try {
                System.out.println("Enter the project total cost: ");
                totalProjectFees = Float.parseFloat(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only!");
            }
        }
        while (true) {
            try {
                System.out.println("Enter the amount paid to date: ");
                amountPaidToDate = Float.parseFloat(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only!");
            }
        }

        deadline = changeDueDate(input);

        // for loop to build 3 Person objects an Architect, contractor and a customer.
        for(int i = 0; i < 3; i++) {
            switch (i) {
                case 0 -> {
                    architect = createPerson("Architect");
                }
                case 1 -> {
                    contractor = createPerson("Contractor");
                }
                case 2 -> {
                    customer = createPerson("Customer");
                }
            }
        }

        //If no name is entered one is created.
        if(name.equals("")) {
            String[] splitName = customer.getName().split(" ");
            if(splitName.length != 1) {
                name = buildingType + " " + splitName[splitName.length -1];
            } else{
                name = buildingType + " " + splitName[0];
            }
        }

        databaseManager.addProject(projectNumber, name, buildingType, address,
                totalProjectFees, amountPaidToDate, String.valueOf(deadline), null,  contractor.getName(),
                architect.getName(), customer.getName());

        return new Project(projectNumber, name, buildingType, address, erfNumber,
                totalProjectFees, amountPaidToDate, deadline, architect,
                contractor, customer);

    }

    private static Person createPerson(String jobType) {

        String personName;
        String telNumber;
        String emailAddress;
        String physicalAddress;

        Scanner personInput = new Scanner(System.in);

        System.out.println("Enter the name of the " + jobType + ": ");
        personName = personInput.nextLine();
        while (true) {
            System.out.println("Enter the tel number of the " + jobType + ": ");
            telNumber = personInput.nextLine();
            telNumber = telNumber.replace(" ", "");
            telNumber = telNumber.strip();
            if (telNumber.length() == 10) {

                break;
            } else {
                System.out.println("A tel number may only have 10 characters.");
            }
        }

        while (true) {
            System.out.println("Enter the email address of the " + jobType + ": ");
            emailAddress = personInput.nextLine();
            if (!emailAddress.contains("@")) {
                System.out.println("Email format incorrect. No @ symbol detected.");
            } else {
                break;
            }
        }

        System.out.println("Enter the physical address of the " + jobType + ": ");
        physicalAddress = personInput.nextLine();

        return new Person(jobType, personName, telNumber, emailAddress,
                physicalAddress);
    }

    public static void printAllFromTable(Statement statement)throws
            SQLException{
        ResultSet results = statement.executeQuery("select * from projects ");
        while(results.next()){
            System.out.println(
                    results.getString("Name") + ", "
                            + results.getString("building type") + ", "
                            + results.getString("address") + " ,"
            );

        }
    }


    // Draws line to console
    public static void drawLine(){
        for(int i = 0; i < 120; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

}


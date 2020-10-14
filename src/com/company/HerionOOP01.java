package com.company;

// Brock Herion
// 10/01/2020
// OOP assignment
// Developed in Intellij Idea on MacOS

/*
 * I haven't changed anything on here from week 3, as I had already implemented the OOP requirements
 * in week 2 and left them in in week 3
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class HerionOOP01 {
    // Constant variables we can use when we are starting the app
    // and validating option 4
    public static final String DEFAULT_NAME = "";
    public static final Year DEFAULT_YEAR = Year.NONE;
    public static final double DEFAULT_GPA = -1.0;
    public static final Student DEFAULT_STUDENT = new Student(DEFAULT_NAME, DEFAULT_YEAR, DEFAULT_GPA);

    // Declare initial currentStudent. I set them to these values to easily see
    // if they have had input or not
    public static Student currentStudent = DEFAULT_STUDENT;
    public static ArrayList<Student> currentStudents = new ArrayList<>();

    public static void main(String[] args) {
        // Boolean that controls if the application is running or not
        boolean isRunning = true;

        // We can reuse the scanner throughout our application
        Scanner s = new Scanner(System.in);

        // Clear the screen right away
        clear();

        do {
            System.out.println("You are currently working on:");

            if (currentStudents.contains(currentStudent)) {
                System.out.println(currentStudent.getName());
            } else {
                System.out.println("A new student");
            }
            System.out.println();

            printMenuOptions();
            int choice = inputMenuOption(s);

            switch (choice) {
                case 1:
                    String name = inputString(s, "Please enter the students name: ");
                    currentStudent.setName(name);
                    System.out.println();
                    System.out.println("Name was successfully updated to " + currentStudent.name + "\n");
                    clearInput();
                    break;
                case 2:
                    String yearString = inputString(s, "Please enter the students year: ");
                    Year year = Year.findByYear(yearString);
                    System.out.println();
                    if (year == null) {
                        System.out.println("Error: The students year must be either\n" +
                                "\tFreshman\n" +
                                "\tSophomore\n" +
                                "\tJunior\n" +
                                "\tSenior\n");
                        currentStudent.setYear(year.NONE);
                    } else {
                        currentStudent.setYear(year);
                        System.out.println("Year was successfully updated to " + currentStudent.getYear().year + "\n");
                    }
                    clearInput();
                    break;
                case 3:
                    double gpa = inputDouble(s, "Please enter the students GPA: ");
                    System.out.println();
                    if (!validateGpa(gpa)) {
                        System.out.println("Error: The students GPA must be between 0.0 and 4.0\n");
                    } else {
                        currentStudent.setGpa(gpa);
                        System.out.println("GPA was successfully updated to: " + currentStudent.getGpa() + "\n");
                    }
                    clearInput();
                    break;
                case 4:
                    printInformation(currentStudent.getName(), currentStudent.getYear().year, currentStudent.getGpa());
                    System.out.println();
                    clearInput();
                    break;
                case 5:
                    printAllStudents(currentStudents);
                    clearInput();
                    break;
                case 6:
                    writeDataToFile(s);
                    clearInput();
                    break;
                case 7:
                    readFromFile(s);
                    clearInput();
                    break;
                case 8:
                    searchByName(s);
                    clearInput();
                    break;
                case 9:
                    createNewStudent();
                    clearInput();
                    break;
                case 10:
                    deleteStudent(s);
                    clearInput();
                    break;
                case 11:
                    isRunning = false;
                    break;
                default:
                    System.out.println("The menu option entered was invalid. Please enter 1-11");
                    clearInput();
                    break;

            }
        } while (isRunning);
        System.out.println("\nGoodbye!\n");
        s.close();
    }

    // Print methods
    public static void printMenuOptions() {
        // Array to store menu items
        String[] menuItems = {
                "1. Enter Students Name",
                "2. Enter Students Academic Year",
                "3. Enter Students GPA",
                "4. Display Current Students Information",
                "5. Display all Students",
                "6. Write to File",
                "7. Read from File",
                "8. Search for a Student",
                "9. Create a new Student",
                "10. Delete a the current Student",
                "11. Exit"
        };

        for (String item: menuItems) {
            System.out.println(item);
        }
    }

    public static void printInformation(String name, String year, double gpa) {
        System.out.println();
        System.out.println("The students name is " + name);
        System.out.println("The students year is " + year);
        System.out.println("The students GPS is " + gpa);
    }

    public static void printValidationErrors(ArrayList<String> errors) {
        System.out.println("Please answer all questions");
        for(String error: errors) {
            System.out.println("\t" + error);
        }
    }

    public static void printAllStudents(ArrayList<Student> students) {
        System.out.println();
        if (students.isEmpty()) {
            System.out.println("There are no students in this file");
        } else {
            for(int i = 0; i < students.size(); i++) {
                System.out.println("Student " + (i + 1) + ":\n" +
                        "\tName: " + students.get(i).getName() + "\n" +
                        "\tYear: " + students.get(i).getYear().year + "\n" +
                        "\tGPA: " + students.get(i).getGpa() + "\n");
            }
        }
    }

    // Input methods
    public static int inputMenuOption(Scanner s) {
        String inputPrompt = "Please enter which number you want to answer: ";
        System.out.print(inputPrompt);
        try {
            return s.nextInt();
        } catch (Exception e) {
            // We need this line here to clear the buffer on a bad input,
            // or else we get trapped in a permanent loop
            s.nextLine();
            return -1;
        }
    }

    public static int inputInt(Scanner s, String prompt) {
        System.out.print(prompt);
        try {
            return s.nextInt();
        } catch (Exception e) {
            // We need this line here to clear the buffer on a bad input,
            // or else we get trapped in a permanent loop
            s.nextLine();
            return -1;
        }
    }


    public static String inputString(Scanner s, String prompt) {
        System.out.print(prompt);

        try {
            // Clear the buffer from the previous input
            s.nextLine();
            return s.nextLine().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static double inputDouble(Scanner s, String prompt) {
        System.out.print(prompt);

        try {
            return s.nextDouble();
        } catch (Exception e) {
            return -1.0;
        }
    }

    public static boolean validateGpa(double studentGpa) {
        return studentGpa >= 0.0 && studentGpa <= 4.0;
    }

    public static ArrayList<String> validateInformation(String name, Year year, Double gpa) {
        // Store the errors in an array list. We do this because
        // the function doesn't know how many things are valid or
        // invalid till it's run. The number of errors can be between
        // 0 and 3
        ArrayList<String> errors = new ArrayList<>();
        if (name.equals(""))
            errors.add("The students name must be set");
        if (year == Year.NONE)
            errors.add("The students year must be set");
        if (gpa == -1.0)
            errors.add("The students GPA must be set");

        return errors;
    }

    // Methods to read and write to file
    public static void writeDataToFile(Scanner s) {
        // Cannot write to file if not all fields are entered
        ArrayList<String> errors = validateInformation(currentStudent.getName(), currentStudent.getYear(), currentStudent.getGpa());
        if (!errors.isEmpty()) {
            printValidationErrors(errors);
        } else {
            try {
                System.out.println();
                File outputFile = new File("students-" + LocalDate.now() + ".csv");
                if (!outputFile.exists()) {
                    System.out.println();
                    if (outputFile.createNewFile()) {
                        System.out.println("Created a new file at: " + outputFile.getAbsolutePath());
                    }
                }
                System.out.println("File Found: " + outputFile.getAbsolutePath());
                // We don't want to append here. If we did, we would be duplicating users all
                // over the place
                FileWriter writer = new FileWriter(outputFile, false);
                // We need to add the current student if they are not
                // already in the file
                for (int i = 0; i < currentStudents.size(); i++) {
                    if (currentStudent.getId() == currentStudents.get(i).getId()) {
                        currentStudents.set(i, currentStudent);
                    } else if (!currentStudents.contains(currentStudent)) {
                        currentStudents.add(currentStudent);
                    }
                }

                for(Student student: currentStudents) {
                    writer.append(student.toString() + '\n');
                }
                writer.close();
                System.out.println("Successfully added student " + currentStudent.getName() + " to file " + outputFile.getName());
                System.out.println();
            } catch (IOException e) {
                System.err.println("An error occurred while writing to file. Check your file location and try again!");
            }
        }
    }

    public static void readFromFile(Scanner s) {
        try {
            String inputPath = inputString(s, "Please enter the path of the file you want to read from: ");
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.out.println();
                System.out.println("The file entered was not found at the given path. Please try another path");
            }
            else {
                boolean success = readStudents(inputFile);
                if (success) {
                    printAllStudents(currentStudents);
                    System.out.println("Successfully loaded all students from the file!");
                }
            }
        } catch (Exception e) {
            printFileError();
        }
    }

    public static boolean readStudents(File file) {
        try {
            // ArrayList of all students loaded from the file
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String studentData = s.nextLine().trim();
                Student student = formatStudent(studentData);
                currentStudents.add(student);
            }
            s.close();
            return true;
        } catch (FileNotFoundException e) {
            printFileError();
        } catch (Exception e) {
            System.err.println("An error occurred processing your request. Please try again");
        }
        return false;
    }

    public static Student formatStudent(String studentData) {
        String[] studentFields = studentData.split(",");

        UUID id = UUID.fromString(studentFields[0]);
        String studentName = studentFields[1];
        Year studentYear = Year.findByYear(studentFields[2]);
        double studentGpa = Double.parseDouble(studentFields[3]);


        Student student = new Student(
                studentName,
                studentYear,
                studentGpa);
        return student;
    }

    public static void printFileError() {
        System.err.println("An error occurred while reading the file. Check your file path and try again");
    }

    // Method to search for a student
    public static void searchByName(Scanner s) {
        String name = inputString(s, "Please enter the name of the student you want to search for: ").trim();
        ArrayList<Student> students = new ArrayList<>();

        for(Student student: currentStudents) {
            if (student.getName().equals(name)) {
                students.add(student);
            }
        }

        if (students.isEmpty()) {
            System.out.println("No student with that name was found!");
        } else if(students.size() > 1) {
            System.out.println("Multiple students with that name were found");
            for (int i = 0; i < students.size(); i++) {
                System.out.println(i + 1 + ". " + students.get(i).getName() + " | " + students.get(i).getYear() + " | " + students.get(i).getGpa());
            }

            int choice = inputInt(s, "Which student would you like to select?");
            if (choice != -1) {
                currentStudent = students.get(choice - 1);
                System.out.println("Successfully selected a student!");
                printInformation(currentStudent.getName(), currentStudent.getYear().year, currentStudent.getGpa());
            } else {
                System.out.println("Invalid choice entered!");
            }
        } else {
            // There's only one element in the ArrayList
            currentStudent = students.get(0);
            System.out.println("Successfully selected a student!");
            printInformation(currentStudent.getName(), currentStudent.getYear().year, currentStudent.getGpa());
        }
    }

    // Method to create a student
    public static void createNewStudent() {
        currentStudent = DEFAULT_STUDENT;
        System.out.println("New student created! You can begin changing fields");
    }

    // Method to delete a student
    public static void deleteStudent(Scanner s) {
        String response = inputString(s, "Are you sure you want to delete the current student? (yes/no) ");
        if (response.equals("yes")) {
            for (int i = 0; i < currentStudents.size(); i++) {
                if (currentStudent.getId() == currentStudents.get(i).getId()) {
                    currentStudents.remove(currentStudent);
                }
            }
            currentStudent = DEFAULT_STUDENT;
            System.out.println("Successfully deleted the student!");
        }
    }

    // Method to clear the screen
    public static void clearInput() {
        System.out.println("Press the Enter key to continue");

        try {
            // Only read the enter key
            System.in.read();
            clear();
        } catch (Exception ignored) { }
    }

    public static void clear() {
        // ANSI code for move cursor home and clear screen
        // This does not work in non-ANSI terminals, like CMD
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Enum to hold year types
    // This makes working with currentStudent objects
    // much easier to work with
    public enum Year {
        NONE("None"),
        FRESHMAN("Freshman"),
        SOPHOMORE("Sophomore"),
        JUNIOR("Junior"),
        SENIOR("Senior");

        String year;

        Year(String year) {
            this.year = year;
        }

        public static Year findByYear(String year) {
            for(Year y: values()) {
                if (y.year.equals(year)) {
                    return y;
                }
            }
            return null;
        }
    }

    // Student class to hold data per student
    // If we want to append students to a file,
    // we want an admin to be able to chose which currentStudent
    // to view or edit
    public static class Student {
        // Read-Only ID that gets set when a user is created
        private UUID id;
        private String name;
        private Year year;
        private double gpa;

        public Student() {
            this.id = UUID.randomUUID();
        }

        public Student(String name, Year year, double gpa) {
            this.id = UUID.randomUUID();
            this.name = name;
            this.year = year;
            this.gpa = gpa;
        }

        public Student(UUID id, String name, Year year, double gpa) {
            this.id = id;
            this.name = name;
            this.year = year;
            this.gpa = gpa;
        }


        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Year getYear() {
            return year;
        }

        public void setYear(Year year) {
            this.year = year;
        }

        public double getGpa() {
            return gpa;
        }

        public void setGpa(double gpa) {
            this.gpa = gpa;
        }

        @Override
        public String toString() {
            return id + "," + name +
                    "," + year.year +
                    "," + gpa;
        }
    }
}


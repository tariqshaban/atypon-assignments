package com.company;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.company.Assignment.ASSIGNMENTS;

public class Student {
    //A linked hashmap that stores the student objects
    static final LinkedHashMap<Integer, Student> STUDENTS = new LinkedHashMap<>();
    //The ID of the student; cant be changed
    private final int ID;
    //The name of the student
    private String name;
    //A hashmap that shows the student's registered assignment, each student object has one
    private final HashMap<Assignment, Float> STUDENTS_ASSIGNMENT;

    //Getter,setters

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Assignment, Float> getSTUDENTS_ASSIGNMENT() {
        return STUDENTS_ASSIGNMENT;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    //A parameterized constructor which assigns each values respectively as well as allocates space for STUDENTS_ASSIGNMENT
    Student(int ID, String name) {
        this.ID = ID;
        this.name = name;
        STUDENTS_ASSIGNMENT = new HashMap<>();
    }

    //Creates a new student object and add it to the hashmap, the parameters should adhere to some requirements
    static boolean addStudent(int studentID, String name) {
        if (name.length() > 20)
            printToConsole("The student name is too long", 1, true);
        else if (STUDENTS.containsKey(studentID)) {
            printToConsole("ID already exists", 1, true);
            return false;
        } else {
            STUDENTS.put(studentID, new Student(studentID, name));
            printToConsole("Student registered successfully", 0, true);
        }
        return true;
    }

    //Deletes a student from the list
    static void deleteStudent(int studentID) {
        if (STUDENTS.containsKey(studentID)) {
            STUDENTS.put(studentID, null);
            STUDENTS.remove(studentID);
            printToConsole("Student deleted successfully", 0, true);
        } else
            printToConsole("The student ID doesn't exist", 1, true);
    }

    //Adds a new assignment for a student
    public void addAssignment(Assignment assignment) {
        if(!STUDENTS_ASSIGNMENT.containsKey(assignment)) {
            printToConsole("Assignment \"" + assignment.getName() + "\" is set to " + this.getName(), 0, true);
            STUDENTS_ASSIGNMENT.put(assignment, -1.0f);
        }
        else printToConsole("This assignment is already set to student "+this.getName(), 1, true);
    }

    //Prints a list of the students
    static void printAllStudents() {
        if (!STUDENTS.isEmpty()) {
            printToConsole("ID\t\tName", 2, true);
            printToConsole("--\t\t----", 2, true);
            for (Student student : STUDENTS.values())
                System.out.println(student.getID() + "\t\t" + student.getName());
        } else printToConsole("No marks to display", 1, true);
    }

    //Prints the student marks in each assignment as well as adding the average
    static void printStudentMarks(int studentID) {
        if (!STUDENTS.containsKey(studentID))
            printToConsole("The student ID doesn't exist", 1, true);
        else {
            boolean printed = false;
            float sum = 0;
            int count = 0;
            if (!STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().isEmpty() && shouldPrint(STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT())) {
                printed = true;
                printToConsole(String.format("%-20s", STUDENTS.get(studentID).getName() + "(" + STUDENTS.get(studentID).getID() + ")" + ":"), 0, false);
                for (Assignment assignment : ASSIGNMENTS.values()) {
                    if (STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment) != null && STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment) != -1.0) {
                        printToConsole(assignment.getName() + "(" + assignment.getWeight() + ")" + " ", 0, false);
                        if (STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment) >= 50)
                            printToConsole(String.format("%-15s", STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment)), 2, false);
                        else
                            printToConsole(String.format("%-15s", STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment)), 3, false);
                        sum += STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().get(assignment) * assignment.getWeight();
                        count += assignment.getWeight();
                    } else {
                        printToConsole(assignment.getName() + "(" + assignment.getWeight() + ")" + " ", 0, false);
                        printToConsole(String.format("%-15s", "-"), 1, false);
                    }
                }
                printToConsole("Average is: ", 0, false);
                if (sum / count >= 50)
                    printToConsole(String.format("%.2f", sum / count) + "", 2, true);
                else printToConsole(String.format("%.2f", sum / count) + "", 3, true);
            }
            if (!printed)
                printToConsole("No marks to display for student " + STUDENTS.get(studentID).getName(), 1, true);
        }
    }

    //Checks if the mark of the assignment is -1.0 or not "the default mark when it is not set"
    private static boolean shouldPrint(HashMap<Assignment, Float> studentsAssignment) {
        for (Float mark : studentsAssignment.values())
            if (mark != -1)
                return true;
        return false;
    }

    //Orders the students depending on their average, then prints it
    static void printAllMarks() {
        HashMap<HashMap<Student, HashMap<Assignment, Float>>, Float> studentsData = new HashMap<>();
        for (Student student : STUDENTS.values()) {
            HashMap<Student, HashMap<Assignment, Float>> studentData = new HashMap<>();
            studentData.put(student, student.getSTUDENTS_ASSIGNMENT());
            studentsData.put(studentData, -1.0f);
        }
        for (HashMap<Student, HashMap<Assignment, Float>> students : studentsData.keySet()) {
            float sum = 0;
            int count = 0;
            for (HashMap<Assignment, Float> studentAssignments : students.values()) {
                for (Assignment studentAssignment : studentAssignments.keySet()) {
                    if (studentAssignments.get(studentAssignment) != -1.0f) {
                        sum += studentAssignments.get(studentAssignment) * studentAssignment.getWeight();
                        count += studentAssignment.getWeight();
                    }
                }
                if (!Float.isNaN((sum / count)))
                    studentsData.put(students, (sum / count));
                else studentsData.put(students, -1.0f);
            }
        }
        boolean printed = false;
        if (!studentsData.isEmpty()) {
            float max = Collections.max(studentsData.values());
            while (max != -1.0f) {
                for (HashMap<Student, HashMap<Assignment, Float>> students : studentsData.keySet()) {
                    if (studentsData.get(students) != null)
                        if (studentsData.get(students) == max && max != -1.0f) {
                            printStudentMarks(students.entrySet().iterator().next().getKey().getID());
                            studentsData.put(students, -1.0f);
                            students.keySet().removeIf(student1 -> student1 == students.entrySet().iterator().next().getKey());
                            max = Collections.max(studentsData.values());
                            printed = true;
                        } else if (Float.isNaN(max))
                            max = Collections.max(studentsData.values());
                }
            }
        }
        if (studentsData.isEmpty() || !printed)
            printToConsole("No marks to display", 1, true);
    }

    //Prints an array of colors; depending on the parameters
    //Windows CMD does not support ANSI escape code, hence, the color wont change and it will print the text as is
    static void printToConsole(String text, int warning, boolean newLine) {
        if (newLine)
            switch (warning) {
                case 0 -> System.out.println(text);
                case 1 -> System.out.println("\u001B[33m" + text + "\u001B[0m");
                case 2 -> System.out.println("\u001B[34m" + text + "\u001B[0m");
                case 3 -> System.out.println("\u001B[31m" + text + "\u001B[0m");
                case 4 -> System.out.println("\u001B[46m" + text + "\u001B[0m");
            }
        else
            switch (warning) {
                case 0 -> System.out.print(text);
                case 1 -> System.out.print("\u001B[33m" + text + "\u001B[0m");
                case 2 -> System.out.print("\u001B[34m" + text + "\u001B[0m");
                case 3 -> System.out.print("\u001B[31m" + text + "\u001B[0m");
                case 4 -> System.out.print("\u001B[46m" + text + "\u001B[0m");
            }
    }
}
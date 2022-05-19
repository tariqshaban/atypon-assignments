package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.company.Student.printToConsole;
import static com.company.Student.STUDENTS;

public class Assignment {
    //A linked hashmap that stores the assignment objects
    static final LinkedHashMap<Integer, Assignment> ASSIGNMENTS = new LinkedHashMap<>();
    //The number of the assignment; cant be changed
    private final int ASSIGNMENT_NO;
    //The name of the assignment
    private String name;
    //The weight of the assignment; the higher the value, the more of the assignment can inflict on the student's average
    private float weight;

    //Getter,setters

    public int getAssignmentNo() {
        return ASSIGNMENT_NO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    //A parameterized constructor which assigns each values respectively
    Assignment(int assignmentNo, String name, float weight) {
        this.ASSIGNMENT_NO = assignmentNo;
        this.name = name;
        this.weight = weight;
    }

    //Creates a new assignment object and add it to the hashmap, the parameters should adhere to some requirements
    static boolean addAssignment(int assignmentNo, String name, float weight) {
        if (name.length() > 30)
            printToConsole("The assignment name is too long", 1, true);
        else if (ASSIGNMENTS.containsKey(assignmentNo)) {
            printToConsole("Assignment number already exists", 1, true);
            return false;
        } else {
            ASSIGNMENTS.put(assignmentNo, new Assignment(assignmentNo, name, weight));
            printToConsole("Assignment registered successfully", 0, true);
        }
        return true;
    }

    //Deletes an assignment from the list
    static void deleteAssignment(int assignmentNo) {
        if (ASSIGNMENTS.containsKey(assignmentNo)) {
            ASSIGNMENTS.put(assignmentNo, null);
            ASSIGNMENTS.remove(assignmentNo);
            for (Student student : STUDENTS.values())
                student.getSTUDENTS_ASSIGNMENT().keySet().removeIf(assignment1 -> assignment1.getAssignmentNo() == assignmentNo);
            printToConsole("Assignment deleted successfully", 0, true);
        } else
            printToConsole("The assignment number doesn't exist", 1, true);
    }

    //Sets a specific assignment to a specific student
    static void setAssignmentTo(int assignmentNo, int studentID) {
        if (!STUDENTS.containsKey(studentID))
            printToConsole("The student ID doesn't exist", 1, true);
        else if (!ASSIGNMENTS.containsKey(assignmentNo))
            printToConsole("The assignment number doesn't exist", 1, true);
        else
            STUDENTS.get(studentID).addAssignment(ASSIGNMENTS.get(assignmentNo));
    }

    //Sets a specific assignment to all students
    static void setAssignmentToAll(int assignmentNo) {
        if(!STUDENTS.isEmpty()) {
            if (!ASSIGNMENTS.containsKey(assignmentNo))
                printToConsole("The assignment number doesn't exist", 1, true);
            else {
                for (Student student : STUDENTS.values())
                    student.getSTUDENTS_ASSIGNMENT().put(ASSIGNMENTS.get(assignmentNo), -1.0f);
                printToConsole("Assignment \"" + ASSIGNMENTS.get(assignmentNo).getName() + "\" is set to all students", 0, true);
            }
        } else printToConsole("There are no students added", 1, true);
    }

    //Sets a mark for a specific assignment to a specific student
    static void setAssignmentMarkTo(int assignmentNo, int studentID, float mark) {
        if (!STUDENTS.containsKey(studentID))
            printToConsole("The student ID doesn't exist", 1, true);
        else if (!ASSIGNMENTS.containsKey(assignmentNo))
            printToConsole("The assignment number doesn't exist", 1, true);
        else if (mark > 100 || mark < 0)
            printToConsole("Invalid Mark", 1, true);
        else {
            if (!STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().isEmpty() && STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().containsKey(ASSIGNMENTS.get(assignmentNo))) {
                STUDENTS.get(studentID).getSTUDENTS_ASSIGNMENT().put(ASSIGNMENTS.get(assignmentNo), mark);
                printToConsole("Mark is set successfully", 0, true);
            } else printToConsole("Operation failed, assignment is not set to such student", 1, true);
        }
    }

    //Sets a mark for a specific assignment to each student (iterates through them)
    static void setAssignmentMarkToAllEach(int assignmentNo, Float[] marks) {
        if(!STUDENTS.isEmpty()) {
            if (!ASSIGNMENTS.containsKey(assignmentNo))
                printToConsole("The assignment number doesn't exist", 1, true);
            else {
                int counter = 0;
                for (Student student : STUDENTS.values())
                    student.getSTUDENTS_ASSIGNMENT().put(ASSIGNMENTS.get(assignmentNo), marks[counter++]);
                printToConsole("Marks are set successfully", 0, true);
            }
        } else printToConsole("There are no students added", 1, true);
    }

    //Sets a mark for a specific assignment to all students (all students have the same mark for this assignment)
    static void setAssignmentMarkToAllBatch(int assignmentNo, float mark) {
        if(!STUDENTS.isEmpty()) {
            if (!ASSIGNMENTS.containsKey(assignmentNo))
                printToConsole("The assignment number doesn't exist", 1, true);
            else if (mark > 100 || mark < 0)
                printToConsole("Invalid Mark", 1, true);
            else {
                for (Student student : STUDENTS.values())
                    student.getSTUDENTS_ASSIGNMENT().put(ASSIGNMENTS.get(assignmentNo), mark);
                printToConsole("Marks are set successfully", 0, true);
            }
        } else printToConsole("There are no students added", 1, true);
    }

    //Prints a list showing the students who have this assignment registered
    static void printAnAssignmentStudents(int assignmentNo) {
        if (!ASSIGNMENTS.containsKey(assignmentNo))
            printToConsole("The assignment number doesn't exist", 1, true);
        else {
            ArrayList<String> studentsForThisAssignment = new ArrayList<>();
            for (Student student : STUDENTS.values()) {
                for (Assignment assignment : student.getSTUDENTS_ASSIGNMENT().keySet())
                    if (assignment.getAssignmentNo() == assignmentNo)
                        studentsForThisAssignment.add(student.getName());
            }
            if (!studentsForThisAssignment.isEmpty()) {
                printToConsole(ASSIGNMENTS.get(assignmentNo).getName(), 2, true);
                printToConsole("---------------------------", 2, true);
                for (String name : studentsForThisAssignment)
                    printToConsole(name, 0, true);
            } else
                printToConsole("No students have \"" + ASSIGNMENTS.get(assignmentNo).getName() + "\" set", 1, true);
        }
    }

    //Prints a list of assignments and their properties
    static void printAllAssignments() {
        if (!STUDENTS.isEmpty()) {
            printToConsole(String.format("%-15s %-30s %-15s", "Number", "Name", "Weight"), 2, true);
            printToConsole(String.format("%-15s %-30s %-15s", "----------", "---------------------------", "-------"), 2, true);
            for (Assignment assignment : ASSIGNMENTS.values())
                printToConsole((String.format("%-15s %-30s %-15s", assignment.getAssignmentNo(), assignment.getName(), assignment.getWeight())), 0, true);
        } else printToConsole("No assignments to display", 1, true);
    }

    //Calculates the average mark for this assignment (ignores any unset mark)
    static void printAverageGradeForAssignment(int assignmentNo) {
        if (!ASSIGNMENTS.containsKey(assignmentNo))
            printToConsole("The assignment number doesn't exist", 1, true);
        else {
            boolean printed = false;
            float sum = 0;
            int count = 0;
            for (Student student : STUDENTS.values()) {
                if (!student.getSTUDENTS_ASSIGNMENT().isEmpty() && student.getSTUDENTS_ASSIGNMENT().containsKey(ASSIGNMENTS.get(assignmentNo)) && shouldPrint(student.getSTUDENTS_ASSIGNMENT())) {
                    printed = true;
                    if (student.getSTUDENTS_ASSIGNMENT().get(ASSIGNMENTS.get(assignmentNo)) != -1.0) {
                        sum += student.getSTUDENTS_ASSIGNMENT().get(ASSIGNMENTS.get(assignmentNo));
                        count++;
                    }
                }
            }
            if (!printed)
                printToConsole("No students have \"" + ASSIGNMENTS.get(assignmentNo).getName() + "\" set or marks have not been set yet", 1, true);
            else {
                printToConsole("\"" + ASSIGNMENTS.get(assignmentNo).getName() + "\" With a weight of: ", 0, false);
                printToConsole(ASSIGNMENTS.get(assignmentNo).getWeight() + "", 2, false);
                printToConsole(" Have an average of: ", 0, false);
                if (sum / count >= 50)
                    printToConsole(sum / count + "", 2, true);
                else printToConsole(sum / count + "", 3, true);
            }
        }
    }

    //Checks if the mark of the assignment is -1.0 or not "the default mark when it is not set"
    private static boolean shouldPrint(HashMap<Assignment, Float> studentsAssignment) {
        for (Float mark : studentsAssignment.values())
            if (mark != -1)
                return true;
        return false;
    }

    //Calls printAverageGradeForAssignment() multiple times to show all the assignment's average
    static void printAverageGradeForAssignmentAll() {
        if(!ASSIGNMENTS.isEmpty())
        for (Assignment assignment : ASSIGNMENTS.values())
            printAverageGradeForAssignment(assignment.getAssignmentNo());
        else printToConsole("No assignments to display", 1, true);
    }
}
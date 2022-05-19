package com.company;

import java.util.ArrayList;
import java.util.Scanner;

import static com.company.Student.*;
import static com.company.Assignment.*;

public class CliHandler {

    private enum Page {
        MainMenu,
        Modify_Student_Assignment,
        Set_Assignments_Marks_To_Students,
        Print,
        PrintStudent,
        PrintAssignment
    }

    //A scanner to log user's commands
    private static final Scanner input = new Scanner(System.in);
    //An enum to know which menu the user is currently in
    private static Enum<Page> outerState;

    //Stimulates clearing the screen by printing many new lines
    private static void clear() {
        printToConsole("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", 0, true);
    }

    //A group of functions that shows a menu to the user

    static void printMainMenu() {
        clear();
        printToConsole("Main Menu", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Modify Student/Assignment", 0, true);
        printToConsole("2. Set Assignments/Marks To Students", 0, true);
        printToConsole("3. Print", 0, true);
        printToConsole("0. Exit", 1, true);

        //Calls a function to display the next menu depending on the user's selected option
        outerState = Page.MainMenu;
        switch (validatedSelection(1, 3)) {
            case 1 -> printModifyStudentAssignment();
            case 2 -> printSetAssignmentsMarksToStudents();
            case 3 -> printPrint();
            case 0 -> System.exit(0);
        }
    }

    private static void printModifyStudentAssignment() {
        clear();
        printToConsole("Modify Student/Assignment", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Add Student", 0, true);
        printToConsole("2. Delete Student", 0, true);
        printToConsole("3. Add Assignment", 0, true);
        printToConsole("4. Delete Assignment", 0, true);
        printToConsole("0. Back", 1, true);

        outerState = Page.Modify_Student_Assignment;
        switch (validatedSelection(1, 4)) {
            case 1 -> printAddStudent();
            case 2 -> printDeleteStudent();
            case 3 -> printAddAssignment();
            case 4 -> printDeleteAssignment();
            case 0 -> {
                outerState = Page.MainMenu;
                handleMenuBack();
            }
        }

    }

    private static void printSetAssignmentsMarksToStudents() {
        clear();
        printToConsole("Set Assignments/Marks To Students", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Set An Assignment To A Student", 0, true);
        printToConsole("2. Set An Assignment To All", 0, true);
        printToConsole("3. Set An Assignment Mark To A Student", 0, true);
        printToConsole("4. Set An Assignment Mark To All (Each)", 0, true);
        printToConsole("5. Set An Assignment Mark To All (Batch)", 0, true);
        printToConsole("0. Back", 1, true);

        outerState = Page.Set_Assignments_Marks_To_Students;
        switch (validatedSelection(1, 5)) {
            case 1 -> printSetAssignmentTo();
            case 2 -> printSetAssignmentToAll();
            case 3 -> printSetAssignmentMarkTo();
            case 4 -> printSetAssignmentMarkToAllEach();
            case 5 -> printSetAssignmentMarkToAllBatch();
            case 0 -> {
                outerState = Page.MainMenu;
                handleMenuBack();
            }
        }
    }

    private static void printPrint() {
        clear();
        printToConsole("Print", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Students Printable", 0, true);
        printToConsole("2. Assignments Printable", 0, true);
        printToConsole("0. Back", 1, true);

        outerState = Page.Print;
        switch (validatedSelection(1, 2)) {
            case 1 -> printStudent();
            case 2 -> printAssignment();
            case 0 -> {
                outerState = Page.MainMenu;
                handleMenuBack();
            }
        }
    }

    private static void printStudent() {
        clear();
        printToConsole("Print Student", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Print All Students", 0, true);
        printToConsole("2. Print All Students Marks", 0, true);
        printToConsole("3. Print A Student Marks", 0, true);
        printToConsole("0. Back", 1, true);

        outerState = Page.PrintStudent;
        switch (validatedSelection(1, 3)) {
            case 1 -> printPrintAllStudents();
            case 2 -> printPrintAllMarks();
            case 3 -> printPrintAStudentMarks();
            case 0 -> {
                outerState = Page.Print;
                handleMenuBack();
            }
        }
    }

    private static void printAssignment() {
        clear();
        printToConsole("Print Assignment", 4, true);
        printToConsole("Please Select An Option..\n", 0, true);
        printToConsole("1. Print All Assignments", 0, true);
        printToConsole("2. Print Average Grade For An Assignment", 0, true);
        printToConsole("3. Print Average Grade For All Assignments", 0, true);
        printToConsole("4. Print An Assignment's Students", 0, true);
        printToConsole("0. Back", 1, true);

        outerState = Page.PrintAssignment;
        switch (validatedSelection(1, 4)) {
            case 1 -> printPrintAllAssignments();
            case 2 -> printPrintAverageGradeForAnAssignment();
            case 3 -> printPrintAverageGradeForAllAssignments();
            case 4 -> printPrintAnAssignmentStudents();
            case 0 -> {
                outerState = Page.Print;
                handleMenuBack();
            }
        }
    }

    //A group of functions gets input from the user then calls a function inside the student/assignment class

    private static void printAddStudent() {
        clear();
        printToConsole("Please enter the ID followed by the name of the student", 0, true);
        int studentID = (int) validateDatatype(0);
        String studentName = (String) validateDatatype(2);
        if (!addStudent(studentID, studentName)) {
            printToConsole("Overwrite existing data? (Y/N)", 0, true);
            String response = (String) validateDatatype(2);
            while (!(response.toLowerCase().equals("y") || response.toLowerCase().equals("n"))) {
                printToConsole("Invalid input", 1, true);
                response = (String) validateDatatype(2);
            }
            if (response.toLowerCase().equals("y")) {
                STUDENTS.get(studentID).setName(studentName);
                printToConsole("Student registered", 0, true);
            }
        }
        printGoBack();
    }

    private static void printDeleteStudent() {
        clear();
        printToConsole("Please enter the ID of the student that you wish to delete", 0, true);
        deleteStudent((int) validateDatatype(0));
        printGoBack();
    }

    private static void printAddAssignment() {
        clear();
        printToConsole("Please enter the assignment number, name and weight", 0, true);
        int assignmentNo = (int) validateDatatype(0);
        String assignmentName = (String) validateDatatype(2);
        float assignmentWeight = (float) validateDatatype(1);
        if (!addAssignment(assignmentNo, assignmentName, assignmentWeight)) {
            printToConsole("Overwrite existing data? (Y/N)", 0, true);
            String response = (String) validateDatatype(2);
            while (!(response.toLowerCase().equals("y") || response.toLowerCase().equals("n"))) {
                printToConsole("Invalid input", 1, true);
                response = (String) validateDatatype(2);
            }
            if (response.toLowerCase().equals("y")) {
                ASSIGNMENTS.get(assignmentNo).setName(assignmentName);
                ASSIGNMENTS.get(assignmentNo).setWeight(assignmentWeight);
                printToConsole("Assignment registered", 0, true);
            }
        }
        printGoBack();
    }

    private static void printDeleteAssignment() {
        clear();
        printToConsole("Please enter the assignment number that you wish to delete", 0, true);
        deleteAssignment((int) validateDatatype(0));
        printGoBack();
    }

    private static void printSetAssignmentTo() {
        clear();
        printToConsole("Please enter the assignment number followed by the student ID", 0, true);
        setAssignmentTo((int) validateDatatype(0), (int) validateDatatype(0));
        printGoBack();
    }

    private static void printSetAssignmentToAll() {
        clear();
        printToConsole("Please enter the assignment number that you wish to set the marks for", 0, true);
        setAssignmentToAll((int) validateDatatype(0));
        printGoBack();
    }

    private static void printSetAssignmentMarkTo() {
        clear();
        printToConsole("Please enter the assignment number followed by the student ID and his mark", 0, true);
        setAssignmentMarkTo((int) validateDatatype(0), (int) validateDatatype(0), (float) validateDatatype(1));
        printGoBack();
    }

    private static void printSetAssignmentMarkToAllEach() {
        clear();
        printToConsole("Please enter the assignment number that you wish to set the marks for", 0, true);
        setAssignmentMarkToAllEach((int) validateDatatype(0), getMarks());
        printGoBack();
    }

    private static Float[] getMarks() {
        ArrayList<Float> marks = new ArrayList<>();
        for (Student student : STUDENTS.values()) {
            printToConsole("Please enter the mark for the student " + student.getName() + ": ", 0, true);
            marks.add(validateRange((float) validateDatatype(1)));
        }
        return marks.toArray(new Float[0]);
    }

    private static void printSetAssignmentMarkToAllBatch() {
        clear();
        printToConsole("Please enter the assignment number followed by a mark for all students", 0, true);
        setAssignmentMarkToAllBatch((int) validateDatatype(0), (float) validateDatatype(1));
        printGoBack();
    }

    private static void printPrintAStudentMarks() {
        clear();
        printToConsole("Please enter the student ID", 0, true);
        printStudentMarks((int) validateDatatype(0));
        printGoBack();
    }

    private static void printPrintAverageGradeForAnAssignment() {
        clear();
        printToConsole("Please enter the assignment number", 0, true);
        printAverageGradeForAssignment((int) validateDatatype(0));
        printGoBack();
    }

    private static void printPrintAverageGradeForAllAssignments() {
        clear();
        printAverageGradeForAssignmentAll();
        printGoBack();
    }

    private static void printPrintAllStudents() {
        clear();
        printAllStudents();
        printGoBack();
    }

    private static void printPrintAllAssignments() {
        clear();
        printAllAssignments();
        printGoBack();
    }

    private static void printPrintAnAssignmentStudents() {
        clear();
        printToConsole("Please enter the assignment number", 0, true);
        printAnAssignmentStudents((int) validateDatatype(0));
        printGoBack();
    }

    private static void printPrintAllMarks() {
        clear();
        printAllMarks();
        printGoBack();
    }

    //A function which modifies the enum depending on the current menu state when back is triggered
    private static void handleMenuBack() {
        if (outerState == Page.MainMenu)
            printMainMenu();
        else if (outerState == Page.Modify_Student_Assignment) {
            outerState = Page.MainMenu;
            printModifyStudentAssignment();
        } else if (outerState == Page.Set_Assignments_Marks_To_Students) {
            outerState = Page.MainMenu;
            printSetAssignmentsMarksToStudents();
        } else if (outerState == Page.Print) {
            outerState = Page.MainMenu;
            printPrint();
        } else if (outerState == Page.PrintStudent) {
            outerState = Page.Print;
            printStudent();
        } else if (outerState == Page.PrintAssignment) {
            outerState = Page.Print;
            printAssignment();
        }
    }

    //A function which modifies the enum depending on the current state when a function is finished
    private static void printGoBack() {
        printToConsole("\n0. Back", 1, true);
        if (validatedSelection(0, 0) == 0)
            handleMenuBack();
    }

    //Checks the user's option input in a menu; whether it is in the numbers range or not, continues requesting until the value is correct
    private static int validatedSelection(int minSelection, int maxSelection) {
        int selection = -1;
        while (!(selection >= minSelection && selection <= maxSelection) && selection != 0) {
            try {
                selection = input.nextInt();
                if (!(selection >= minSelection && selection <= maxSelection) && selection != 0) {
                    throw new OutOfBoundOptionException("Invalid input");
                }
            } catch (OutOfBoundOptionException e) {
                printToConsole("Invalid number", 1, true);

            } catch (Exception e) {
                printToConsole("Invalid input", 1, true);
                input.nextLine();
            }
        }
        return selection;
    }

    //Checks the user's datatype input; whether it is integer, float ,or string, continues requesting until the value is correct
    private static Object validateDatatype(int dataType) {
        Object value = null;
        while (value == null) {
            try {
                if (dataType == 0)
                    value = input.nextInt();
                else if (dataType == 1)
                    value = input.nextFloat();
                else if (dataType == 2)
                    value = input.next();
                if (value == null)
                    throw new DatatypeMismatchedException("Invalid input");
            } catch (Exception e) {
                printToConsole("Invalid input", 1, true);
                input.nextLine();
            }
        }
        return value;
    }

    //Checks the user's mark input; whether its between 0 or 100 ,continues requesting until the value is correct
    private static float validateRange(float mark) {
        while (mark < 0 || mark > 100) {
            try {
                if (mark > 0 || mark < 100)
                    throw new InvalidMarkRange("Invalid mark");
            } catch (Exception e) {
                printToConsole(e.getMessage(), 1, true);
                mark = (float) validateDatatype(1);
            }
        }
        return mark;
    }

    //A function that fills the hashmaps with template data as well as manipulates them (Can be removed safely)
    static void initializeDB() {
        enterTemplateData();
        setAssignmentTo(4, 2);
        setAssignmentTo(4, 1);
        setAssignmentTo(3, 2);
        setAssignmentToAll(3);
        setAssignmentToAll(2);
        setAssignmentMarkTo(4, 1, 60);
        setAssignmentMarkTo(4, 2, 90);
        setAssignmentMarkTo(3, 2, 49);
        setAssignmentMarkTo(3, 1, 80);
        setAssignmentMarkToAllBatch(4, 80);
        setAssignmentMarkToAllBatch(2, 75);
    }

    //A function that fills the hashmaps with template data
    private static void enterTemplateData() {
        addStudent(1, "Ahmad");
        addStudent(2, "Mohammad");
        addStudent(3, "Hamed");
        addStudent(4, "Tamer");
        addStudent(5, "Hamza");
        addStudent(6, "Yousef");
        addStudent(7, "Ali");
        addStudent(8, "Ahmad");


        addAssignment(1, "Java Basics", 2);
        addAssignment(2, "XML Basics", 1);
        addAssignment(3, "Firebase DB", 3);
        addAssignment(4, "Firebase Authentication API", 5);
    }
}
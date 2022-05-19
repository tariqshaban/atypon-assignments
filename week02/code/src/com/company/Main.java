package com.company;

import static com.company.CliHandler.*;

public class Main {
    //Don't create a new object manually as it wont be added to the list implicitly, use addStudent/addAssignment instead
    public static void main(String[] args) {
        //Calls a function which handles students/assignments template insertions
        initializeDB();

        //Initializes the CLI by printing the main menu and waiting for an input
        printMainMenu();
    }
}
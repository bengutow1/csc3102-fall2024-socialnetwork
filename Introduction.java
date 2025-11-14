import java.util.Scanner;

/*
* Handles the user-interface
* @author Benjamin Gutowski, Khalil El-abbassi, Robert Ngo, Timothy Posley
*/

public class Introduction {

    public static void main(String[] args) {
        SocialNetwork sn = new SocialNetwork();
        sn.addStudentsFromFile("students.csv");
        sn.addStudentsFromFile("network.csv");
        sn.print();

        // Create a scanner to read user input from the console
        Scanner scanner = new Scanner(System.in);
        String input;

        // Display the command menu
        printMenu();
        System.out.println();

        // Main program loop that waits for user input
        while (true) {
            input = scanner.nextLine();

            // Check if the user wants to exit the program
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("\nEnding program");
                break;

                // Check if the user wants to display the menu again
            } else if (input.equalsIgnoreCase("menu")) {
                System.out.println();
                printMenu();

                // Option 1: View a student's network
            } else if (input.equals("1")) {
                System.out.println("\nEnter the enrollment number of the student who's network you want to check:\n");
                while (true) {
                    input = scanner.nextLine();
                    // Check if the user wants to cancel the action or return to the menu
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        // Parse the input as an order ID
                        int enrollmentID = Integer.parseInt(input);
                        if (enrollmentID <= 0 || enrollmentID > sn.students.size()) {
                            System.out.println("\nThat enrollment number doesn't exist!\n");
                        } else {
                            System.out.println();
                            //Print the network of the selected student
                            sn.printNetwork(enrollmentID);
                            System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }

                // Option 2: Dijkstra's algorithm
            } else if (input.equals("2")) {
                System.out.println("\nEnter the enrollment number of the student to start at:\n");
                boolean cancelling = false;
                while (true) {
                    if (cancelling) {
                        break;
                    }
                    input = scanner.nextLine();
                    // Check if the user wants to cancel the action or return to the menu
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        // Parse the input as an enrollment ID
                        int enrollmentID = Integer.parseInt(input);
                        if (enrollmentID <= 0 || enrollmentID > sn.students.size()) {
                            System.out.println("\nThat enrollment number doesn't exist!\n");
                        } else {
                            // Ask for the student to remove from student 1's network
                            System.out.println("\nEnter the enrollment ID of the student you want to get to:\n");
                            while (true) {
                                input = scanner.nextLine();
                                // Check if the user wants to cancel the action or return to the menu
                                if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                                    System.out.println("\nCancelling command and returning to the menu\n");
                                    printMenu();
                                    cancelling = true;
                                    break;
                                }
                                try {
                                    int enrollmentID2 = Integer.parseInt(input);
                                    if (enrollmentID2 <= 0 || enrollmentID2 > sn.students.size()) {
                                        System.out.println("\nThat enrollment number doesn't exist!\n");
                                    } else {
                                        System.out.println();
                                        //Attempt to remove student 2 from student 1's network
                                        sn.findShortestPath(enrollmentID, enrollmentID2);
                                        System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                                        cancelling = true;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nYou must enter a number!\n");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }
                // Option 3: Remove a student from another student's network
            } else if (input.equals("3")) {
                System.out.println("\nEnter the enrollment number of the student who's network you modify:\n");
                boolean cancelling = false;
                while (true) {
                    if (cancelling) {
                        break;
                    }
                    input = scanner.nextLine();
                    // Check if the user wants to cancel the action or return to the menu
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        // Parse the input as an enrollment ID
                        int enrollmentID = Integer.parseInt(input);
                        if (enrollmentID <= 0 || enrollmentID > sn.students.size()) {
                            System.out.println("\nThat enrollment number doesn't exist!\n");
                        } else {
                            // Ask for the student to remove from student 1's network
                            System.out.println("\nEnter the enrollment ID of the student you want to remove from " + enrollmentID + " - " + sn.students.get(enrollmentID - 1).name + "'s network:\n");
                            while (true) {
                                input = scanner.nextLine();
                                // Check if the user wants to cancel the action or return to the menu
                                if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                                    System.out.println("\nCancelling command and returning to the menu\n");
                                    printMenu();
                                    cancelling = true;
                                    break;
                                }
                                try {
                                    int removalEnrollmentID = Integer.parseInt(input);
                                    if (removalEnrollmentID <= 0 || removalEnrollmentID > sn.students.size()) {
                                        System.out.println("\nThat enrollment number doesn't exist!\n");
                                    } else {
                                        System.out.println();
                                        //Attempt to remove student 2 from student 1's network
                                        sn.disconnectConnection(enrollmentID, removalEnrollmentID);
                                        System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                                        cancelling = true;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nYou must enter a number!\n");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }
                // Option 4: Set the number of wait days for a student
            } else if (input.equals("4")) {
                System.out.println("\nEnter the enrollment number of the student who's wait time you want to modify:\n");
                boolean cancelling = false;
                while (true) {
                    if (cancelling) {
                        break;
                    }
                    input = scanner.nextLine();
                    // Check if the user wants to cancel the action or return to the menu
                    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                        System.out.println("\nCancelling command and returning to the menu\n");
                        printMenu();
                        break;
                    }
                    try {
                        // Parse the input as an enrollment ID
                        int enrollmentID = Integer.parseInt(input);
                        if (enrollmentID <= 0 || enrollmentID > sn.students.size()) {
                            System.out.println("\nThat enrollment number doesn't exist!\n");
                        } else {
                            // Ask for the student to remove from student 1's network
                            System.out.println("\nEnter the new number of wait days for " + enrollmentID + " - " + sn.students.get(enrollmentID - 1).name + ":\n");
                            while (true) {
                                input = scanner.nextLine();
                                // Check if the user wants to cancel the action or return to the menu
                                if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("menu")) {
                                    System.out.println("\nCancelling command and returning to the menu\n");
                                    printMenu();
                                    cancelling = true;
                                    break;
                                }
                                try {
                                    int newWait = Integer.parseInt(input);
                                    if (newWait < 0) {
                                        System.out.println("\nThe number of wait days can't be below 0!\n");
                                    } else {
                                        System.out.println();
                                        //Set the new number of wait days for the student
                                        sn.setWaitDays(enrollmentID, newWait);
                                        System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
                                        cancelling = true;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nYou must enter a number!\n");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nYou must enter a number!\n");
                    }
                }
                // Option 5: Print the full list of students and their information.
            } else if (input.equals("5")) {
                System.out.println();
                // Find and print the oldest book order
                sn.print();
                System.out.println("Operation complete. Type \"menu\" to view the commands as needed");
            } else {
                System.out.println("\nInvalid command. For a list of commands, type \"menu\"");
            }
            System.out.println();
        }

        // Close the scanner to prevent resource leaks
        scanner.close();
    }

    // Method to print the command menu for the user
    public static void printMenu() {
        System.out.println("COMMAND LIST:");
        System.out.println("\"menu\" - View the command list");
        System.out.println("\"1\" - Print a student's network list");
        System.out.println("\"2\" - Find the shortest path for a student to connect to another");
        System.out.println("\"3\" - Remove a connection from a student's network");
        System.out.println("\"4\" - Set the number of wait days for a student");
        System.out.println("\"5\" - Print the full list of students and their information");
        System.out.println("\nFor all commands, select students using their enrollment number");
        System.out.println("To execute any of these commands, type the associated number that's in quotations");
        System.out.println("To return to the menu when in a command, type \"cancel\" or \"menu\"");
        System.out.println("To exit the program, type \"exit\"");
    }

}

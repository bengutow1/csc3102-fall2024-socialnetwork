import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Stack;

/*
* Handles the graph and methods to modify node information
* @author Benjamin Gutowski, Khalil El-abbassi, Robert Ngo, Timothy Posley
*/

public class SocialNetwork {

    static int NETWORK_SIZE = 5;
    static int DEFAULT_WAIT = 1;

    //Student class acts as the node in the graph and stores relevant information.
    public class Student {

	public int distance = Integer.MAX_VALUE;
	public int connectedFrom = -1;

        public int[] network = new int[NETWORK_SIZE];
        public int wait = DEFAULT_WAIT;
        public int enrollmentID;
        public String name;

        //Constructor for creating a new instance of the Student class
        public Student(String name, int wait, int enrollmentID, int[] network) {
            this.name = name;
            this.wait = wait;
            this.enrollmentID = enrollmentID;
            for (int i = 0; i < network.length; i++) {
                this.network[i] = network[i];
            }
        }
    }

    public ArrayList<Student> students;

    public SocialNetwork() {
        students = new ArrayList();
    }

    /*
    * Method used for adding a student to the graph (ArrayList students).
    * input: Student s - student to add
    */
    public void addStudent(Student s) {
        if (s.enrollmentID > 0) {
            boolean added = false;
            /* Whenever the enrollment ID isn't yet in the students ArrayList, fills the ArrayList with "empty" students
              To ensure correct placement */
            if (students.size() < s.enrollmentID) {
                fill(s.enrollmentID - students.size());
                added = true;
            }
            //Takes and updates the already existing student with info from new student (necessary since both documents contain separate info)
            Student curS = students.get(s.enrollmentID - 1);
            curS.enrollmentID = s.enrollmentID;
            if (s.wait != -1) {
                curS.wait = s.wait;
            }
            if (!s.name.equals("")) {
                curS.name = s.name;
            }
            if (!Arrays.equals(s.network, new int[NETWORK_SIZE])) {
                for (int i = 0; i < curS.network.length; i++) {
                    curS.network[i] = s.network[i];
                }
            }
            //Sets the new student based on new and old information
            students.set(s.enrollmentID - 1, curS);
            if (added) {
                System.out.println("Successfully added student: " + curS.name);
            } else {
                System.out.println("Successfully modified student: " + curS.name);
            }
        } else {
            System.out.println("Couldn't add student: invalid enrollment ID (must be > 0)");
        }
    }

    //Helper method to fill the array with empty students when adding a new student.
    public void fill(int fillAmount) {
        for (int i = 0; i < fillAmount; i++) {
            Student newStudent = new Student("", -1, -1, new int[NETWORK_SIZE]);
            students.add(newStudent);
        }
    }

    /*
    * Method for printing the entire list of students along with their name, wait, and network.
    */
    public void print() {
        System.out.println("Printing students (total amount: " + students.size() + "): \n");
        //Repeats through each student
        for (int i = 0; i < students.size(); i++) {
            Student curS = students.get(i);
            String curSNetworks = "";
            boolean firstConnection = false;
            //Repeats through a student's network and constructs a string of all the connections
            for (int j = 0; j < curS.network.length; j++) {
                if (curS.network[j] > 0 && curS.network[j] <= students.size()) {
                    if (firstConnection) {
                        curSNetworks += ", ";
                    }
                    curSNetworks += curS.network[j];
                    firstConnection = true;
                }
            }
            //Sets the network string to "EMPTY" whenever there's no connection
            if (!firstConnection) {
                curSNetworks = "EMPTY";
            }
            System.out.println(curS.enrollmentID + " - " + curS.name + "; Wait: " + curS.wait + ", Network: " + curSNetworks);
        }
        System.out.println("\nFinished printing\n");
    }

    /*
    * Method for printing a specific student's information
    * input: int studentEnrollmentID - the enrollment ID of the student to print the network of
    */
    public void printNetwork(int studentEnrollmentID) {
        if (studentEnrollmentID <= students.size() && studentEnrollmentID > 0) {
            Student s = students.get(studentEnrollmentID - 1);
            System.out.println("Printing network for: " + s.enrollmentID + " - " + s.name + "\n");
            System.out.println("Number of wait days: " + s.wait + "\n");
            boolean hasAConnection = false;
            //Loops through the student's network, printing each student that the selected student has a connection with.
            for (int i = 0; i < s.network.length; i++) {
                if (s.network[i] > 0) {
                    System.out.println(s.network[i] + " - " + students.get(s.network[i] - 1).name);
                    hasAConnection = true;
                }
            }
            if (hasAConnection) {
                System.out.println("\nFinished printing " + s.name + "'s network\n");
            } else {
                System.out.println(s.enrollmentID + " - " + s.name + " doesn't have a connection with any students :(\n");
            }
        } else {
            System.out.println("There's no student with an enrollment number of " + studentEnrollmentID + "\n");
        }
    }

    /*
    * Method using Djikstra's Algorithm to find the shortest path of connection between any 2 students
    * input: int startingEN - enrollment number of the student to start from
    * input: int endingEN - enrollment number of the student to get to
    */
    public void findShortestPath(int startingEN, int endingEN) {
        int startInd = startingEN - 1;
        int endInd = endingEN - 1;
        if ((startingEN <= 0 || startingEN > students.size() || (endingEN <= 0 || endingEN > students.size()))) {
            System.out.println("One of the enrollment numbers given doesn't exist!\n");
        } else {
            boolean[] visited = new boolean[students.size()];
            //Resets every students' total distance and connectedFrom values to ensure the algorithm works properly
            for (int i = 0; i < students.size(); i++) {
                students.get(i).distance = Integer.MAX_VALUE;
                students.get(i).connectedFrom = -1;
            }
            students.get(startInd).distance = 0;
            students.get(startInd).connectedFrom = startInd;
            //Creates a PriorityQueue with a new comparator that prioritizes the student with the lowest distance.
            PriorityQueue<Student> pq = new PriorityQueue<>(new Comparator<Student>() {
                @Override
                public int compare(Student x, Student y) {
                    return x.distance - y.distance;
                }
            });
            boolean endIndNotFound = true;
            pq.offer(students.get(startInd));
            //While the Priority Queue isn't empty and the destination hasn't yet been reached, loops through the next student
            while (!pq.isEmpty() && endIndNotFound) {
                Student curS = pq.poll();
                int curInd = curS.enrollmentID - 1;
                if (curInd == endInd) {
                    endIndNotFound = false;
                    continue;
                }
                if (visited[curInd]) {
                    continue;
                }
                visited[curInd] = true;
                int curTotalWait = curS.distance;
                int[] curNetwork = students.get(curInd).network;
                //Loops through the current student's network, adding any student that hasn't been visited yet.
                for (int i = 0; i < curNetwork.length; i++) {
                    int curSInNetIndex = curNetwork[i] - 1;
                    if (curNetwork[i] <= 0 || curNetwork[i] > students.size()) {
                        continue;
                    }
                    Student curSInNet = students.get(curSInNetIndex);
                    int newWait = curTotalWait + curS.wait;
                    if (newWait < curSInNet.distance) {
                        curSInNet.distance = newWait;
                        curSInNet.connectedFrom = curInd;
                        pq.offer(curSInNet);
                    }
                }
            }
            if (endIndNotFound) {
                System.out.println("There is no connection possible from " + startingEN + " - " + students.get(startingEN - 1).name + " to " + endingEN + " - " + students.get(endingEN - 1).name + "\n");
            } else {
                int nextStudentIndex = endInd;
                int pathLength = 1;
                Stack<Integer> connectedStudents = new Stack();
                connectedStudents.push(endInd + 1);
                //Gets the full list of connected students to print the proper order of connections from one student to another
                while (students.get(nextStudentIndex).connectedFrom != startInd) {
                    connectedStudents.push(students.get(nextStudentIndex).connectedFrom + 1);
                    nextStudentIndex = students.get(nextStudentIndex).connectedFrom;
                    pathLength++;
                }
                if (startInd != endInd) {
                    connectedStudents.push(startInd + 1);
                    pathLength++;
                }
                System.out.println("Connection found from " + startingEN + " - " + students.get(startingEN - 1).name + " to " + endingEN + " - " + students.get(endingEN - 1).name + "\n");
                System.out.print("Path (using enrollment numbers): ");
                //Prints the list of connections that connects the starting student to the ending student
                while (!connectedStudents.isEmpty()) {
                    int curSInd = connectedStudents.pop();
                    System.out.print(curSInd);
                    if (!connectedStudents.isEmpty()) {
                        System.out.print(" --> ");
                    } else {
                        System.out.print("\n");
                    }
                }
                System.out.println("Number of nodes (students) on the path: " + pathLength);
                System.out.println("Minimum number of days until connection is formed: " + students.get(endInd).distance + "\n");
            }
        }
    }

    /*
    * Method to remove the specified connection from a student (if it exists)
    * input: int studentEnrollmentID - student to modify the connection of
    * input: int connectionID - student to remove from student x's network
    */
    public void disconnectConnection(int studentEnrollmentID, int connectionID) {
        if (studentEnrollmentID <= students.size() && studentEnrollmentID > 0) {
            if (connectionID <= students.size() && connectionID > 0) {
                Student s = students.get(studentEnrollmentID - 1);
                boolean foundNetwork = false;
                //Loops through the students' network to ensure that the specified connection does exist
                for (int i = 0; i < s.network.length; i++) {
                    if (connectionID == s.network[i]) {
                        foundNetwork = true;
                        System.out.println("Successfully removed " + s.enrollmentID + " - " + s.name + "'s connection with: " + s.network[i] + " - " + students.get(s.network[i] - 1).name + "\n");
                        s.network[i] = -1;
                    }
                }
                if (!foundNetwork) {
                    System.out.println(s.enrollmentID + " - " + s.name + " isn't connected to " + students.get(connectionID - 1).enrollmentID + " - " + students.get(connectionID - 1).name + "\n");
                }
            } else {
                System.out.println("There's no student with an enrollment number of " + connectionID + " (your number for the student to remove from the other's network)\n");
            }
        } else {
            System.out.println("There's no student with an enrollment number of " + studentEnrollmentID + " (your number for the student to remove a connection from)\n");
        }
    }

    /*
    * Method to set a new wait time for a student
    * input: int studentEnrollmentID - enrollment number of the student to modify
    * input: int newWait - new number of days it takes for the student to form a new connection (can't be below 0)
    */
    public void setWaitDays(int studentEnrollmentID, int newWait) {
        if (studentEnrollmentID > 0 && studentEnrollmentID <= students.size()) {
            if (newWait >= 0) {
                Student s = students.get(studentEnrollmentID - 1);
                s.wait = newWait;
                System.out.println("Successfully updated the wait days for " + s.enrollmentID + " - " + s.name + " to " + newWait + "\n");
            } else {
                System.out.println("The new amount of wait days cannot be below 0\n");
            }
        } else {
            System.out.println("There's no student with an enrollment number of " + studentEnrollmentID + "\n");
        }
    }

    /*
    * Method to add students from a specified document located in the same folder
    * input: String fileName - file name to add from (include suffix (.csv, for ex.))
    */
    public void addStudentsFromFile(String fileName) {
        // Attempt to open the file and read from it
        try (InputStream inputStr = SocialNetwork.class
                .getResourceAsStream(fileName); BufferedReader br = new BufferedReader(new InputStreamReader(inputStr))) {

            // Check if the file is found
            if (inputStr == null) {
                System.out.println("File can't be found!\n");
                return;
            }

            // Process each line in the file (expected format requires the first line to be a list of categories)
            System.out.println("Reading the file: " + fileName + "\n");
            int curLine = 0;
            String line = br.readLine();
            String[] columns = null;
            while (line != null) {
                boolean successfulRead = true;
                curLine++;
                String curStudentFirstName = "";
                String curStudentLastName = "";
                int curStudentWait = -1;
                int curStudentID = -1;
                int[] curStudentNetwork = new int[5];
                String[] values = line.split(",");

                //Reads the first line to determine what each column is. Valid column titles: Enrollment, First name, Last name, Wait, Connection [1-5]
                if (curLine == 1) {
                    columns = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        columns[i] = values[i];
                    }
                } else {
                    for (int i = 0; i < values.length; i++) {
                        if (i >= columns.length || !successfulRead) {
                            break;
                        }
                        String[] connectionCheck = columns[i].split(" ");
                        if (columns[i].equals("Enrollment")) {
                            try {
                                curStudentID = Integer.parseInt(values[i]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error when reading enrollment id: not a number; ignoring line");
                                successfulRead = false;
                            }
                        } else if (columns[i].equals("First name")) {
                            curStudentFirstName = values[i];
                        } else if (columns[i].equals("Last name")) {
                            curStudentLastName = values[i];
                        } else if (columns[i].equals("Wait")) {
                            try {
                                curStudentWait = Integer.parseInt(values[i]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error when reading wait time: not a number; ignoring line");
                                successfulRead = false;
                            }
                        } else if (connectionCheck[0].equals("Connection") && connectionCheck.length > 1) {
                            try {
                                int networkIndex = Integer.parseInt(connectionCheck[1]);
                                try {
                                    int curStudentConnection = Integer.parseInt(values[i]);
                                    curStudentNetwork[networkIndex - 1] = curStudentConnection;
                                } catch (NumberFormatException e) {
                                    System.out.println("Error when reading network connection pointer: not a number; ignoring line");
                                    successfulRead = false;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error when reading network connection number: not a number; ignoring line");
                                successfulRead = false;
                            }
                        }
                    }
                }
                if (successfulRead && curLine != 1) {
                    if (curStudentID != -1) {
                        //Successfully got enough information to add Student!
                        Student newStudent = new Student(curStudentFirstName + " " + curStudentLastName, curStudentWait, curStudentID, curStudentNetwork);
                        this.addStudent(newStudent);
                    } else {
                        System.out.println("Couldn't find enrollment ID, skipping line");
                    }
                }
                // Read the next line in the file
                line = br.readLine();
            }
            System.out.println("Finished reading the file: " + fileName + "\n");
        } catch (Exception e) {
            // Prints an error message if the file cannot be found or read
            System.out.println("File can't be found!\n");
        }
    }
}

# CSC3102 Social Network Group Project

Authors: Khalil El-abbassi, Benjamin Gutowski, Robert Ngo, Timothy Posley

This is a social network simulator project I made with a group. The program finds the quickest path (using Djikstra's Algorithm) of connections from one student to another given that each student has an "introduction time" it takes for them to meet new people and a network of five other students they're connected to. The program reads a .csv file containing all of the social network information and converts the data into an array of Student objects. The user is provided with a command line interface to interact with the social network. 

---

## How to run:

Clone the repository:
```bash
git clone https://github.com/bengutow1/csc3102-fall2024-socialnetwork.git
```

Step into the folder:
```bash
cd csc3102-fall2024-socialnetwork
```

Compile the program and run with the following commands:
```bash
javac *.java
java Introduction
```

## Features
+ Print a student's network (five or less students currently connected to the given student)
+ Find the quickest (least amount of days) path for a student to connect to another
+ Remove a connection from a student's network
+ Set the "introduction time" (number of wait days) for a student
+ Print the full list of students and their information



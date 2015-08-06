package topologicalsortsourceremoval;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TopologicalSortSourceRemoval {

    static ArrayList<Integer> topSort = new ArrayList<>();          //Topological Array
    static ArrayList<Integer> emptyset = new ArrayList<>();         //Temporary Array which handles source removal
    static int numPoints = 0;                                       //intialize number of Nodes - first number on file
    static int numEdges = 0;                                        //intialize number of Edges - second number on file
    static int[][] edges = null;                                    //intialize double array of directed connections

    public static void main(String[] args) throws IOException {

        if (args.length == 0 || !new File(args[0]).exists()) {      //file validation
            System.out.println("File not found.");
        } 
        else {
            readFile(args[0]);                                      //method to read the file
            for (int q = 0; q < numPoints; q++) {                   //cycle through the methods "numPoints" times
                dependents_check();                                 //checks to see if there are any nodes that are independent
                remove_node();                                      //removes said node by looping it unto itself
            }
        }

        if (topSort.size() < numPoints) {                           
            System.out.println("Graph is not a DAG. Cannot perform a topological sort.");
            //if the size of the topologial sort is not the number of initial points
            //then the graph can not be a DAG, and therefore can not have a feasible topological sort
        } 
        else {
            System.out.println("Topological sort: " + topSort);
        }
    }

    public static void readFile(String filename) throws IOException {

        Scanner scan = new Scanner(new File(filename));     //scans in file

        numPoints = scan.nextInt();                         //sets numPoints to first read line in file
        numEdges = scan.nextInt();                          //sets numEdges to second read line in file
        edges = new int[numEdges][2];                       //sets edges to be a 2 dimensional array 
                                                            //each column (numEdges) has two rows (2)
        for (int i = 0; i < numEdges; i++) {
            edges[i][0] = scan.nextInt();                   //scan in the directed graph
            edges[i][1] = scan.nextInt();                   
        }
    }

    public static void dependents_check() {
        for (int i = 0; i < numPoints; i++) {           //i represents the node we are looking for
            int count = 0;                              //that node must be independent, not having any edges directing towards it
            for (int j = 0; j < edges.length; j++) {    //goes through each of the destination nodes to see if i is dependent
                if (i == edges[j][1]) {                 
                    count++;                            //occurs if i is dependent
                }
            }
            if (count == 0) {                           //add i to the empty set and breaks the loop once count = 0
                emptyset.add(i);                        //count = 0 means that i is independent
                break;                                  
            }   
        }
    }

    public static void remove_node() {                  
        for (int i = 0; i < emptyset.size(); i++) {     //for each index in emptyset (the temporary array)
            for (int j = 0; j < edges.length; j++) {    //compare it with all edges j
                if (emptyset.get(i) == edges[j][0]) {   //takes all edges associated with node i
                    edges[j][0] = emptyset.get(i);      //sets them equal to node i looping to itself
                    edges[j][1] = emptyset.get(i);
                }
            }
            topSort.add(emptyset.get(i));               //adds the value of the index at emptyset into topSort
        }
        emptyset.clear();                               //clears the temporary ArrayList to start fresh
    }
}
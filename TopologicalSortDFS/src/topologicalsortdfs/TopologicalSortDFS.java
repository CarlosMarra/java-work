package topologicalsortdfs;

import java.util.*;
import java.io.*;

public class TopologicalSortDFS {

    static boolean[][] adj;                                 //initialization of the adjacency matrix 
    static int numEdges = 0;                                //represents the number of edges the graph has
    static int numPoints = 0;                               //represents the number of nodes the graphs has
    static boolean[] visited;                               //initialization of the visited array, this acts as a check to
                                                            //determine if a node has already been visited or not.
    static Stack<Integer> pop_order = new Stack<>();        //a stack that records each pop from the visit stack
    static Stack<Integer> visit_stack = new Stack<>();      //a temporary stack that is used find the topological sort
    static ArrayList<Integer> topSort = new ArrayList<>();  //the arraylist that will hold the final topological sort

    public static void main(String[] args) throws IOException {

        if (args.length == 0 || !new File(args[0]).exists()) {      //file validation
            System.out.println("File not found.");
        } 
        else {  readFile(args[0]);  }                               //method that reads in the file
        
        int F = 0;
        for (int T = 0; T < numPoints; T++) {
            if (adj[F][T] == true && visited[T] == false){            

                //if the value of adj[from][to] is an actual edge and the node T has not been visited yet
                //then push F into the visit stack, set T equal to F and T equal to 0
                //in essence the for loop is travelling from node F to node T
                //the for loop will then continue to repeat until this condition is met again OR...

                visit_stack.push(F);
                F = T;
                T = 0;
            } 
            
            else if ((T == numPoints - 1)){
                
                //... if T has reached it's "last" iteration of the foor loop
                //when this happens, it is time to backtrack, so the visited boolean for index F becomes true
                //then the value of F gets pushed into the pop_order stack while visit_stack gets popped
            
                visited[F] = true;
                
                pop_order.push(F);
                
                if (visit_stack.size() > 0)                         //prevents emptystackexception
                    F = visit_stack.peek();
                
                else 
                    F = 1;
                
                if (visit_stack.size() > 0)                         //prevents emptystackexception
                    visit_stack.pop();
                
                T = 0;                                              //resets T to 0
            }
            
            if (pop_order.size() == numPoints) {

                for (int q = numPoints; q > 0; q--)                 //Inputting the topological sort into topSort 
                    topSort.add(pop_order.pop());
                
                System.out.println("Topological sort: " + topSort);

                break;
            }
            if (visit_stack.size() > numPoints) {                   
                //If the graph is not a DAG, an infinite loops occurs,
                //therefore to counter this, this statement will break the loop.
                System.out.println("Graph is not a DAG. Cannot perform a topological sort.");
                break;
            }
        }
    }

    public static void readFile(String filename) throws IOException {
        Scanner scan = new Scanner(new File(filename));             //Scans the file
        numPoints = scan.nextInt();                                 //sets numPoints to the first number in file      
        visited = new boolean[numPoints];                           //ser
        adj = new boolean[numPoints][numPoints];                    //The adj 2d array becomes numPoints x numPoints large
        numEdges = scan.nextInt();                                  //sets numEdges to the first number in file    

        for (int i = 0; i < numEdges; i++) {                        //for loops that 

            int x = scan.nextInt();                                 //Scans the node that an edges starts with and
            int y = scan.nextInt();                                 //Scans the node that an edges ends with

            adj[x][y] = true;                                       //then each edges is entered into the adj 2D array as true
        }
    }
}
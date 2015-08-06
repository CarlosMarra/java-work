package convexhull;

import java.io.*;   //importing exceptions
import java.util.*; //importing all the things

public class ConvexHull {       

    static LinkedList<Point> convexHullPoints = new LinkedList<Point>();    //where the convex hull points go for math()
    static HashSet<Point> cHP = new HashSet<Point>();                       //where the convex hull points go for slope()

    static int numPairs = 0;            //intialize number of points - first number on file
    static double[][] points = null;    //intialize double array for coordinates

    public static void main(String[] args) throws IOException {
        
        if(args.length == 0 || !new File(args[0]).exists()){    //file validation
            System.out.println("File not found.");
        }
        
        else{
            
            readFile(args[0]);          //method to read the file
            
            math();                     //method to find the convex hull points using formula
            slope();                    //method to find the convex hull points using slope (alternative method)

            saveConvexHullPoints();     //save found points from math();
            saveConvexHullPoints2();    //save found points from slope(); 
        
        }
    }

    public static void readFile(String filename) throws IOException {

        Scanner scan = new Scanner(new File(filename)); //scans in file

        numPairs = scan.nextInt();                      //sets numPairs to first read line in file
        points = new double[numPairs][2];               //sets points to be a 2 dimensional each column (numPairs) has two rows (2)
        
        for (int i = 0; i < numPairs; i++) {
            points[i][0] = scan.nextDouble();           //scan in the coordinates from text file
            points[i][1] = scan.nextDouble();           
        }

    }

    public static void saveConvexHullPoints() throws IOException {  //prints out convex hull points found in math() into
        PrintWriter out = new PrintWriter("output.txt");            //text file called output.txt
        for (Point p : convexHullPoints) {
            out.println((Point) p);
        }
        out.close();
    }

    public static void saveConvexHullPoints2() throws IOException { //prints out convex hull points found in slope() into
        PrintWriter out2 = new PrintWriter("output2.txt");          //text file called output2.txt
        for (Point p : cHP) {
            out2.println((Point) p);
        }
        out2.close();
    }

    public static boolean math() {      //method that finds and adds convex hull points into linkedlist

        for (int i = 0; i < numPairs - 1; i++) {

            for (int j = i + 1; j < numPairs; j++) {

                double x1 = points[i][0] + (Math.random()/1000000);     //slightly perturb each coordinate
                double y1 = points[i][1] + (Math.random()/1000000);
                double x2 = points[j][0] + (Math.random()/1000000);
                double y2 = points[j][1] + (Math.random()/1000000);
                double a = y2 - y1;
                double b = x1 - x2;
                double c = (x1 * y2) - (y1 * x2);                       //formula crunching
                int numUnder = 0;
                int numOver = 0;

                for (int k = 0; k < numPairs; k++) {
                    if (k == i) {                   //skips duplicate coordinates
                        k++;
                    }
                    if (k == j) {
                        k++;
                    }
                    if (k == numPairs) {
                        break;
                    }

                    double ans = a * points[k][0] + b * points[k][1];  

                    if (ans > c) {      //determining if other points that aren't on the given line 
                        numUnder++;     //are on one side of the line or the other
                    }
                    if (ans < c) {
                        numOver++;
                    }
                }
                if (numUnder > 0 && numOver == 0) {     
                    convexHullPoints.add(new Point(points[i][0], points[i][1]));
                                //these two ifs are the conditions that signify that 
                                //the two points used are in fact convex hull points
                }
                if (numUnder == 0 && numOver > 0) {
                    convexHullPoints.add(new Point(points[j][0], points[j][1]));
                }
            }
        }
        return true;
    }

    public static boolean slope() { //alternative method that determines convex hull points (duplicates exist)

        for (int i = 0; i < numPairs - 1; i++) {

            for (int j = 0; j < numPairs; j++) {

                if ((points[i][0] != points[j][0]) && (points[i][1] != points[j][1])) {

                    double x1 = points[i][0] + (Math.random()/1000000); //slightly perturb each coordinate 
                    double y1 = points[i][1] + (Math.random()/1000000);
                    double x2 = points[j][0] + (Math.random()/1000000);
                    double y2 = points[j][1] + (Math.random()/1000000);
                    double m = (y2 - y1) / (x2 - x1);
                    double b = y1 - (m * x1); //slope formula

                    int countpos = 0;
                    int countneg = 0;

                    for (int k = 0; k < numPairs; k++) {

                        if (k == i) {                   //skips duplicate coordinates
                            k++;
                        }
                        if (k == j) {
                            k++;
                        }
                        if (k == numPairs) {
                            break;
                        }

                        double x3 = points[k][0];
                        double y3 = points[k][1];

                        double exp = Math.ceil((m * x3) + b - y3); //apply slope formula to third point

                        if (exp > 0) {
                            countpos++;
                        }
                        if (exp < 0) {
                            countneg++;
                        }
                    }

                    if (countpos > 0 && countneg == 0) {
                        cHP.add(new Point(points[i][0], points[i][1]));
                                //conditions that signify the points given are convex hull points
                    }
                    if (countpos == 0 && countneg > 0) {
                        cHP.add(new Point(points[j][0], points[j][1]));
                    }
                }
            }
        }
        return true;
    }

}

// Leave this class as is. If you want to add functionality, make a sub-class.
class Point {

    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return x + "\t" + y;
    }
}

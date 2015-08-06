package databases.pkgfinal;

import java.util.*;

public class DatabasesFinal {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        int enter;
        int action = 0;

        System.out.println("               Welcome, to Rubio's Trattoria!                  ");
        System.out.println("_______________________________________________________________");
        open_Menu_one();
        enter = scan.nextInt();
        clear_lines();

        while (!((enter >= 1) && (enter <= 3))) {
            System.out.println("                       Invalid entry!                          ");
            System.out.println("_______________________________________________________________");
            open_Menu_one();
            enter = scan.nextInt();
            clear_lines();
        }
        if (enter == 3) //Farewell message
        {
            System.out.println("Good day to you!");
        } 
        else {
            while (enter != 3) {
                if (enter == 1) {
                    do {
                        action_one();
                        
                        action = scan.nextInt();

                        switches_operational(action);

                    } while (((action < 1) || (action > 16)) || (action != 16));
                    if (action == 16) {
                        System.out.println("Exiting running menu...");
                    }
                } else {
                    do {
                        action_two();

                        action = scan.nextInt();
                        
                        switches_managerial(action);
      
                    } while (((action < 1) || (action > 12)) || (action != 12));
                    if (action == 12) {
                        System.out.println("Exiting managing menu...");
                    }
                }
                do {
                    clear_lines();
                    System.out.println("               Welcome, to Rubio's Trattoria!                  ");
                    System.out.println("_______________________________________________________________");
                    open_Menu_one();
                    enter = scan.nextInt();
                    clear_lines();
                } while ((enter != 3) && (enter != 2) && (enter != 1));
            }
            if (enter == 3) // Farewell message
            {
                System.out.println("Good day sir!");
            }
        }
    }

    public static void pauseProg() {
        System.out.println("Press enter to continue...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
    }
    
    public static void action_one() {
        System.out.println("                  You selected: Operational Queries!                 ");
        System.out.println("_____________________________________________________________________");
        System.out.println("\n  1. Retrieve the names of all servers");
        System.out.println("  2. Retrieve a list of all free tables of a given size");
        System.out.println("  3. Assign a party to a free table");
        System.out.println("  4. Sit someone at the bar");
        System.out.println("  5. Assign a server to a party");
        System.out.println("  6. Take orders for the patrons in a party");
        System.out.println("  7. Allow a patron to change his/her order or cancel an ordered item");
        System.out.println("  8. Record any special intructions for preparing the meal");
        System.out.println("  9. Calculate the total due for a party");
        System.out.println("  10. Calculate the total due for an individual diner if check is spliy");
        System.out.println("  11. Allocate a tip for a server (for parties of fewer than 8 patrons)");
        System.out.println("  12. Assess a 20% gratuity (for parties of 8 or more)");
        System.out.println("  13. Free a table for a party that has left");
        System.out.println("  14. Take a reservation");
        System.out.println("  15. Seat a party that had a reservation");
        System.out.println("  16. Return to actions menu");
    }

    public static void action_two() {
        System.out.println("                       You selected: Operational Queries!                       ");
        System.out.println("________________________________________________________________________________");
        System.out.println("\n  1. Compute the total receipts");
        System.out.println("  2. Determine the top 5 items ordered");
        System.out.println("  3. Determine the top 3 drinks ordered");
        System.out.println("  4. Determine the top 3 appetizers ordered");
        System.out.println("  5. Calculate the average number of guests per table");
        System.out.println("  6. Determine how many parties each server served");
        System.out.println("  7. Determine which server recieved the most tips and how much they were");
        System.out.println("  8. Determine which server servered the parties with the highest average bill");
        System.out.println("  9. Determine how long a party took to eat a meal");
        System.out.println("  10. Split the tips among servers, giving the top-tip earner twice as much money");
        System.out.println("  11. Retrieve a list of parties, showing the time they were seated, the time \n"
                + "      they left  and how much they spent");
        System.out.println("  12. Return to the actions menu");
    }

    public static void open_Menu_one() {
        System.out.println("What kind of operations would you like to perform this session?");
        System.out.println("\n  1. Operational");
        System.out.println("  2. Managerial");
        System.out.println("  3. Leave the restarant");
    }

    public static void open_Menu_order() {
        System.out.println("  1. Beverages");
        System.out.println("  2. Appetizers");
        System.out.println("  3. Entrees");
        System.out.println("  4. Desserts");
        System.out.println("  5. I don't want to order");
    }    
    public static void clear_lines() {
        for(int i = 0; i < 100; i++){
            System.out.println();
        }
    }    

    public static void switches_operational(int action) {
        switch (action) {
            case 1:
                methodqueries.printServers();
                break;
            case 2:
                methodqueries.freeTables();
                break;
            case 3:
                methodqueries.assignParty();
                break;
            case 4:
                methodqueries.assignBar();
                break;
            case 5:
                methodqueries.tableInNeed();
                break;
            case 6:
                methodqueries.orders();
                break;
            case 7:
                methodqueries.changeOrder();
                break;
            case 8:
                clear_lines();
                System.out.println("To add special instructions, change the order using option 7.");
                pauseProg();
                clear_lines();
                break;
            case 9:
                methodqueries.calculateTotal();
                break;
            case 10:
                methodqueries.calculateSplitTotal();
                break;
            case 11:
                methodqueries.leaveTip();
                break;
            case 12:                
                clear_lines();
                System.out.println("To calculate this, enter the name of a party \nwho's size is 8 or more in the previous selection.");
                pauseProg();
                clear_lines();
                break;
            case 13:
                methodqueries.partyLeftfreeTable();
                break;
            case 14:
                methodqueries.takeReservation();
                break;
            case 15:
                methodqueries.setReservation();
                break;
        }
    } 

    public static void switches_managerial(int action) {
        switch (action) {
            case 1:
                methodqueries.numReceipts();
                break;
            case 2:
                methodqueries.top5Items();
                break;
            case 3:
                methodqueries.top3Drinks();
                break;
            case 4:
                methodqueries.top3Apps();
                break;
            case 5:
                methodqueries.avgPartySize();
                break;
            case 6:
                methodqueries.numPartiesServed();
                break;
            case 7:
                clear_lines();
                methodqueries.bestTipEarner();
                pauseProg();
                clear_lines();
                break;
            case 8:
                methodqueries.bestSalesperson();
                break;
            case 9:
                methodqueries.lengthOfStay();
                break;
            case 10:
                methodqueries.splitTips();
                break;
            case 11:
                methodqueries.partyTimes();
                break;
        }
    }
}
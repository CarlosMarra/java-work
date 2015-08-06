package databases.pkgfinal;

import static databases.pkgfinal.DatabasesFinal.clear_lines;
import static databases.pkgfinal.DatabasesFinal.open_Menu_order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class subQueries {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String dbUrl = "jdbc:mysql://localhost:3306/restaurant";
    private static final String username = "root";
    private static final String password = "";

    //This method is to show which tables are free: used in assignParty()
    public static int freeTablesSize(int size) {

        String query;
        int assignTable = 0;
        Scanner scan = new Scanner(System.in);
        ArrayList<Integer> tables = new ArrayList<>();

        int adj_size = 0;

        if (!(size == 1
                || size == 2
                || size == 4
                || size == 6
                || size == 8
                || size == 10)) {

            adj_size = size + 1;

            query = "SELECT * FROM seating_area "
                    + "WHERE occupied = FALSE AND table_id != 0 AND seating_area.size = '" + adj_size + "';";
        } else {
            query = "SELECT * FROM seating_area "
                    + "WHERE occupied = FALSE AND table_id != 0 AND seating_area.size = '" + size + "';";
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.print("\nFree Tables:");

            while (rs.next()) {

                int tableid = rs.getInt("table_id");
                System.out.print(" " + tableid);
                tables.add(tableid);
            }

            if (tables.isEmpty()) {

                assignTable = 0;

            } else {

                System.out.println();

                System.out.print("\nWhich table would you like to give them: ");
                assignTable = scan.nextInt();

                boolean choice = false;
                while (!choice) {
                    for (int i = 0; i < tables.size(); i++) {
                        if (assignTable == tables.get(i)) {
                            choice = true;
                        }
                    }
                    if (!choice) {
                        System.out.println("\nInvalid table number, please choose from the one availiable.");
                        System.out.print("\nWhich table would you like to give them: ");
                        assignTable = scan.nextInt();
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return assignTable;
    }

    //shows servers in alphabetical order; used in tableInNees();
    public static int server_id_fullname() {
            
        int server_id = 0;
        String server_needed;
        Scanner scan = new Scanner(System.in);
        ArrayList<String> servers = new ArrayList<>();
        
        String query = "SELECT CONCAT(first_name, ' ', last_name) AS server_name FROM servers WHERE server_id > 0 ORDER BY server_name ASC";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement2 = connection.createStatement();
            ResultSet rs2 = statement2.executeQuery(query);
            
            System.out.println("\nWhich server would you like to assign to the party: ");
            System.out.println("----------------------------------------------------");

            while (rs2.next()) {
                String fullname = rs2.getString("server_name");
                System.out.println(fullname);
                servers.add(fullname);
            }

            System.out.println("----------------------------------------------------");
            System.out.print("Server's Full Name: ");

            server_needed = scan.nextLine();

            boolean choice2 = false;
            while (!choice2) {

                for (int i = 0; i < servers.size(); i++) {
                    if (server_needed.equalsIgnoreCase(servers.get(i))) {
                        choice2 = true;
                    }
                }
                if (!choice2) {
                    System.out.println("Invalid server name, please choose from the one availiable.");
                    System.out.print("Which server would you like to the party: ");
                    server_needed = scan.nextLine();
                }
            }

            String query_server = "SELECT server_id FROM servers WHERE CONCAT(first_name, ' ', last_name) = '" + server_needed + "' ";

            Statement statement3 = connection.createStatement();
            ResultSet rs3 = statement3.executeQuery(query_server);

            while(rs3.next()){
                server_id = rs3.getInt("server_id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return server_id;
    }
    
    public static int party_id_fullname() {
            
        int party_id = 0;
        String party_leader;
        Scanner scan = new Scanner(System.in);
        ArrayList<String> parties = new ArrayList<>();
        
        String query = "SELECT party_name FROM party WHERE time_out IS NULL ORDER BY party_name ASC";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            System.out.println("\nWhich party is involved: ");
            System.out.println("--------------------------------");

            while (rs.next()) {
                String party = rs.getString("party_name");
                System.out.println(party);
                parties.add(party);
            }

            System.out.println("--------------------------------");
            System.out.print("Party Name: ");

            party_leader = scan.nextLine();

            boolean choice2 = false;
            while (!choice2) {

                for (int i = 0; i < parties.size(); i++) {
                    if (party_leader.equalsIgnoreCase(parties.get(i))) {
                        choice2 = true;
                    }
                }
                if (!choice2) {
                    System.out.println("\nInvalid party name, please choose from the ones availiable.");
                    System.out.print("\nWhich party is involved: ");
                    party_leader = scan.nextLine();
                }
            }

            String query_server = "SELECT party_id FROM party WHERE party_name = '" + party_leader + "' ";

            Statement statement3 = connection.createStatement();
            ResultSet rs3 = statement3.executeQuery(query_server);

            while (rs3.next()) {
                party_id = rs3.getInt("party_id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_id;
    }
    
    public static int party_size_fullname(int party_id) {
            
        int party_size = 0;
        
        String query = "SELECT party_size FROM party WHERE party_id = '" + party_id + "'";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement3 = connection.createStatement();
            ResultSet rs3 = statement3.executeQuery(query);

            while (rs3.next()) {
                party_size = rs3.getInt("party_size");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_size;
    }
     
    public static int menu_Choice(int a) {
        int food_id = 0;
        String query = null;
        String course = null;
        String food_choice = null;
        Scanner scan = new Scanner(System.in);
        ArrayList<String> food = new ArrayList<>();

        switch (a) {
            case 1:
                query = "SELECT menu_item, menu_price, menu_description "
                        + "FROM menu_items "
                        + "WHERE menu_id BETWEEN 1000 AND 2000";
                course = "Beverages";
                break;

            case 2:
                query = "SELECT menu_item, menu_price, menu_description "
                        + "FROM menu_items "
                        + "WHERE menu_id BETWEEN 2000 AND 3000";
                course = "Appetizers";
                break;

            case 3:
                query = "SELECT menu_item, menu_price, menu_description "
                        + "FROM menu_items "
                        + "WHERE menu_id BETWEEN 3000 AND 4000";
                course = "Entrees";
                break;

            case 4:
                query = "SELECT menu_item, menu_price, menu_description "
                        + "FROM menu_items "
                        + "WHERE menu_id BETWEEN 4000 AND 5000";
                course = "Desserts";
                break;
        }
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println(course + ": ");
            while (rs.next()) {
                String foodname = rs.getString("menu_item");
                double foodprice = rs.getDouble("menu_price");
                String description = rs.getString("menu_description");
                System.out.println("\t" + foodname + "\t\t" + "$" + foodprice + "\t" + description);
                food.add(foodname);
            }
            System.out.println("\nType 'back' to go back to previous menu.\n");
            food.add("back");
            System.out.print("Food Name: ");

            food_choice = scan.nextLine();

            if (food_choice.equalsIgnoreCase("back")) {

                food_id = 0;

            } else {

                boolean choice2 = false;
                while (!choice2) {

                    for (int i = 0; i < food.size(); i++) {
                        if (food_choice.equalsIgnoreCase(food.get(i))) {
                            choice2 = true;
                        }
                    }
                    if (!choice2) {
                        System.out.println("Invalid food choice, please choose from the ones availiable.");
                        System.out.print("What would you like to order: ");
                        food_choice = scan.nextLine();
                    }
                }

                String query_server = "SELECT menu_id FROM menu_items WHERE menu_item = '" + food_choice + "' ";

                Statement statement3 = connection.createStatement();
                ResultSet rs3 = statement3.executeQuery(query_server);

                while (rs3.next()) {
                    food_id = rs3.getInt("menu_id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return food_id;
    }

    public static void finalize_Order(int foodorder, int partyid, int i, String special) {

        String query = "INSERT INTO orders (party_id, party_member_id, menu_id, requests) VALUES "
                + "('" + partyid + "', '" + i + "', '" + foodorder + "', '" + special + "')";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            System.out.println("\nTHE ORDER IS GIVEN!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int member_orders(int party_id, int party_member) {
            
        Scanner scan = new Scanner(System.in);
        ArrayList<Integer> food_id = new ArrayList<>();
        ArrayList<String> food_name = new ArrayList<>();
        
        String query = "SELECT menu_id FROM orders WHERE party_id = '" + party_id + "' AND party_member_id = '"+party_member+"' ORDER BY menu_id ASC";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int food = rs.getInt("menu_id");
                food_id.add(food);
            }
            
            for(int i = 0; i < food_id.size(); i++){
                String query2 = "SELECT menu_item FROM menu_items WHERE menu_id = '" + food_id.get(i) + "'";
                Statement statement2 = connection.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                while (rs2.next()) {
                    String name = rs2.getString("menu_item");
                    food_name.add(name);
                }
            }

            System.out.println("\nThese are the orders party member " + party_member + " placed\n");
            System.out.println(food_name);
            System.out.print("\nWhich order would you like to change: ");

            String changed_item_name = scan.nextLine();
            

            boolean choice2 = false;
            while (!choice2) {

                for (int i = 0; i < food_name.size(); i++) {
                    if (changed_item_name.equalsIgnoreCase(food_name.get(i))) {
                        choice2 = true;
                    }
                }
                if (!choice2) {
                    System.out.println("Invalid order, please choose from the ones availiable.");
                    System.out.print("Which order would you like to change: ");
                    changed_item_name = scan.nextLine();
                }
            }

            int changed_item_id = 0;
            
            String query3 = "SELECT menu_id FROM menu_items WHERE menu_item = '" + changed_item_name + "'";
            Statement statement3 = connection.createStatement();
            ResultSet rs3 = statement3.executeQuery(query3);

            while (rs3.next()) {
                changed_item_id = rs3.getInt("menu_id");
            }
        
            clear_lines();

            System.out.println("What do you want to do with your order?");
            System.out.println("1. Change order");
            System.out.println("2. Cancel order");
            int remove = scan.nextInt();
            while ((remove > 2) || (remove < 1)) {
                System.out.println("Invalid choice. What do you want to do with your order?");
                System.out.println("1. Change order");
                System.out.println("2. Cancel order");
                remove = scan.nextInt();
            }
            
            if(remove == 1){
                
                int order_made = ordering_process(party_member, party_id);
                
                if(order_made >= 1){
                    
                    remove_order(party_member, party_id, changed_item_id);
                    System.out.println("\norder changed\n");
                }          
            }else{
                remove_order(party_member, party_id, changed_item_id);
                System.out.println("\norder changed\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_id;
    }

    public static int ordering_process(int i, int party_id){
    
            int order_made = 0;
            Scanner scan = new Scanner(System.in);
            boolean exit = false;
            while (exit != true) {
                System.out.println("\nWhat would member " + i + " like to order?");
                open_Menu_order();
                int choice = scan.nextInt();

                while ((choice < 1) || (choice > 5)) {
                    System.out.println("Invalid choice! What would you like to order?");
                    open_Menu_order();
                    choice = scan.nextInt();
                }
                while ((choice > 0) && (choice < 5)) {
                    
                    clear_lines();

                    int order = subQueries.menu_Choice(choice);

                    clear_lines();

                    if (order == 0) {

                    } else {

                        String response = "y";
                        System.out.print("Any special instructions for this order? (Y/N) ");
                        response = scan.nextLine();
                        
                        while ((!response.equalsIgnoreCase("y")) && (!response.equalsIgnoreCase("n"))) {
                            response = scan.nextLine();
                            
                            if (!response.equalsIgnoreCase("y") && !(response.equalsIgnoreCase("n"))){
                                System.out.println("\nPlease answer with a 'y' or 'n'.");
                                System.out.print("\nAny special instructions for this order? (Y/N) ");
                                response = scan.nextLine();
                            }
                        }
                        if (response.equalsIgnoreCase("y")) {
                            System.out.print("\nWhat is your special instruction: ");
                            String special = scan.nextLine();
                            subQueries.finalize_Order(order, party_id, i, special);
                        } else {
                            String nonspecial = "";
                            subQueries.finalize_Order(order, party_id, i, nonspecial);
                        }
                        order_made = 1;
                    }

                    System.out.println("\nWhat else would member " + i + " like to order?");
                    open_Menu_order();
                    choice = scan.nextInt();
                }
                if (choice == 5) {
                    System.out.println("\nOrder for member " + i + " finished!");
                    exit = true;
                }
            }
            return order_made;
    }
    
    public static void remove_order(int party_member, int party_id, int changed_item_id){
    
         String query = "DELETE FROM orders WHERE party_id = '" + party_id + "' AND party_member_id = '"+party_member+"' AND menu_id = '"+changed_item_id+"'";

        
         try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            
        
         } catch (SQLException e) {
            System.out.println(e.getMessage());
        }   
    }

    public static String party_id_fullname_reservation() {
            
        String party_leader = "things";
        Scanner scan = new Scanner(System.in);
        ArrayList<String> parties = new ArrayList<>();
        
        String query = "SELECT party_name FROM party WHERE time_out IS NULL ORDER BY party_name ASC";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
           
            while (rs.next()) {
                String party = rs.getString("party_name");
                parties.add(party);
            }

            System.out.print("Name of reservation: ");

            party_leader = scan.nextLine();

            int choice2 = 0;
            while (choice2 != 2) {

                for (int i = 0; i < parties.size(); i++) {
                    if ((party_leader.equalsIgnoreCase(parties.get(i)))) {
                        choice2 = 1;
                    }
                }
                if (choice2 == 1) {
                    choice2 = 0;
                    System.out.println("Party name in use, please choose another.");
                    System.out.print("Name of reservation: ");
                    party_leader = scan.nextLine();
                }
                else if (choice2 == 0){
                    choice2 = 2;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_leader;
    }
    
    public static String party_reservation() {
            
        String party_leader = "things";
        Scanner scan = new Scanner(System.in);
        ArrayList<String> parties = new ArrayList<>();
        
        String query = "SELECT name FROM reservations ORDER BY name ASC";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            System.out.println("Which reservation wants a table: ");
            System.out.println("--------------------------------");

            while (rs.next()) {
                String party = rs.getString("name");
                System.out.println(party);
                parties.add(party);
            }

            System.out.println("--------------------------------");
            System.out.print("Reservation Name: ");

            party_leader = scan.nextLine();

            boolean choice2 = false;
            while (!choice2) {

                for (int i = 0; i < parties.size(); i++) {
                    if (party_leader.equalsIgnoreCase(parties.get(i))) {
                        choice2 = true;
                    }
                }
                if (!choice2) {
                    System.out.println("Invalid reservation name, please choose from the ones availiable.");
                    System.out.print("Reservation Name: ");
                    party_leader = scan.nextLine();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_leader;
    }
    
    //used in assignParty/Bar
    public static String party_name_check() {
            
        String party_leader = "";
        Scanner scan = new Scanner(System.in);
        ArrayList<String> parties = new ArrayList<>();
        
        String query = "SELECT party_name FROM party";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                String party = rs.getString("party_name");
                parties.add(party);
            }

            System.out.print("\nWhat is the name of your party: ");
            party_leader = scan.nextLine();

            int choice2 = 0;
            while (choice2 != 2) {
                for (int i = 0; i < parties.size(); i++) {
                    if ((party_leader.equalsIgnoreCase(parties.get(i)))) {
                        choice2 = 1;
                    }
                }
                if (choice2 == 1) {
                    choice2 = 0;
                    System.out.println("\nInvalid party name, please choose from the ones availiable.");
                    System.out.print("\nWhat is the name of your party: ");
                    party_leader = scan.nextLine();
                }
                else if (choice2 == 0){
                    choice2 = 2;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return party_leader;
    }
    
}
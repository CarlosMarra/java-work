package databases.pkgfinal;

import static databases.pkgfinal.DatabasesFinal.clear_lines;
import static databases.pkgfinal.DatabasesFinal.open_Menu_order;
import static databases.pkgfinal.DatabasesFinal.pauseProg;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class methodqueries {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String dbUrl = "jdbc:mysql://localhost:3306/restaurant";
    private static final String username = "root";
    private static final String password = "";

    //This method executes Option 1 from Operational Queries
    public static void printServers() {
        clear_lines();
        String query = "SELECT CONCAT(last_name, ', ', first_name) AS server_name FROM servers WHERE server_id > 0 ORDER BY last_name";
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            System.out.println("Currently Employed Servers:");
            System.out.println("---------------------------");

            while (rs.next()) {
                String fullname = rs.getString("server_name");
                System.out.println("Name:\t" + fullname);
            }

            System.out.println("---------------------------");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    //This method executes Option 2 from Operational Queries
    public static void freeTables() {
        clear_lines();
        Scanner scan = new Scanner(System.in);

        System.out.print("Size of the free table (1, 2, 4, 6, 8, 10)?: ");

        int sizeTable = scan.nextInt();

        while (!((sizeTable == 1) || (sizeTable == 8)
                || (sizeTable == 2) || (sizeTable == 4)
                || (sizeTable == 6) || (sizeTable == 10))) {
            System.out.println("Invalid table size. Please enter another size");
            sizeTable = scan.nextInt();
        }

        String query = "SELECT * FROM seating_area "
                + "WHERE occupied = FALSE AND table_id != 0 AND seating_area.size = '" + sizeTable + "';";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            System.out.print("\nFree Tables:");

            while (rs.next()) {

                int tableid = rs.getInt("table_id");
                System.out.print(" " + tableid);
            }
            System.out.println("\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    //This method executes Option 3 from Operational Queries    
    public static int assignParty() {
        clear_lines();
        Scanner scan2 = new Scanner(System.in);
        String partyname = subQueries.party_name_check();

        System.out.print("\nWhat is the size of your party: ");
        int size = scan2.nextInt();

        while ((size < 2) || (size > 10)) {
            System.out.println("\nInvalid party size. Please enter a size between 1 and 10");
            System.out.print("\nWhat is the size of your party: ");
            size = scan2.nextInt();
        }

        int table_id = subQueries.freeTablesSize(size);

        if (table_id != 0) {

            String query = "INSERT INTO party (party_name, table_id, party_size, time_in) "
                    + "VALUES ('" + partyname + "', '" + table_id + "', '" + size + "', NOW());";

            String query2 = "UPDATE seating_area "
                    + "SET occupied = TRUE "
                    + "WHERE table_id = '" + table_id + "'";

            try (Connection connection = DriverManager.getConnection(dbUrl, username, password);) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                Statement statement2 = connection.createStatement();
                statement2.executeUpdate(query2);

                System.out.println("\n" + partyname + " will be seated at table " + (table_id - 1000) + "\n");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            table_id = -1;
            System.out.println("\nThere are currently no available seats at this moment.\n");
        }
        pauseProg();
        clear_lines();
        return table_id;
    }

    public static int assignBar() {

        clear_lines();
        String partyname = subQueries.party_name_check();

        int table_id = subQueries.freeTablesSize(1);

        if (table_id != 0) {

            String query = "INSERT INTO party (party_name, table_id, party_size, time_in) "
                    + "VALUES ('" + partyname + "', '" + table_id + "', 1, NOW());";

            String query2 = "UPDATE seating_area "
                    + "SET occupied = TRUE "
                    + "WHERE table_id = '" + table_id + "'";

            try (Connection connection = DriverManager.getConnection(dbUrl, username, password);) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                Statement statement2 = connection.createStatement();
                statement2.executeUpdate(query2);

                System.out.println("\n" + partyname + " will be seated at stool " + (table_id - 2000) + "\n");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            table_id = -1;
            System.out.println("\nThere are currently no available seats at this moment.");
        }
        pauseProg();
        clear_lines();
        return table_id;
    }

    public static void tableInNeed() {
        clear_lines();

        String table_needed;
        Scanner scan = new Scanner(System.in);
        ArrayList<String> party = new ArrayList<>();

        String querycheck = "SELECT party_name FROM party WHERE server_id = 0";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(querycheck);

            System.out.print("Parties in need of a server:");

            while (rs.next()) {

                String name_party = rs.getString("party_name");
                System.out.print(" " + name_party);
                party.add(name_party);
            }

            if (party.isEmpty()) {

                System.out.println("\nAll tables currently have a server.\n");

            } else {

                System.out.println("\n");
                System.out.print("Which party would you like to assign a server: ");

                table_needed = scan.nextLine();

                boolean choice = false;
                while (!choice) {

                    for (int i = 0; i < party.size(); i++) {
                        if (table_needed.equalsIgnoreCase(party.get(i))) {
                            choice = true;
                        }
                    }
                    if (!choice) {
                        System.out.println("\nInvalid party name, please choose from the one availiable.");
                        System.out.print("\nWhich party would you like to assign a server: ");
                        table_needed = scan.nextLine();
                    }
                }

                int server_id = subQueries.server_id_fullname();

                String query_update_seating = "UPDATE party SET server_id = '" + server_id + "' WHERE party_name = '" + table_needed + "' ";
                Statement statement4 = connection.createStatement();
                statement4.executeUpdate(query_update_seating);

                System.out.println("\nThe " + table_needed + " party has been assigned a server!\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    public static void orders() {
        clear_lines();
        int party_id = subQueries.party_id_fullname();
        int party_size = subQueries.party_size_fullname(party_id);
        clear_lines();
        for (int i = 1; i < party_size + 1; i++) {
            subQueries.ordering_process(i, party_id);
        }
        System.out.println("\nAll members have successfully ordered!\n");
        pauseProg();
        clear_lines();
    }

    public static void changeOrder() {
        clear_lines();
        int party_id;
        int party_size;
        int party_member = 0;
        Scanner scan = new Scanner(System.in);
        party_id = subQueries.party_id_fullname();
        party_size = subQueries.party_size_fullname(party_id);

        clear_lines();
        
        System.out.println("There are " + party_size + " members to this party.");
        System.out.print("\nWhich party member want to edit their order: ");
        party_member = scan.nextInt();

        while (!(party_member > 0 && party_member <= party_size)) {

            System.out.println("Not a valid member number.");
            System.out.print("Which party member want to edit their order: ");
            party_member = scan.nextInt();
        }

        subQueries.member_orders(party_id, party_member);
        pauseProg();
        clear_lines();
    }

    public static double calculateTotal() {
        clear_lines();
        double total = 0;
        int party_id = subQueries.party_id_fullname();

        String query5 = "UPDATE party SET total = "
                + "(SELECT SUM(menu_items.menu_price) FROM menu_items JOIN orders ON menu_items.menu_id = orders.menu_id WHERE orders.party_id = '" + party_id + "')"
                + "WHERE party_id = '" + party_id + "'; ";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            statement.executeUpdate(query5);

            String query6 = "SELECT total FROM party where party_id = '" + party_id + "'";
            Statement statement2 = connection.createStatement();
            ResultSet rs = statement2.executeQuery(query6);

            while (rs.next()) {
                total = rs.getDouble("total");
            }

            Locale locale = new Locale("en", "US");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            System.out.println("\n" + currencyFormatter.format(total) + " is the total of this party\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
        return total;
    }

    public static void calculateSplitTotal() {
        clear_lines();
        int party_id = subQueries.party_id_fullname();

        String query5 = "SELECT DISTINCT orders.party_member_id partygoer, "
                + "(SELECT SUM(menu_items.menu_price) "
                + "FROM menu_items "
                + "JOIN orders ON menu_items.menu_id = orders.menu_id "
                + "WHERE orders.party_member_id = partygoer AND orders.party_id = '" + party_id + "') AS member_total "
                + "FROM orders "
                + "WHERE orders.party_id = '" + party_id + "';";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query5);

            Locale locale = new Locale("en", "US");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            System.out.println("\nEach member and their respective tab:");
            System.out.println("_____________________________________");

            while (rs.next()) {
                System.out.println(rs.getInt("partygoer") + "\t" + currencyFormatter.format(rs.getDouble("member_total")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        pauseProg();
        clear_lines();
    }

    public static void leaveTip() {
       
        clear_lines();
        double tipping = 0;
        Scanner scan = new Scanner(System.in);
        int party_id = subQueries.party_id_fullname();
        int party_size = subQueries.party_size_fullname(party_id);

        String query1 = "UPDATE party SET party.tip = '" + tipping + "' WHERE party_id = '" + party_id + "' AND party_size < 8;";
        String query2 = "UPDATE party SET party.tip = (party.total * .2) WHERE party_id = '" + party_id + "' AND party_size >= 8;";
        String query3 = "SELECT total FROM party WHERE party_id = '" + party_id + "'";
        String query4 = "UPDATE servers SET tips = tips + (SELECT party.tip FROM party WHERE party_id = '" + party_id + "') "
                + "WHERE server_id = (SELECT party.server_id FROM party WHERE party_id = '" + party_id + "')";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            if (party_size < 8) {

                System.out.print("\nHow much did they tip: ");
                tipping = scan.nextDouble();

                boolean valid = false;
                while (!valid) {
                    try {
                        if (tipping >= 0) {
                            valid = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nError, not a number. Please try again.");
                        System.out.print("\nHow much did they tip: ");
                        tipping = scan.nextDouble();
                    }
                }

                Statement statement = connection.createStatement();
                statement.executeUpdate(query1);
                System.out.println("\nYou left $" + tipping + " as a tip\n");

            } else {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query2);

                Statement statement2 = connection.createStatement();
                ResultSet rs = statement2.executeQuery(query3);

                while (rs.next()) {
                    tipping = .2 * rs.getDouble("total");
                }
                System.out.println("You left $" + tipping + " as a tip");
            }

            Statement statement3 = connection.createStatement();
            statement3.executeUpdate(query4);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    public static void partyLeftfreeTable() {
       
        clear_lines();
        int table_id = 0;
        int party_id = subQueries.party_id_fullname();

        String query1 = "UPDATE party SET table_id = 0000 WHERE party_id = '" + party_id + "';";
        String query3 = "SELECT table_id FROM party WHERE party_id = '" + party_id + "';";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query3);

            while (rs.next()) {
                table_id = rs.getInt("table_id");
            }
            statement.executeUpdate(query1);

            String query2 = "UPDATE seating_area SET occupied = FALSE WHERE table_id = '" + table_id + "';";
            statement.executeUpdate(query2);
            
            String query = "UPDATE party SET time_out = NOW() WHERE party_id = '" + party_id + "';";
            statement.executeUpdate(query);           

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\nTable has been freed!\n");
        pauseProg();
        clear_lines();
    }

    public static void takeReservation() {
        clear_lines();
        String nameOfParty = subQueries.party_id_fullname_reservation();

        Scanner scan5 = new Scanner(System.in);

        System.out.print("\nWhat is the size of your party: ");
        int sizeOfParty = scan5.nextInt();

        while ((sizeOfParty < 1) || (sizeOfParty > 10)) {
            System.out.println("Invalid party size. Please enter a size between 1 and 10");
            sizeOfParty = scan5.nextInt();
        }

        String query = "INSERT INTO reservations (name, time, size) "
                + "VALUES ('" + nameOfParty + "', NOW(), '" + sizeOfParty + "');";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("\nReservation recorded!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
    
    public static void setReservation() {
        clear_lines();

        int size = 0;
        String name = subQueries.party_reservation();

        String query2 = "SELECT size "
                + "FROM reservations "
                + "WHERE name = '" + name + "'";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query2);

            while (rs.next()) {
                size = rs.getInt("size");
            }

            int table_id = subQueries.freeTablesSize(size);

            if (table_id != 0) {

                String query = "INSERT INTO party (party_name, table_id, party_size, time_in) "
                        + "VALUES ('" + name + "', '" + table_id + "', '" + size + "', NOW());";

                String query3 = "UPDATE seating_area "
                        + "SET occupied = TRUE "
                        + "WHERE table_id = '" + table_id + "'";

                statement.executeUpdate(query);

                statement.executeUpdate(query3);

                if (size > 1) {
                    System.out.println("\n" + name + " will be seated at table " + (table_id - 1000) + "\n");

                } else {
                    System.out.println("\n" + name + " will be seated at table " + (table_id - 2000) + "\n");
                }
            }

            String query5 = "DELETE FROM reservations WHERE name = '" + name + "'";
            statement.executeUpdate(query5);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    
    
    public static void numReceipts() {
        clear_lines();
        double total = 0;

        String query = "SELECT SUM(total + tip) AS recipts FROM party;";
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                total = rs.getDouble("recipts");
            }
            Locale locale = new Locale("en", "US");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            System.out.println("Total receipts: " + currencyFormatter.format(total) + "\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
       
    public static void top5Items() {
        clear_lines();
        String query = "SELECT menu_items.menu_item delicious,"
                + "(SELECT COUNT(orders.menu_id)"
                + "FROM orders "
                + "JOIN menu_items ON orders.menu_id = menu_items.menu_id "
                + "WHERE menu_items.menu_item = delicious) AS count "
                + "FROM menu_items "
                + "ORDER BY count DESC "
                + "LIMIT 5;";
        
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)){
               
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("Top 5 Menu Items :\n");
            System.out.println("Amount, Item");
            System.out.println("____________");
            while (rs.next()) {
                String foodName = rs.getString("delicious");
                int numOrders = rs.getInt("count");
                System.out.println(numOrders + ",\t" + foodName);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
       
    public static void top3Drinks() {
        clear_lines();
        String query = "SELECT menu_items.menu_item delicious, "
                + "(SELECT COUNT(orders.menu_id) "
                + "FROM orders "
                + "JOIN menu_items ON orders.menu_id = menu_items.menu_id "
                + "WHERE menu_items.menu_item = delicious) AS count "
                + "FROM menu_items "
                + "WHERE menu_items.menu_id BETWEEN 1000 AND 1999 "
                + "ORDER BY count DESC "
                + "LIMIT 3;";
        
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)){
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            System.out.println("Top 3 Drinks :\n");
            System.out.println("Amount, Item");
            System.out.println("____________");
            while (rs.next()) {
                String drinkName = rs.getString("delicious");
                int numOrders = rs.getInt("count");
                System.out.println(numOrders + ",\t" + drinkName);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
            
    public static void top3Apps() {
        clear_lines();
        String query = "SELECT menu_items.menu_item delicious, "
                + "(SELECT COUNT(orders.menu_id) "
                + "FROM orders "
                + "JOIN menu_items ON orders.menu_id = menu_items.menu_id "
                + "WHERE menu_items.menu_item = delicious) AS count "
                + "FROM menu_items "
                + "WHERE menu_items.menu_id BETWEEN 2000 AND 2999 "
                + "ORDER BY count DESC "
                + "LIMIT 3;";
        
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("Top 3 Appetizers :\n");
            System.out.println("Amount, Item");
            System.out.println("____________");
            while (rs.next()) {
                String drinkName = rs.getString("delicious");
                int numOrders = rs.getInt("count");
                System.out.println(numOrders + ",\t" + drinkName);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
    
    public static void avgPartySize() {
        clear_lines();
        double avgGuest = 0;
        String query = "SELECT AVG(party.party_size) AS average_party_size FROM party WHERE party.table_id BETWEEN 1000 AND 1999;";
        try (Connection connection = DriverManager.getConnection(
                dbUrl, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                avgGuest = rs.getDouble("average_party_size");
            }
            System.out.println("The average party size is: " + avgGuest + "\n");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
        
    public static void numPartiesServed() {
        clear_lines();
        ArrayList<Integer> server_id = new ArrayList<>();
        ArrayList<Integer> parties = new ArrayList<>();

        String query = "SELECT DISTINCT party.server_id served, "
                + "(SELECT COUNT(party.server_id) "
                + "FROM party "
                + "WHERE party.server_id = served) AS num_tables "
                + "FROM party;";

        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int fullname = rs.getInt("served");
                int numParties = rs.getInt("num_tables");
                server_id.add(fullname);
                parties.add(numParties);

            }

            String server = "";
            int number = 0;
            for (int i = 1; i < server_id.size(); i++) {
                String query2 = "SELECT CONCAT(last_name, ', ', first_name) AS served FROM servers WHERE server_id = '" + server_id.get(i) + "'";
                Statement statement2 = connection.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                while (rs2.next()) {
                    server = rs2.getString("served");
                }
                number = parties.get(i);

                System.out.println(server + "\tserved " + number + " parties");
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        pauseProg();
        clear_lines();
    }    
   
    public static String bestTipEarner() {
        String fullname = "";
        String query = "SELECT p.server_id, CONCAT(s.first_name, ' ', s.last_name) AS full_name, SUM(tip) as total_tips "
                + "FROM party p JOIN servers s "
                + "ON p.server_id = s.server_id "
                + "GROUP BY server_id "
                + "ORDER BY total_tips DESC "
                + "LIMIT 1";
        try (Connection connection = DriverManager.getConnection(
                dbUrl, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            System.out.println("#1 Tip Earner");
            while (rs.next()) {
                System.out.println("_________________________");
                fullname = rs.getString("full_name");
                int totaltips = rs.getInt("total_tips");

                System.out.println(
                        "Name:\t\t" + fullname);
                System.out.println(
                        "Total tips:\t" + "$" + totaltips);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return fullname;
    }
    
    public static void bestSalesperson() {
        clear_lines();
        String query = "SELECT p.server_id, CONCAT(s.first_name, ' ', s.last_name) AS full_name, FORMAT(AVG(p.total), 2) as avg_check_total "
                + "FROM party p JOIN servers s "
                + "ON p.server_id = s.server_id "
                + "GROUP BY server_id "
                + "ORDER BY avg_check_total DESC "
                + "LIMIT 1";
        try (Connection connection = DriverManager.getConnection(
                dbUrl, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            System.out.println("#1 Salesperson");
            while (rs.next()) {
                System.out.println("_________________________");
                String fullname = rs.getString("full_name");
                int avgBill = rs.getInt("avg_check_total");

                System.out.println(
                        "Name:\t\t" + fullname);
                System.out.println(
                        "Total tips:\t" + "$" + avgBill);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }   
    
    public static void lengthOfStay() {
        clear_lines();
        String query = "SELECT party_name, timediff(time_out, time_in) AS length_of_visit "
                + "FROM party "
                + "WHERE time_in IS NOT NULL AND time_out IS NOT NULL "
                + "GROUP BY party_id";
        try (Connection connection = DriverManager.getConnection(
                dbUrl, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            System.out.println("Time of Stay: ");
            System.out.println("Time, Party Name");
            System.out.println("_____________________________");
            while (rs.next()) {
                String partyname = rs.getString("party_name");
                Time lengthofvisit = (rs.getTime("length_of_visit"));
                System.out.println(lengthofvisit + ",\t" + partyname);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
    
    public static void splitTips() {
        clear_lines();
        double total_tips = 0;
        double num_servers = 0;
        double avg = 0;
        double mvp = 0;
        String toptip = bestTipEarner();
        
        String query = "SELECT SUM(tip) AS tipsy FROM party;";
        String query2 = "SELECT COUNT(server_id) AS best_tips_earner FROM servers;";
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password)){
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                total_tips = rs.getDouble("tipsy");
            }
            
            ResultSet rs2 = statement.executeQuery(query2);
            
            while (rs2.next()) {
                num_servers = rs2.getDouble("best_tips_earner");
            }
            
            avg = total_tips / num_servers;
            mvp = avg * 2;
            
            String query3 = "UPDATE servers SET tips = '"+avg+"' WHERE server_id > 0;";
            String query4 = "UPDATE servers SET tips = '"+mvp+"' WHERE CONCAT(first_name, ' ', last_name) = '"+toptip+"' ;";

            statement.executeUpdate(query3);
            statement.executeUpdate(query4);
            
            System.out.println("Tips have been allocated appropriately.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }

    public static void partyTimes() {
        clear_lines();
        String query = "SELECT party_id, party_name, time_in, time_out, total "
                + "FROM party "
                + "WHERE time_in IS NOT NULL AND time_out IS NOT NULL";
        try (Connection connection = DriverManager.getConnection(
                dbUrl, username, password);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            System.out.println("Party Log: ");
            while (rs.next()) {
                System.out.println("------------------------------");
                String partyname = rs.getString("party_name");
                Time timeseated = (rs.getTime("time_in"));
                Time timeleft = (rs.getTime("time_out"));
                double total = rs.getDouble("total");

                System.out.println(
                        "Party Name:      " + partyname);
                System.out.println(
                        "Time seated:     " + timeseated);
                System.out.println(
                        "Time left:       " + timeleft);
                System.out.println(
                        "Total:           " + "$" + total);
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pauseProg();
        clear_lines();
    }
}
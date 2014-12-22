/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ice.mvc.model;

import ice.json.SliversInfoJSONFileParser;
import ice.mvc.model.DetailedResults;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

/**
 *
 * @author gerard
 */
public class ResultsManager {

    // connection to DB
    Connection connection;
    // List of slivers info (info from JSON file)
    List<Map<String, String>> slivers_info;

    public ResultsManager(String db_path, String slivers_info_json_file) {
        // Open connection to Results DB
        connection = openConnection(db_path);
        // create the slivers_info list
        slivers_info = SliversInfoJSONFileParser.parse(slivers_info_json_file);

    }
    
    public ResultsManager(String slivers_info_json_file) {
        // Open connection to Results DB
        //connection = openConnection(db_path);
        // create the slivers_info list
        slivers_info = SliversInfoJSONFileParser.parse(slivers_info_json_file);

    }

    public ResultSet getNodesListResultSet() {
        // Gets a ResultSet with the name of the different nodes and their corresponding islands
        Statement statement;
        ResultSet resultSet = null;
        try {
            // create new statement
            statement = connection.createStatement();
            // create String with the corresponding SQL query 
            String sql_query = String.format("SELECT dest_node, dest_island FROM ping_result GROUP BY dest_island;");
            // execute the query and get the result set
            resultSet = statement.executeQuery(sql_query);
            // debug printing
            while (resultSet.next()) {
                String dest_node = resultSet.getString("dest_node");
                String dest_island = resultSet.getString("dest_island");
                System.out.println(dest_node + "   " + dest_island);
                System.out.println();
            }
            // close statement
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }

    
    public List<String> getNodesSortedList() {
        // get list with the names of the nodes
        List<String> nodesList = new ArrayList<>();
        List<Map<String, String>> nodesSortedListMap = SliversInfoJSONFileParser.getNodesSortedListMap(slivers_info);
        for (int i = 0; i < nodesSortedListMap.size(); i++) {
            nodesList.add(nodesSortedListMap.get(i).get("node"));
        }
        return nodesList;

    }

    public String getNodesSortedListJSON() {
        // get list with the names of the nodes in JSON format
        JSONArray jsonArray = new JSONArray(getNodesSortedList());
        String jsonString = jsonArray.toString();
        return jsonString;
    }

    public ResultSet getFromToNodeMostRecentResultSet(String src_node, String dest_node) {
        // Get a ResultSet with the results of the most recent ping from src_node to dest_node
        Statement statement;
        ResultSet resultSet = null;
        try {
            // create new statement
            statement = connection.createStatement();
            // Create string with the SQL query
            String sql_query = String.format("SELECT *, max(ts) FROM ping_result WHERE src_node = '%s' AND dest_node = '%s' ORDER BY ts DESC;", src_node, dest_node);
            // execute query and get the results set
            resultSet = statement.executeQuery(sql_query);
            // debug printing
            while (resultSet.next()) {
                String _src_node = resultSet.getString("src_node");
                String src_island = resultSet.getString("src_island");
                String _dest_node = resultSet.getString("dest_node");
                String dest_island = resultSet.getString("dest_island");
                String ts = resultSet.getString("ts");
                System.out.println(src_island + "/" + _src_node + " --> " + dest_island + "/" + _dest_node + "   " + ts);
                System.out.println();
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }

    public ResultSet getNodeMatrixMostRecentResultSet() {
        Statement statement;
        ResultSet resultSet = null;
        // Get the list with the names of the nodes
        List<String> nodesList = getNodesSortedList();
        // number of nodes
        int length = nodesList.size();
        // Create a Matrix to store the results
        String[][] matrix = new String[length][length];

        try {
            // for each node in the list get the results of the pings to other nodes
            for (String node : nodesList) {
                // create new statement
                statement = connection.createStatement();
                // create string witht the SQL query
                String sql_query = String.format("SELECT *, max(ts) FROM ping_result WHERE src_node = '%s' GROUP BY dest_node", node);
                // Execute query and get the results set
                resultSet = statement.executeQuery(sql_query);
                // Close statement
                statement.close();

                // each node in the for corresponds to a row of the matrix
                // each element of the resultsSet corresponds to a cell of this row
                // fill the i-i cell with nothing
                // check for each element of the results set the position where it has to go
                // to do that, get the index of the NodeName in the nodesList 
                // add the prepared information to the correct cell of the matrix
                // for each element in the result cell
                // Prepare the cell with the info
                while (resultSet.next()) {
                    // Get result parameters
                    String src_addr = resultSet.getString("src_addr");
                    String src_node = resultSet.getString("src_node");
                    String src_island = resultSet.getString("src_island");
                    String dest_addr = resultSet.getString("dest_addr");
                    String dest_node = resultSet.getString("dest_node");
                    String dest_island = resultSet.getString("dest_island");
                    String ts = resultSet.getString("ts");
                    int tx = resultSet.getInt("tx");
                    int rx = resultSet.getInt("rx");
                    long lossrate = resultSet.getLong("lossrate");
                    long rtt_avg = resultSet.getLong("rtt_avg");
                    String rtt_unit = resultSet.getString("rtt_unit");

                    // Prepare the string with the ping results to add in the the matrix
                    String result = String.format("RTT avg: %f %s \nLoss rate: %f%", rtt_avg, rtt_unit, lossrate);

                    // Get the position in the matrix to store the informaiton of this ping
                    int col = nodesList.indexOf(dest_node);
                    int row = nodesList.indexOf(src_node);

                    // Add the result to its position in the matrix
                    matrix[row][col] = result;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }

    public String[][] getNodeMatrixMostRecentResultsMatrix() {
        Statement statement;
        ResultSet resultSet = null;
        // Get the list with the names of the nodes
        List<String> nodesList = getNodesSortedList();
        // number of nodes
        int length = nodesList.size();
        System.out.println("nodesList size: " + length);
        // Create a Matrix to store the results
        String[][] matrix = new String[length][length + 1];

        try {
            // for each node in the list get the results of the pings to other nodes
            for (String node : nodesList) {
                // create new statement
                statement = connection.createStatement();
                // create string witht the SQL query
                String sql_query = String.format("SELECT *, max(ts) FROM ping_result WHERE src_node = '%s' GROUP BY dest_node", node);
                System.out.println(sql_query);
                // Execute query and get the results set
                resultSet = statement.executeQuery(sql_query);
                // Close statement
                statement.close();

                // each node in the for corresponds to a row of the matrix
                // each element of the resultsSet corresponds to a cell of this row
                // fill the i-i cell with nothing
                // check for each element of the results set the position where it has to go
                // to do that, get the index of the NodeName in the nodesList 
                // add the prepared information to the correct cell of the matrix
                // for each element in the result cell
                // Prepare the cell with the info
                //System.out.println(resultSet);
                while (resultSet.next()) {
                    // Get result parameters
                    System.out.println("inside while");
                    String src_addr = resultSet.getString("src_addr");
                    String src_node = resultSet.getString("src_node");
                    String src_island = resultSet.getString("src_island");
                    String dest_addr = resultSet.getString("dest_addr");
                    String dest_node = resultSet.getString("dest_node");
                    String dest_island = resultSet.getString("dest_island");
                    String ts = resultSet.getString("ts");
                    int tx = resultSet.getInt("tx");
                    int rx = resultSet.getInt("rx");
                    double lossrate = resultSet.getLong("lossrate");
                    double rtt_avg = resultSet.getLong("rtt_avg");
                    String rtt_unit = resultSet.getString("rtt_unit");

                    System.out.println(src_addr + " " + src_node + " " + src_island + " " + dest_addr + " " + dest_node + " " + dest_addr);

                    // Prepare the string with the ping results to add in the the matrix
                    String result = String.format("RTT avg: %.3f %s \nLoss rate: %.2f%%", rtt_avg, rtt_unit, lossrate);
                    System.out.println(result);

                    // Get the position in the matrix to store the informaiton of this ping
                    int col = nodesList.indexOf(dest_node) + 1;
                    int row = nodesList.indexOf(src_node);

                    // Add the result to its position in the matrix
                    matrix[row][col] = result;

                }

            }
            // Add nodes names to the first column of the matrix
            for (int i = 0; i < nodesList.size(); i++) {
                matrix[i][0] = nodesList.get(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matrix;
    }

    public String getNodeMatrixMostRecentResultsMatrixJSON() {
        JSONArray jsonMatrix;
        String jsonString = null;
        try {
            jsonMatrix = new JSONArray(getNodeMatrixMostRecentResultsMatrix());
            jsonString = jsonMatrix.toString();
        } catch (JSONException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonString;
    }

    public Connection openConnection(String db_path) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String db_url = "jdbc:sqlite:" + db_path;
            connection = DriverManager.getConnection(db_url);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Functions returning fake informaiton for testing
    public static List<String> getFakeNodesList() {
        List<String> nodesList = new ArrayList<>();
        nodesList.add("Node-1");
        nodesList.add("Node-2");
        nodesList.add("Node-3");
        nodesList.add("Node-4");
        return nodesList;
    }

    public static String[][] getFakeMatrix(List<String> nodesList) {
        String[][] matrix;
        int size = nodesList.size();
        matrix = new String[size][size];
        String content = "RTT avg: %.3f ms--Loss rate: %.2f%%";
        Random random = new Random();
        double random_rtt, random_lossrate;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
                } else {
                    random_rtt = random.nextDouble() * 10;
                    random_lossrate = random.nextDouble() * 100.0;
                    matrix[i][j] = String.format(content, random_rtt, random_lossrate);
                }
            }
        }

        return matrix;
    }

    public String getNodeMatrixMostRecentResultsMatrixFakeJSON() {
        Statement statement;
        ResultSet resultSet = null;
        // Get the list with the names of the nodes
        List<String> nodesList = getNodesSortedList();
        // number of nodes
        int length = nodesList.size();
        System.out.println("nodesList size: " + length);
        // Create a Matrix to store the results
        String[][] matrix = new String[length][length + 1];
        String content = "%.3fms  %.2f%%";
        Random random = new Random();
        double random_rtt, random_lossrate;

        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length + 1; j++) {
                if (i == j - 1) {
                    matrix[i][j] = "";
                } else {
                    random_rtt = random.nextDouble() * 10;
                    random_lossrate = random.nextDouble() * 100.0;
                    matrix[i][j] = String.format(content, random_rtt, random_lossrate);
                }
            }
        }

        // Add nodes names to the first column of the matrix
        for (int i = 0; i < nodesList.size(); i++) {
            matrix[i][0] = nodesList.get(i);
        }

        String jsonString = null;
        JSONArray jsonMatrix;
        try {
            jsonMatrix = new JSONArray(matrix);
            jsonString = jsonMatrix.toString();
        } catch (JSONException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonString;
    }

    public ResultSet getFromToNodeAllResultSet(String src_node, String dest_node) throws SQLException {
        // Get a ResultSet with all the results of the pings from src_node to dest_node sorted by the timestamp
        Statement statement;
        ResultSet resultSet = null;
        // create new statement
        statement = connection.createStatement();
        // create a String with the SQL query
        String sql_query = String.format("SELECT * FROM ping_result WHERE src_node = '%s' AND dest_node = '%s' ORDER BY ts DESC;", src_node, dest_node);
        // execute query and get the result set
        resultSet = statement.executeQuery(sql_query);
        //debug printing
        /*while (resultSet.next()) {
            String _src_node = resultSet.getString("src_node");
            String src_island = resultSet.getString("src_island");
            String _dest_node = resultSet.getString("dest_node");
            String dest_island = resultSet.getString("dest_island");
            String ts = resultSet.getString("ts");
            System.out.println(src_island + "/" + _src_node + " --> " + dest_island + "/" + _dest_node + "   " + ts);
            System.out.println();
        }*/
        // close statement
        statement.close();

        return resultSet;
    }

    public DetailedResults getNodesDetailedResults(String fromNode, String toNode) {
        DetailedResults detailedResults = null;
        String fromAddr, fromIsland, toAddr, toIsland, ts, tx, rx, rtt_avg, rtt_unit, lossrate, historicResultsJSON;
        List<List<String>> historicResults = new ArrayList<>();
        try {
            ResultSet resultSet = getFromToNodeAllResultSet(fromNode, toNode);
            fromAddr = resultSet.getString("src_addr");
            fromIsland = resultSet.getString("src_island");
            toAddr = resultSet.getString("dest_addr");
            toIsland = resultSet.getString("dest_island");

            while (resultSet.next()) {
                ts = resultSet.getString("ts");
                tx = Integer.toString(resultSet.getInt("tx"));
                rx = Integer.toString(resultSet.getInt("rx"));
                rtt_avg = resultSet.getString("rtt_unit");
                rtt_unit = Double.toString(resultSet.getDouble("rtt_avg"));
                lossrate = Double.toString(resultSet.getDouble("lossrate"));
                List<String> result = new ArrayList<>();
                result.add(ts);
                result.add(tx);
                result.add(rx);
                result.add(lossrate);
                result.add(rtt_avg+rtt_unit);
                historicResults.add(result);
            }
            historicResultsJSON = (new JSONArray(historicResults)).toString();
            detailedResults = new DetailedResults(fromNode, fromIsland, fromAddr, toNode, toIsland, toAddr, historicResultsJSON);
        } catch (SQLException ex) {
            Logger.getLogger(ResultsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return detailedResults;
    }

}

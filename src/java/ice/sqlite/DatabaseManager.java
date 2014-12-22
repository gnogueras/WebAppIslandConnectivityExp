/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ice.sqlite;

import ice.mvc.model.DetailedResults;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author gerard
 */
public class DatabaseManager {

    public static DetailedResults getDetailedResults(String fromNode, String toNode) {
        // declare and initialise local variables
        String commandString = String.format("sqlite3 /var/lib/oml2/2014-12-17T16:34:24.518Z.sq3 'SELECT * FROM ping_result WHERE src_node = \"%s\" AND dest_node = \"%s\" ORDER BY ts DESC;'", fromNode, toNode);
        String src_node, src_island, src_addr, dest_node, dest_island, dest_addr, historic_results;
        String ts = "-", tx = "-", rx = "-", lossrate = "-", rtt_avg = "-", rtt_unit = "";
        DetailedResults detailedResults = new DetailedResults();
        List<List<String>> historicResultsArray = null;
        
        try {
            // Execute the command to query the db
            String[] cmd = {"/bin/bash", "-c", commandString};
            Process pb = Runtime.getRuntime().exec(cmd);
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
            
            // Read the first line and fill info of source and destination nodes
            if ((line = input.readLine()) != null) {
                String[] split = line.split(Pattern.quote("|"));
                src_addr = split[6];
                src_node = split[7];
                src_island = split[8];
                dest_addr = split[9];
                dest_node = split[10];
                dest_island = split[11];
                detailedResults = new DetailedResults(src_node, src_island, src_addr, dest_node, dest_island, dest_addr, null);
                historicResultsArray = new ArrayList<>();
            }
            // Read the rest of the lines and get the ping parameters
            while (line != null) {
                String[] split = line.split(Pattern.quote("|"));
                System.out.println(line);
                for (int i = 0; i < split.length; i++) {
                    System.out.print(i + ": " + split[i] + " ");
                }
                System.out.println();
                ts = split[5];
                // try to get the other parameters. It might raise an exception if rtt is not avaialable
                try {
                    tx = split[12];
                    rx = split[13];
                    lossrate = split[14];
                    rtt_avg = split[15];
                    rtt_unit = split[16];
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                // create an array of strings with the parameters of the ping
                List<String> result = new ArrayList();
                result.add(ts);
                result.add(tx);
                result.add(rx);
                result.add(lossrate + "%");
                result.add(rtt_avg + rtt_unit);
                // Add the string of ping results to the historic results
                historicResultsArray.add(result);
                // read the next line
                line = input.readLine();
            }
            input.close();
            // Set the historic results as a JSON string in the detialedResults
            detailedResults.setHistoricResults(listToJSONArray(historicResultsArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detailedResults;
    }

    public static String getMatrixResults(List<String> nodesSortedList) {
        // Prepare and declare variables
        String commandString;
        int length = nodesSortedList.size();
        String[][] matrix = new String[length][length + 1];
        String dest_node, rtt_avg, rtt_unit, lossrate;

        // Initialise Matrix
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length + 1; j++) {
                if (j == 0) {
                    matrix[i][j] = nodesSortedList.get(i);
                } else if (i == j - 1) {
                    matrix[i][j] = " ";
                } else {
                    matrix[i][j] = "Not info avaialable";
                }
            }
        }

        // for each node in the List
        for (String src_node : nodesSortedList) {
            // prepare the command to query the db
            commandString = String.format("sqlite3 /var/lib/oml2/2014-12-17T16:34:24.518Z.sq3 'SELECT *, max(ts) FROM ping_result WHERE src_node = \"%s\" GROUP BY dest_node;'", src_node);

            try {
                // execute the command to query the db
                String[] cmd = {"/bin/bash", "-c", commandString};
                Process pb = Runtime.getRuntime().exec(cmd);
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                // read the results line by line
                while ((line = input.readLine()) != null) {
                    // Parse each line and get the result fields
                    String[] split = line.split(Pattern.quote("|"));
                    dest_node = split[10];
                    lossrate = split[14];
                    rtt_avg = split[15];
                    rtt_unit = split[16];

                    // check if the destination node is a OMF-RC node
                    if (nodesSortedList.contains(dest_node)) {
                        if (!(rtt_avg.equals("") && lossrate.equals(""))) {
                            // There are available results
                            if (rtt_avg.equals("")) {
                                rtt_avg = "?";
                                rtt_unit = "";
                            }
                            // Prepare the string with the ping results to add in the the matrix
                            String result = String.format("%s%s    %s%%", rtt_avg, rtt_unit, lossrate);

                            // Get the position in the matrix to store the informaiton of this ping
                            int col = nodesSortedList.indexOf(dest_node) + 1;
                            int row = nodesSortedList.indexOf(src_node);

                            // Add the result to its position in the matrix
                            //System.out.println(src_node + "-->" + dest_node + "  [" + row + "," + col + "]:  " + result);
                            matrix[row][col] = result;
                        }
                    }
                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return matrixToJSONArray(matrix);
    }

    static String listToJSONArray(List list) {
        JSONArray jsonArray = new JSONArray(list);
        return jsonArray.toString();
    }

    static String matrixToJSONArray(String[][] matrix) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(matrix);
            return jsonArray.toString();
        } catch (JSONException ex) {
            Logger.getLogger(DatabaseManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "[]";

    }

}

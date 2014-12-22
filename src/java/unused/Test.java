/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unused;

import ice.mvc.model.ResultsManager;
import ice.sqlite.DatabaseManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author gerard
 */
public class Test {

    public static void main(String args[]) {
        /*try {
            String[] cmd = {"/bin/bash", "-c", "sqlite3 /var/lib/oml2/2014-12-17T16:34:24.518Z.sq3 'SELECT *, max(ts) FROM ping_result GROUP BY dest_node;'"};
            Process pb = Runtime.getRuntime().exec(cmd);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //DatabaseManager.getDetailedResults("UPC-lab104-VM05", "UPC-Vertex-Pangea-Zotac");
        String slivers_info_json_file = "/home/gerard/island-connectivity-experiment/slivers/slivers_info.json";
        ResultsManager rm = new ResultsManager(slivers_info_json_file);
        List<String> nodesSortedList = rm.getNodesSortedList();
        DatabaseManager.getMatrixResults(nodesSortedList);
        System.out.println(nodesSortedList.toString());
        System.out.println(nodesSortedList.indexOf("UPC-lab104-VM09"));

    }
}

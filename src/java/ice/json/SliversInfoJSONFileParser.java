/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ice.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.*;

/**
 *
 * @author gerard
 */
public class SliversInfoJSONFileParser {
// Add java-json.jar to the libraries of the project
// http://www.java2s.com/Code/JarDownload/java/java-json.jar.zip

    public SliversInfoJSONFileParser() {

    }

    public static List<Map<String, String>> parse(String slivers_info_json_file) {

        // create new List of Maps to store slivers_info
        List<Map<String, String>> slivers_info = new ArrayList<>();

        try {
            // get the content of the JSON file
            String content = new Scanner(new File(slivers_info_json_file)).useDelimiter("\\Z").next();
            // create a JSON array with the content of the file
            JSONArray slivers = new JSONArray(content);

            // Convert each element of the JSON array into a Map object and add it to the slivers_info list
            for (int i = 0; i < slivers.length(); i++) {
                JSONObject sliver = (JSONObject) slivers.get(i);
                Map<String, String> sliver_map = new HashMap<>();
                Iterator<String> keys = sliver.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = sliver.getString(key);
                    sliver_map.put(key, value);
                }
                slivers_info.add(sliver_map);
            }
        } catch (FileNotFoundException | JSONException ex) {
            Logger.getLogger(SliversInfoJSONFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return slivers_info;
    }

    public static List<String> getNodesList(List<Map<String, String>> slivers_info) {
        // create a new list with the name of the ndoes
        List<String> nodesList = new ArrayList<>();
        // for each Map element in the slivers_info get the node name and add it to the nodesList
        for (Map<String, String> sliver : slivers_info) {
            if (!sliver.get("role").equals("OMF-EC")) {
                nodesList.add(sliver.get("node"));
            }
        }
        return nodesList;
    }

    public static List<Map<String, String>> getNodesSortedListMap(List<Map<String, String>> slivers_info) {
        // create a new list with the name of the ndoes
        List<Map<String, String>> nodesSortedListMap = new ArrayList<>();
        // for each Map element in the slivers_info get the node name and add it to the nodesList
        for (Map<String, String> sliver : slivers_info) {
            if (!sliver.get("role").equals("OMF-EC")) {
                Map<String, String> node = new HashMap<>();
                node.put("island", sliver.get("island"));
                node.put("node", sliver.get("node"));
                nodesSortedListMap.add(sliver);
            }
        }

        Collections.sort(nodesSortedListMap, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> node1, Map<String, String> node2) {
                if(node1.get("island")!=node2.get("island")){
                    return node1.get("island").compareTo(node2.get("island"));
                }
                else{
                    return node1.get("node").compareTo(node2.get("node"));
                }
            }
        });
        return nodesSortedListMap;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unused;

import java.sql.*;

/**
 *
 * @author gerard
 */
public class Database {
// Add sqlite-jdbc-3.8.6.jar to the libraries of the project
// https://bitbucket.org/xerial/sqlite-jdbc/downloads
    public static void main(String args[]) {

        Connection c = null;
        Statement stmt = null;
        try {
            String db_path = "/var/lib/oml2/2014-12-17T16:34:24.518Z.sq3";
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/var/lib/oml2/2014-12-17T16:34:24.518Z.sq3");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            //String sql_query = String.format("SELECT * FROM ping_result WHERE src_node = 'UPC-lab104-VM03' AND dest_node = 'UPC-lab104-VM02' ORDER BY ts DESC");
            String sql_query = String.format("SELECT *, max(ts) FROM ping_result GROUP BY dest_node");
            ResultSet rs = stmt.executeQuery(sql_query);
            while (rs.next()) {
                String src_node = rs.getString("src_node");
                String src_island = rs.getString("src_island");
                String dest_node = rs.getString("dest_node");
                String dest_island = rs.getString("dest_island");
                String ts = rs.getString("ts");
                System.out.println(src_island + "/" + src_node + " --> " + dest_island + "/" + dest_node + "   " + ts);
                System.out.println();
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}

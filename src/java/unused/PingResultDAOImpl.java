/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unused;

import ice.mvc.model.DetailedResults;
import unused.PingResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author gerard
 */
public class PingResultDAOImpl implements PingResultDAO {
    
    private DataSource dataSource;
 
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    @Autowired
    DataSource dataSource;
    */
    
    //private JdbcTemplate jdbcTemplate;

    public PingResultDAOImpl() {    
    }
    
    /*
    public PingResultDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }*/

    /*
    @Override
    public List<PingResult> getPingResults(String fromNode, String toNode) {
        
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql_query = String.format("SELECT * FROM ping_result WHERE src_node = '%s' AND dest_node = '%s' ORDER BY ts DESC;", fromNode, toNode);
        List<PingResult> pingResultList = jdbcTemplate.query(sql_query, new RowMapper<PingResult>() {

            @Override
            public PingResult mapRow(ResultSet rs, int rowNum) throws SQLException {
                PingResult pingResult = new PingResult();

                pingResult.setTs(rs.getString("ts"));
                pingResult.setRtt(Double.toString(rs.getDouble("rtt_avg")) + rs.getString("rtt_unit"));
                pingResult.setTx(rs.getInt("tx"));
                pingResult.setRx(rs.getInt("rx"));
                pingResult.setLossrate(rs.getDouble("lossrate"));

                return pingResult;
            }
        });
        return pingResultList;

    }*/

    @Override
    public DetailedResults getPingResults(String fromNode, String toNode) {
        
        String sql_query = String.format("SELECT * FROM ping_result WHERE src_node = '%s' AND dest_node = '%s' ORDER BY ts DESC;", fromNode, toNode);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String fromAddr, fromIsland, toAddr, toIsland, ts, tx, rx, rtt_avg, rtt_unit, lossrate, historicResultsJSON;
        List<List<String>> historicResults = new ArrayList<>();        
        DetailedResults detailedResults = null;
        try {
            System.out.println("GET PING RESULTS   "+dataSource.toString());
            con = dataSource.getConnection();
            
            ps = con.prepareStatement(sql_query);
            resultSet = ps.executeQuery();
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
            Logger.getLogger(PingResultDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                resultSet.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
        return detailedResults;
    }

}

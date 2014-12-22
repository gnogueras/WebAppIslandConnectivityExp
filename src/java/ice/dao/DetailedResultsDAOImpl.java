/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ice.dao;

import ice.mvc.model.DetailedResults;
import ice.mvc.model.ResultsManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author gerard
 */
public class DetailedResultsDAOImpl implements DetailedResultsDAO {

    private JdbcTemplate jdbcTemplate;

    public DetailedResultsDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public DetailedResults getDetailedResults(String fromNode, String toNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}

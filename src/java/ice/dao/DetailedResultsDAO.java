/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ice.dao;

import ice.mvc.model.DetailedResults;

/**
 *
 * @author gerard
 */
public interface DetailedResultsDAO {
    
    public DetailedResults getDetailedResults(String fromNode, String toNode);
}

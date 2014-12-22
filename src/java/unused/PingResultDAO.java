/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unused;

import ice.mvc.model.DetailedResults;
import unused.PingResult;
import java.util.List;

/**
 *
 * @author gerard
 */
public interface PingResultDAO {
    
    public DetailedResults getPingResults(String fromNode, String toNode);
    
}

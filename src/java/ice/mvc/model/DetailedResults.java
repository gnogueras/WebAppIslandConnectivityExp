/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ice.mvc.model;

/**
 *
 * @author gerard
 */
public class DetailedResults {
    
    String fromNode;
    String fromIsland;
    String fromAddr;
    
    String toNode;
    String toIsland;
    String toAddr;
    
    String historicResults;

    public DetailedResults() {
    }

    
    public DetailedResults(String fromNode, String fromIsland, String fromAddr, String toNode, String toIsland, String toAddr, String historicResults) {
        this.fromNode = fromNode;
        this.fromIsland = fromIsland;
        this.fromAddr = fromAddr;
        this.toNode = toNode;
        this.toIsland = toIsland;
        this.toAddr = toAddr;
        this.historicResults = historicResults;
    }

    
    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getFromIsland() {
        return fromIsland;
    }

    public void setFromIsland(String fromIsland) {
        this.fromIsland = fromIsland;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public String getToIsland() {
        return toIsland;
    }

    public void setToIsland(String toIsland) {
        this.toIsland = toIsland;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getHistoricResults() {
        return historicResults;
    }

    public void setHistoricResults(String historicResults) {
        this.historicResults = historicResults;
    }
        
}

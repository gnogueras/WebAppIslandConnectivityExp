/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ice.mvc.controller;

import ice.json.SliversInfoJSONFileParser;
import ice.mvc.model.DetailedResults;
import ice.mvc.model.SelectedNodes;
import ice.mvc.model.ResultsManager;
import ice.sqlite.DatabaseManager;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 *
 * @author gerard
 */
@Controller
public class IslandConnectivityController {

    ResultsManager rm;
    SliversInfoJSONFileParser parser;

    //@Autowired
    //private PingResultDAO pingResultDAO;
    
    public IslandConnectivityController() {
        String db_path = "/var/lib/oml2/2014-12-17T16:34:24.518Z.sq3";

        String slivers_info_json_file = "/home/gerard/island-connectivity-experiment/slivers/slivers_info.json";
        rm = new ResultsManager(slivers_info_json_file);
    }

    @RequestMapping(value = "/islandConnectivity", method = RequestMethod.GET)
    public ModelAndView islandConnectivity() {
        System.out.println("islandConnectivity called! ");

        ModelAndView modelAndView = new ModelAndView("islandConnectivity");

        modelAndView.addObject("selectedNodes", new SelectedNodes());
        modelAndView.addObject("detailedResults", new DetailedResults());
        modelAndView.addObject("nodesListJSON", rm.getNodesSortedListJSON());
        modelAndView.addObject("nodesListArray", rm.getNodesSortedList());
        modelAndView.addObject("matrix", DatabaseManager.getMatrixResults(rm.getNodesSortedList()));

        return modelAndView;
    }

    @RequestMapping(value = "/selectNodes", method = RequestMethod.POST)
    public String selectNodes(@ModelAttribute("selectedNodes") SelectedNodes selectedNodes, BindingResult result, Model model) {
        model.addAttribute("selectedNodes", selectedNodes);
        model.addAttribute("detailedResults", DatabaseManager.getDetailedResults(selectedNodes.getFromNode(), selectedNodes.getToNode()));
        model.addAttribute("nodesListJSON", rm.getNodesSortedListJSON());
        model.addAttribute("nodesListArray", rm.getNodesSortedList());
        model.addAttribute("matrix", DatabaseManager.getMatrixResults(rm.getNodesSortedList()));

        return "islandConnectivityDetails";
    }

   
}

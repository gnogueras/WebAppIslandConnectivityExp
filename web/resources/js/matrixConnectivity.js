/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 var headers = [' ', "Node1", "Node2", "Node3", "Node4"];
 var matrix = [
 ["Node1", 0, 1, 2, 3],
 ["Node2", 4, 5, 6, 7],
 ["Node3", 8, 9, 10, 11],
 ["Node4", 12, 13, 14, 15], ];
 */

// Append a new table element to the body
// and create subelements thead, tbody
var table = d3.select("#matrixResults").append("table")
        .attr("class", "matrix_table");
var thead = table.append("thead");
var tbody = table.append("tbody");


// Append row elements to the tbody of the table
var rows = tbody.selectAll("tr")
        .data(matrix)
        .enter()
        .append("tr")
        .attr("class", "matrix_tr");


// Append the Row header for the thead element
var header_row = thead.append("tr")
        .attr("class", "matrix_tr");


// Append cells to each row element of the tbody
var cells = rows.selectAll("td")
        .data(function(d) {
            return d;
        })
        .enter()
        .append("td")
        .attr("cellDefaultType", function(d, i, j) {
            if (i == 0) {
                return "header_cell";
            } else if (i == j + 1) {
                return "empty_cell";
            }
            else {
                //return "content_cell";
                var m = d.match(/(\d*.\d*)(ms)?\??\s+(\d+.\d*)%/);
                if (m == null) {
                    return "content_cell grey_bg";
                } else {
                    var lossrate = m[3];
                    if (lossrate < 20) {
                        return "content_cell green_bg";
                    } else if (lossrate >= 20 && lossrate < 80) {
                        return "content_cell amber_bg";
                    } else if (lossrate >= 80) {
                        return "content_cell red_bg";
                    }
                }
            }
        })
        .attr("class", function(d) {
            return d3.select(this).attr("cellDefaultType")
        })
        .on("mouseover", function(d, column, row) {
            if (column > 0 && column != row + 1) {
                //d3.select(this).style("background-color", "red")
                d3.select(this).attr("class", function(d) {
                    return d3.select(this).attr("cellDefaultType") + "_active"
                })
                d3.select("#from_" + row).style("color", "#0080FF")
                d3.select("#to_" + column).style("color", "#0080FF")

            }
        })
        .on("mouseout", function(d, column, row) {
            if (column > 0 && column != row + 1) {
                //d3.select(this).style("background-color", "white")
                d3.select(this).attr("class", function(d) {
                    return d3.select(this).attr("cellDefaultType")
                })
                d3.select("#from_" + row).style("color", "black")
                d3.select("#to_" + column).style("color", "black")
            }
        })
        .attr('id', function(d, i, j) {
            return 'from_' + j;
        })
        .text(function(d) {
            return d;
        });

// Append the header cells to the header row
var header_cells = header_row.selectAll("th")
        .data(headers)
        .enter()
        .append("th")
        .text(function(d) {
            return d;
        })
        .attr('class', 'header_cell')
        .attr('id', function(d, i, j) {
            return "to_" + i;
        });
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/1.10.4/css/jquery.dataTables.css">
        <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.css">
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

        <!-- Javascript -->
        <!-- D3.js -->
        <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src='http://mbostock.github.com/d3/d3.js'></script>

        <title>Island Connectivity Experiment</title>
    </head>

    <body>

        <!-- Fixed navbar -->
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">
                        <div>
                            <img src="<c:url value="/resources/images/logo_confine.png" />" width="30" alt="logo_confine"/>
                            Island Connectivity Experiment
                        </div>
                    </a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#ExperimentInfo">Experiment Info</a></li>
                        <li><a href="#IslandConnectivityMatrix">Connectivity Matrix</a></li>
                        <li><a href="#DetailedResults">Detailed Results</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <a name="ExperimentInfo"></a>
        <div class="container">
            <br><br>
            <div class="page-header">
                <h1>Experiment Info</h1>
            </div>
            <!-- Main component for a primary marketing message or call to action -->
            <div class="jumbotron">
                <p>Experiment to create a long-running service that monitors connectivity among Community-Lab islands.</p>
                <p>
                <ul>
                    <li><b>SFA</b> to allocate the resources of the experiment</li>
                    <li><b>OMF</b> to define the experiment and deploy it on the resources</li>
                    <li><b>OML</b> to get the results of the experiment</li>
                </ul>
                </p>
            </div>
            <div align="center">
                <img src="<c:url value="/resources/images/ice_topology.png" />" width="791" height="528" alt="ice_topology"/>
            </div>
        </div>

        <a name="IslandConnectivityMatrix"></a>
        <div class="container">
            <br><br>
            <div class="page-header">
                <h1>Island Connectivity Matrix</h1>
            </div>
            <div id="matrixResults" align=center">
                <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/matrix.css" />">
                <script type="text/javascript">
                    var headers = [' '].concat(${nodesListJSON});
                    var matrix = ${matrix};
                </script>
                <script src = "<c:url value="/resources/js/matrixConnectivity.js" />" >
                </script> 

            </div>

        </div>

        <a name="DetailedResults"></a>
        <div class="container">
            <br><br>
            <div class="page-header">
                <h1>Detailed results</h1>
            </div>
            <div class="container">
                <p>Select the source and destination nodes to view the detailed results</p>
                <form:form class="form-inline" role="form" action="selectNodes" method="POST" modelAttribute="selectedNodes">
                    <div class="form-group">
                        <form:label path="fromNode">From: </form:label>
                        <form:select class="form-control" path="fromNode" id="fromNode" >
                            <c:forEach var="node" items="${nodesListArray}">
                                <option value="${node}">
                                    <c:out value="${node}"/>
                                </option>
                            </c:forEach>
                        </form:select>
                    </div>
                    <div class="form-group">
                        <form:label path="toNode">To: </form:label>
                        <form:select class="form-control" path="toNode" id="toNode" >
                            <c:forEach var="node" items="${nodesListArray}">
                                <option value="${node}">
                                    <c:out value="${node}"/>
                                </option>
                            </c:forEach>
                        </form:select>
                    </div>
                    <button type="submit" class="btn btn-default">View</button>
                </form:form>
            </div>

            <div class="container" id="detailedResults" style="visibility: visible">
                <div class="container" id="source_destination_container">
                    <br/>
                    <table id="source_destination_info" class="table" style="table-layout: fixed">
                        <col width="80px"/>
                        <col width="200px"/>
                        <col width="80px"/>
                        <col width="200px"/>
                        <thead>
                            <tr>
                                <th data-field="source" colspan="2">Source Node</th>
                                <th data-field="destiation" colspan="2">Destination Node</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="font-weight: bold"> Name: </td> 
                                <td> ${selectedNodes.fromNode} </td>
                                <td style="font-weight: bold"> Name: </td> 
                                <td> ${selectedNodes.toNode} </td>
                            </tr>
                            <tr>
                                <td style="font-weight: bold"> IPv4 addr: </td>
                                <td> ${detailedResults.fromAddr} </td>
                                <td style="font-weight: bold"> IPv4 addr: </td>
                                <td> ${detailedResults.toAddr} </td>
                            </tr>
                            <tr>
                                <td style="font-weight: bold"> Island: </td>
                                <td> ${detailedResults.fromIsland} </td>
                                <td style="font-weight: bold"> Island: </td>
                                <td> ${detailedResults.toIsland} </td>
                            </tr>                         
                        </tbody>
                    </table>
                    <br/>
                </div>



                <div class="table-responsive">
                    <table id="indextable" class="display" cellspacing="0" width="100%">
                        <thead>
                            <tr>
                                <th>Timestamp</th>
                                <th>Tx packets</th>
                                <th>Rx packets</th>
                                <th>Lossrate</th>
                                <th>RTT average</th>
                            </tr>
                        </thead>


                    </table>
                </div>
            </div>
        </div>
        <br>
        <br>


        <!-- jQuery -->
        <!--<script type="text/javascript" charset="utf8" src="//code.jquery.com/jquery-1.10.2.min.js"></script>-->
        <!-- DataTables -->
        <!--<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js"></script>
        <script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.0/jquery.dataTables.min.js"></script>-->
        <!--<script type="text/javascript" charset="utf8" src="http://cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" charset="utf8" src="http://code.jquery.com/jquery-1.11.1.min.js"></script>-->
        <!--
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js"></script>
        <script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.0/jquery.dataTables.min.js"></script>
        -->

        <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script type="text/javascript" src="http://cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <script>
                    var dataTable = $('#indextable').DataTable(
                            {
                                //paging: false,
                                data: ${detailedResults.historicResults}
                            }
                    );
        </script>

    </body>
</html>
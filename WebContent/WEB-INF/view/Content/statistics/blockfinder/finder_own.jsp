<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-desktop fa-fw"></i> Blocks found by own Workers
      </div>
      <div class="panel-body no-padding table-responsive"> 
        <table class="table table-striped table-bordered table-hover">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Worker</th>
              <th>Blocks</th>
              <th>Coins Generated</th>
            </tr>
          </thead>
          <tbody>
          <c:set var="rank" value="1"/>
          <c:forEach var="block" items="${BLOCKSSOLVEDBYWORKER }">
          <tr>
          	<c:set var="rank" value="${rank + 1 }"/>
          	<td>${rank}</td>
          	<c:set var="tdFinder" value="unknown/deleted"/>
          	<c:if test="${block.finder != null and block.finder ne ''}">
          		<c:set var="tdFinder" value="${block.finder }"/>
          	</c:if>
          	<td>${tdFinder }</td>
          	<td>${block.solvedblocks }</td>
          	<td><fmt:formatNumber value="${block.generatedcoins }" pattern="#,###" /></td>
          </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-user fa-fw"></i> Top 25 Blockfinder
      </div>
      <div class="panel-body no-padding table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Username</th>
              <th>Blocks</th>
              <th>Coins Generated</th>
            </tr>
          </thead>
          <tbody>
		  <c:set var="rank" value="1"/>
		  <c:forEach var="block" items="${ BLOCKSSOLVEDBYACCOUNT}">
		  <tr>
		  	<c:set var="rank" value="${rank + 1 }"/>
          	<td>${rank}</td>
		  	<c:set var="tdVar" value="unknown"/>
		  	<c:if test="${block.finder != null }"> 
		  		<c:set var="tdVar" value="${block.finder }"/>	
		  	</c:if>
		  	<c:if test="${block.is_anonymous == 1 and USERDATA.is_admin == 0 }">
		  		<c:set var="tdVar" value="anonymous"/>
		  	</c:if>
		  	<td>${tdVar }</td>
		  	<td>${block.solvedblocks }</td>
		  	<td><fmt:formatNumber value="${block.generatedcoins }" pattern="#,###" /></td>
		  </tr>
		  </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>

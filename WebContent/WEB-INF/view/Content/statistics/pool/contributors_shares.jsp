<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-tachometer fa-fw"></i> Contributor Shares
      </div>
      <div class="panel-body no-padding table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Donor</th>
              <th>User Name</th>
              <th class="text-right">Shares</th>
            </tr>
          </thead>
          <tbody>
		  <c:set var="rank" value="1"/>
		  <c:set var="listed" value="0"/>
		  <c:forEach var="shares" items="${CONTRIBSHARES }" >
		  	<c:set var="classVar" value=""/>
		  	<c:if test="${USERDATA.username eq shares.account }">
		  		<c:set var="listed" value="1"/>
		  		<c:set var="classVar" value="success"/>
		  	</c:if>
		  	<tr class="${classVar }">
			  	<c:set var="rank" value="${rank + 1 }"/>
			  	<td>${rank }</td>
			  	<td>
			  	<c:choose>
			  		<c:when test="${shares.donate_percent >= 2}">
			  			<i class="fa fa-trophy fa-fw"></i>
			  		</c:when>
			  		<c:when test="${shares.donate_percent < 2 and shares.donate_percent > 0 }">
			  			<i class="fa fa-star-o fa-fw"></i>
			  		</c:when>
			  		<c:otherwise>
			  			<i class="fa fa-ban fa-fw"></i>
			  		</c:otherwise>
			  	</c:choose>
			  	</td>
			  	<c:set var="tdAccount" value="${shares.account }"/>
			  	<c:if test="${shares.is_anonymous == 1 and USERDATA.is_admin == 0 }">
			  		<c:set var="tdAccount" value="anonymous"/>
			  	</c:if>
			  	<td>${tdAccount }</td>
			  	<fmt:parseNumber var="shares" integerOnly="true" value="${shares.shares}"/>
			  	<td class="text-right">${shares }</td>
		  	</tr>
		  </c:forEach>
		  <c:if test="${listed != 1 and USERDATA.username ne '' && USERDATA.shares.valid > 0 }">
		  	<c:set var="classVar" value=""/>
		  	<c:if test="${USERDATA.username eq CONTRIBHASHES.account }">
		  		<c:set var="listed" value="1"/>
		  		<c:set var="classVar" value="success"/>
		  	</c:if>
		  	<tr class="${classVar }">
		  		<td>n/a</td>
		  		<td>
		  		<c:choose>
		  			<c:when test="${USERDATA.donate_percent >= 2 }">
		  				<i class="fa fa-trophy fa-fw"></i>
		  			</c:when>
		  			<c:when test="${shares.donate_percent < 2 and shares.donate_percent > 0 }">
		  				<i class="fa fa-star-o fa-fw"></i>
		  			</c:when>
		  			<c:otherwise>
		  				<i class="fa fa-ban fa-fw"></i>
		  			</c:otherwise>
		  		</c:choose>
		  		</td>
		  		<td>${USERDATA.username }</td>
		  		<fmt:parseNumber var="shares" integerOnly="true" value="${USERDATA.shares.valid}"/>
		  		<td>${shares }</td>
		  	</tr>
		  </c:if>
          </tbody>
        </table>
      </div>
      <div class="panel-footer">
          <h6>
          <i class="fa fa-ban fa-fw"></i>no Donation
          <i class="fa fa-star-o fa-fw"></i> 0&#37;&#45;2&#37; Donation 
          <i class="fa fa-trophy fa-fw"></i> 2&#37; or more Donation
          </h6>
      </div>
    </div>
  </div>

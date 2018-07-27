<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pf" tagdir="/WEB-INF/tags" %>
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-tachometer fa-fw"></i> Contributor Hashrates
      </div>
      <div class="panel-body no-padding table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
            <tr>
              <th>Rank</th>
              <th>Donor</th>
              <th>User Name</th>
              <th class="text-right">KH/s</th>
              <th class="text-right">${currency}/Day</th>
              <!-- {if $GLOBAL.config.price.enabled}
              {if $GLOBAL.config.price.currency}<th class="text-right">{$GLOBAL.config.price.currency}/Day</th>{/if}
               {/if} -->
            </tr>
          </thead>
          <tbody>
          <c:set var="rank" value="1"/>
          <c:set var="listed" value="0"/>
          <c:forEach var="contrib" items="${CONTRIBHASHES }">
          <pf:roundVar var="estday" diff="${DIFFICULTY }" reward="${REWARD }" hashrate="${contrib.hashrate }"/>
          	<c:set var="classVar" value=""/>
		  	<c:if test="${USERDATA.username eq contrib.account }">
		  		<c:set var="listed" value="1"/>
		  		<c:set var="classVar" value="success"/>
		  	</c:if>
		  	<tr class="${classVar }">
		  		<c:set var="rank" value="${rank + 1 }"/>
		  		<td>${rank }</td>
		  		<td>
			  	<c:choose>
			  		<c:when test="${contrib.donate_percent >= 2}">
			  			<i class="fa fa-trophy fa-fw"></i>
			  		</c:when>
			  		<c:when test="${contrib.donate_percent < 2 and contrib.donate_percent > 0 }">
			  			<i class="fa fa-star-o fa-fw"></i>
			  		</c:when>
			  		<c:otherwise>
			  			<i class="fa fa-ban fa-fw"></i>
			  		</c:otherwise>
			  	</c:choose>
			  	</td>
			  	<c:set var="tdAccount" value="${contrib.account }"/>
			  	<c:if test="${contrib.is_anonymous == 1 and USERDATA.is_admin == 0 }">
			  		<c:set var="tdAccount" value="anonymous"/>
			  	</c:if>
			  	<td>${tdAccount }</td>
			  	<fmt:parseNumber var="hashrate" integerOnly="true" value="${contrib.hashrate}"/>
			  	<td class="text-right">${hashrate }</td>
			  	<td class="text-right"><fmt:formatNumber value="${estday}" pattern="0.000" /></td>
			  	<!-- {if $GLOBAL.config.price.enabled}
              		 {if $GLOBAL.config.price.currency}<td class="text-right">{($estday * $GLOBAL.price)|default:"n/a"|number_format:"4"}</td>{/if}
              		 {/if} 
              	-->
		  	</tr>
          </c:forEach>
          
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


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-tasks fa-fw"></i> Last ${BLOCKLIMIT} Blocks Found
      </div>
      <div class="panel-body no-padding">
        <div class="table-responsive">
          <table class="table table-striped table-bordered table-hover">
            <thead>
              <tr>
                <th class="text-center">Block</th>
                <th class="text-center">Validity</th>
                <th class="text-left">Finder</th>
                <th class="text-right">Time</th>
                <th class="text-right">Difficulty</th>
                <th class="text-right">Amount</th>
                <th class="text-right">Expected Shares</th>
                <c:if test="${payout_system == 'pplns' }">
                	<th class="text-right">PPLNS Shares</th>
                </c:if>
                <th class="text-right">Actual Shares</th>
                <th  class="text-right">Percentage</th>
              </tr>
            </thead>
            <tbody>
            	<c:set var="count" value="0"/>
            	<c:set var="totalexpectedshares" value="0"/>
            	<c:set var="totalshares" value="0"/>
            	<c:set var="pplnsshares" value="0"/>
            	<c:forEach	var="block" items="${BLOCKSFOUND }">
            		<tr>
		            	<c:set var="totalshares" value="${totalshares + block.shares }"/>
		            	<c:set var="count" value="${count + 1 }"/>
		            	<c:if test="${payout_system == 'pplns' }">
		            		<c:set var="pplnsshares" value="${pplnsshares + block.pplns_shares}"/>
		            	</c:if>
		            	<!-- {if ! $GLOBAL.website.blockexplorer.disabled}
		                <td class="text-center"><a href="{$smarty.server.SCRIPT_NAME}?page=statistics&action=round&height={$BLOCKSFOUND[block].height}">{$BLOCKSFOUND[block].height}</a></td>
		              	{else}
		                <td class="text-right">{$BLOCKSFOUND[block].height}</td>
		              	{/if} -->
		              	<td class="text-center">
		              	<c:choose>
		              	<c:when test="${block.confirmations >= confirmations }">
		            		<span class="label label-success">Confirmed</span>  	
		              	</c:when>
		              	<c:when test="${block.confirmations == -1 }">
		            		<span class="label label-danger">Orphan</span>	
		              	</c:when>
		              	<c:otherwise>
		              		<span class="label label-warning">${confirmations - block.confirmations} left</span>
		              	</c:otherwise>
		              	</c:choose>
						</td>
						<td>
						<c:choose>
						<c:when test="${block.is_anonymous == 1 and USERDATA.is_admin == 0 }">
							anonymous
						</c:when>
						<c:otherwise>
							${block.finder}
						</c:otherwise>
						</c:choose>
						</td>
						<td class="text-right">${block.time}</td>
						<td class="text-right"><fmt:formatNumber value="${block.difficulty }" pattern="0.0000" /></td>
						<td class="text-right"><fmt:formatNumber value="${block.amount }" pattern="0.00" /></td>
						<td class="text-right">
						<c:set var="totalexpectedshares" value="${totalexpectedshares + block.estshares }"/>
						<fmt:parseNumber var="tdEstshares" integerOnly="true" value="${block.estshares}"/>
	                  	${tdEstshares }
	                	</td>	     
	                	<c:if test="${payout_system == 'pplns' }">
	                	<fmt:parseNumber var="tdPplns_shares" integerOnly="true" value="${block.pplns_shares}"/>
	                		<td class="text-right">${tdPplns_shares}</td>
	                	</c:if>
	                	<fmt:parseNumber var="tdShares" integerOnly="true" value="${block.shares}"/>
	                	<td class="text-right">${tdShares }</td>  
	                	<td class="text-right">
	                	<c:set var="percentage" value="${block.shares / block.estshares * 100 }"/>
	                	<c:set var="colorVar" value="red"/>
	                	<c:if test="${(percentage <= 100) }">
	                		<c:set var="colorVar" value="green"/>
	                	</c:if>
	                	<c:if test="${(percentage <= 115) }">
	                		<c:set var="colorVar" value="orange"/>
	                	</c:if>
	                  	<font color="${colorVar }"><fmt:formatNumber value="${percentage}" pattern="0.00" /></font>
	                	</td>
                	</tr>       	
            	</c:forEach>
              <tr>
                <td colspan="6"><b>Totals</b></td>
                <fmt:parseNumber var="tdTotalexpectedshares" integerOnly="true" value="${totalexpectedshares}"/>
                <td class="text-right">${tdTotalexpectedshares }</td>
                <c:if test="${payout_system == 'pplns' }">
                	<fmt:parseNumber var="tdPplnsshares" integerOnly="true" value="${pplnsshares}"/>
                	<td class="text-right">${tdPplnsshares }</td>
                </c:if>
                <fmt:parseNumber var="tdTotalshares" integerOnly="true" value="${totalshares}"/>
                <td class="text-right">${tdTotalshares }</td> 
                <td class="text-right">
                <c:if test="${count > 0}">
                	<c:set var="colorVar" value="red"/>
                	<c:if test="${((totalshares / totalexpectedshares * 100) <= 100) }">
                		<c:set var="colorVar" value="green"/>	
                	</c:if>
                	<c:if test="${((totalshares / totalexpectedshares * 100) <= 115) }">
                		<c:set var="colorVar" value="orange"/>	
                	</c:if>
                	<font color="${colorVar }"><fmt:formatNumber value="${(totalshares / totalexpectedshares * 100)}" pattern="0.00" /></font>
                </c:if>
                <c:if test="${count <= 0}">
                	0
                </c:if>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="panel-footer">
        <c:if test="${payout_system != 'pps'}">
        	<h6>Round Earnings are not credited until <font class="confirmations">${confirmations}</font> confirms.</h6>
        </c:if>
      </div>
    </div>
  </div>
</div>

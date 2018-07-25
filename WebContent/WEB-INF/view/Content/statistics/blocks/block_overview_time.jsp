<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-clock-o fa-fw"></i> Block Overview
      </div>
      <div class="panel-body no-padding">
        <div class="table-responsive">
          <table class="table table-striped table-bordered table-hover">
            <thead>
              <tr>
                <th></th>
                <th class="text-right">Gen. Est.</th>
                <th class="text-right">Found</th>
                <th class="text-right">Valid</th>
                <th class="text-right">Orphan</th>
                <th class="text-right">Orphan %</th>
                <th class="text-right">Avg. Diff</th>
                <th class="text-right">Shares Est.</th>
                <th class="text-right">Shares</th>
                <th class="text-right">Percentage</th>
                <th class="text-right">Amount</th>
                <th class="text-right">Rate Est.</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th>All Time</th>
                <td class="text-right"><fmt:formatNumber value="${FIRSTBLOCKFOUND / COINGENTIME}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.total}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.totalValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.totalOrphan}" pattern="#,###" /></td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.totalOrphan > 0 }" >
                		<fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.total * LASTBLOCKSBYTIME.totalOrphan) }" pattern="0.00" />
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.totalOrphan <= 0 }" >
                		0.00
                	</c:if>
                </td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.totalValid > 0 }" >
                	<fmt:formatNumber value="${LASTBLOCKSBYTIME.totalDifficulty / LASTBLOCKSBYTIME.totalValid }" pattern="0.0000" />
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.totalValid <= 0 }" >
                	0
                </c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.totalEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.totalShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.totalEstimatedShares > 0 }">
                	<c:set var="colorVar" value="red"/>
                	<c:if test="${((LASTBLOCKSBYTIME.totalShares / LASTBLOCKSBYTIME.totalEstimatedShares * 100) <= 100) }">
                		<c:set var="colorVar" value="green"/>
                	</c:if>
                	<c:if test="${(($LASTBLOCKSBYTIME.totalShares / $LASTBLOCKSBYTIME.totalEstimatedShares * 100) <= 115) }">
                		<c:set var="colorVar" value="orange"/>
                	</c:if>
                	<font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.totalShares / $LASTBLOCKSBYTIME.totalEstimatedShares * 100) }" pattern="0.00" />%</font></b>
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.totalEstimatedShares <= 0 }">
                	0.00%
                </c:if>
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.totalAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.total / (FIRSTBLOCKFOUND / COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
              <tr>
                <th>Last Hour</th>
                <td class="text-right"><fmt:formatNumber value="${(3600 / COINGENTIME)}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.oHourTotal}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.oHourValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.oHourOrphan}" pattern="#,###" /></td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.oHourOrphan > 0 }">
	                	<fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.oHourTotal * LASTBLOCKSBYTIME.oHourOrphan)}" pattern="0.00" />
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.oHourOrphan <= 0 }">
	                	0.00
	                </c:if>
	            </td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.oHourValid > 0 }">
	                	<fmt:formatNumber value="${(LASTBLOCKSBYTIME.oHourDifficulty / LASTBLOCKSBYTIME.oHourValid) }" pattern="0.0000" />
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.oHourValid <= 0 }">
	                	0
	                </c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.oHourEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.oHourShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.oHourEstimatedShares > 0 }">
	                	<c:set var="colorVar" value="red"/>
	                	<c:if test="${((LASTBLOCKSBYTIME.oHourShares / LASTBLOCKSBYTIME.oHourEstimatedShares * 100) <= 100) }">
	                		<c:set var="colorVar" value="green"/>
	                	</c:if>
	                	<c:if test="${((LASTBLOCKSBYTIME.oHourShares / LASTBLOCKSBYTIME.oHourEstimatedShares * 100) <= 115) }">
	                		<c:set var="colorVar" value="orange"/>
	                	</c:if>
	                	<font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.oHourShares / LASTBLOCKSBYTIME.oHourEstimatedShares * 100)}" pattern="0.00" />%</font></b>
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.oHourEstimatedShares <= 0 }">
	                	0.00%
	                </c:if>
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.oHourAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.oHourTotal / (3600 / $COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
              <tr>
                <th style="padding-left:3px;padding-right:1px;">Last 24 Hours</th>
                <td class="text-right"><fmt:formatNumber value="${(86400 / COINGENTIME)}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.tfHourTotal}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.tfHourValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.tfHourOrphan}" pattern="#,###" /></td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.tfHourOrphan > 0}">
                		<td class="text-right"><fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.tfHourTotal * LASTBLOCKSBYTIME.tfHourOrphan)}" pattern="0.00" />
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.tfHourOrphan <= 0}">
                		0.00
                	</c:if>
                </td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.tfHourValid > 0 }">
                	<fmt:formatNumber value="${(LASTBLOCKSBYTIME.tfHourDifficulty / LASTBLOCKSBYTIME.tfHourValid) }" pattern="0.0000" />
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.tfHourValid <= 0 }">
                	0
                </c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.tfHourEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.tfHourShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.tfHourEstimatedShares > 0 }">
                	<c:set var="colorVar" value="red"/>
	                <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 100) }">
	                	<c:set var="colorVar" value="green"/>
	                </c:if>
	                <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 115) }">
	                	<c:set var="colorVar" value="orange"/>
	                </c:if>
	                <font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100)}" pattern="0.00" />%</font></b>
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.tfHourEstimatedShares <= 0 }">
                	0.00%
                </c:if>
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.tfHourAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.tfHourTotal / (86400 / COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
              <tr>
                <th>Last 7 Days</th>
                <td class="text-right"><fmt:formatNumber value="${(604800 / COINGENTIME)}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.sDaysTotal}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.sDaysValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.sDaysOrphan}" pattern="#,###" /></td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.sDaysOrphan > 0}">
	                	<fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.sDaysTotal * LASTBLOCKSBYTIME.sDaysOrphan)}" pattern="0.00" />
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.sDaysOrphan <= 0}">
	                	0.00
	                </c:if>
                </td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.sDaysValid > 0 }">
	                	<fmt:formatNumber value="${(LASTBLOCKSBYTIME.sDaysDifficulty / LASTBLOCKSBYTIME.sDaysValid) }" pattern="0.0000" />
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.sDaysValid <= 0 }">
	                	0
	                </c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.sDaysEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.sDaysShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
	                <c:if test="${LASTBLOCKSBYTIME.sDaysEstimatedShares > 0 }">
	                	<c:set var="colorVar" value="red"/>
		                <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 100) }">
		                	<c:set var="colorVar" value="green"/>
		                </c:if>
		                <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 115) }">
		                	<c:set var="colorVar" value="orange"/>
		                </c:if>
		                <font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.sDaysShares / LASTBLOCKSBYTIME.sDaysEstimatedShares * 100)}" pattern="0.00" />%</font></b>
	                </c:if>
	                <c:if test="${LASTBLOCKSBYTIME.sDaysEstimatedShares <= 0 }">
	                	0.00%
	                </c:if> 
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.sDaysAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.sDaysTotal / (604800 / COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
              <tr>
                <th>Last 4 Weeks</th>
                <td class="text-right"><fmt:formatNumber value="${(2419200 / COINGENTIME)}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.fWeeksTotal}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.fWeeksValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.fWeeksOrphan}" pattern="#,###" /></td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.fWeeksOrphan > 0 }">
                		<fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.fWeeksTotal * LASTBLOCKSBYTIME.fWeeksOrphan)}" pattern="0.00" />
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.fWeeksOrphan <= 0 }">
                		0.00
                	</c:if>
                </td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.fWeeksValid > 0 }">
                	<fmt:formatNumber value="${(LASTBLOCKSBYTIME.fWeeksDifficulty / LASTBLOCKSBYTIME.fWeeksValid) }" pattern="0.0000" />
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.fWeeksValid <= 0 }">
                	0
                </c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.fWeeksEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.fWeeksShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
                <c:if test="${LASTBLOCKSBYTIME.fWeeksEstimatedShares > 0 }">
                	<c:set var="colorVar" value="red"/>
		            <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 100) }">
		                <c:set var="colorVar" value="green"/>
		            </c:if>
		            <c:if test="${((LASTBLOCKSBYTIME.tfHourShares / LASTBLOCKSBYTIME.tfHourEstimatedShares * 100) <= 115) }">
		            	<c:set var="colorVar" value="orange"/>
		            </c:if>
		            <font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.fWeeksShares / LASTBLOCKSBYTIME.fWeeksEstimatedShares * 100)}" pattern="0.00" />%</font></b>
                </c:if>
                <c:if test="${LASTBLOCKSBYTIME.fWeeksEstimatedShares <= 0 }">
                	0.00%
                </c:if>
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.fWeeksAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.fWeeksTotal / (2419200 / COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
              <tr>
                <th>The Past 12 Months</th>
                <td class="text-right"><fmt:formatNumber value="${(29030400 / COINGENTIME)}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.otMonthTotal}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.otMonthValid}" pattern="#,###" /></td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.otMonthOrphan}" pattern="#,###" /></td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.otMonthOrphan > 0}">
                		<fmt:formatNumber value="${(100 / LASTBLOCKSBYTIME.otMonthTotal * LASTBLOCKSBYTIME.otMonthOrphan)}" pattern="0.00" />
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.otMonthOrphan <= 0}">
                		0.00
                	</c:if>
                </td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.otMonthValid > 0 }">
                		<fmt:formatNumber value="${(LASTBLOCKSBYTIME.otMonthDifficulty / LASTBLOCKSBYTIME.otMonthValid) }" pattern="0.0000" />
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.otMonthValid <= 0 }">
                		0
                	</c:if>
                </td>
                <fmt:parseNumber var="estimatedShares" integerOnly="true" value="${LASTBLOCKSBYTIME.otMonthEstimatedShares}"/>
                <fmt:parseNumber var="shares" integerOnly="true" value="${LASTBLOCKSBYTIME.otMonthShares}"/>
                <td class="text-right">${estimatedShares}</td>
                <td class="text-right">${shares}</td>
                <td class="text-right">
                	<c:if test="${LASTBLOCKSBYTIME.otMonthEstimatedShares > 0 }">
                		<c:set var="colorVar" value="red"/>
		            	<c:if test="${((LASTBLOCKSBYTIME.otMonthShares / LASTBLOCKSBYTIME.otMonthEstimatedShares * 100) <= 100) }">
		                	<c:set var="colorVar" value="green"/>
		            	</c:if>
		            	<c:if test="${((LASTBLOCKSBYTIME.otMonthShares / LASTBLOCKSBYTIME.otMonthEstimatedShares * 100) <= 115) }">
		            		<c:set var="colorVar" value="orange"/>
		            	</c:if>
		            	<font color="${colorVar}"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.otMonthShares / LASTBLOCKSBYTIME.otMonthEstimatedShares * 100)}" pattern="0.00" />%</font></b>
                	</c:if>
                	<c:if test="${LASTBLOCKSBYTIME.otMonthEstimatedShares <= 0 }">
                		0.00%
                	</c:if>
                </td>
                <td class="text-right"><fmt:formatNumber value="${LASTBLOCKSBYTIME.otMonthAmount}" pattern="0.00" /></td>
                <td class="text-right"><fmt:formatNumber value="${(LASTBLOCKSBYTIME.otMonthTotal / (29030400 / COINGENTIME)  * 100)}" pattern="0.00" />%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="panel-footer">
        <c:if test="${payout_system != 'pps'}">
        	<h6>Round earnings are not credited until <font class="confirmations">${confirmations}</font> confirms.</h6>
        </c:if>
      </div>
    </div>
  </div>
</div>

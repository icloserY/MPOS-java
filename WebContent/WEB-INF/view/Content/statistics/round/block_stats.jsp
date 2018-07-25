<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-bar-chart fa-fw"></i> Block Statistics
      </div>
      <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <tbody>
            <tr>
              <td colspan="8">
                <a href="Round.do?height=${BLOCKDETAILS.height}&prev=1"><i class="fa fa-chevron-left fa-fw"></i></a>
                <a href="Round.do?height=${BLOCKDETAILS.height}&next=1"><i class="fa fa-chevron-right fa-fw pull-right"></i></a>
              </td>
            </tr>
            <tr class="odd">
              <td>ID</td>
              <c:set var="tdId" value="0"/>
              <c:if test="${BLOCKDETAILS.id != null}">
              	<c:set var="tdId" value="${BLOCKDETAILS.id}"/>
              </c:if>
              <td><fmt:formatNumber value="${tdId}" pattern="#,###" /></td>
              <td>Height</td>
	              <!-- need change  
	              {if ! $GLOBAL.website.blockexplorer.disabled}
	              <td><a href="{$GLOBAL.website.blockexplorer.url}{$BLOCKDETAILS.blockhash}" target="_new">{$BLOCKDETAILS.height|number_format:"0"|default:"0"}</a></td>
	              {else}
	              <td>{$BLOCKDETAILS.height|number_format:"0"|default:"0"}</td>
	              {/if}
	              -->
	          <c:set var="tdHeight" value="0"/>
              <c:if test="${BLOCKDETAILS.height != null}">
              	<c:set var="tdHeight" value="${BLOCKDETAILS.height}"/>
              </c:if>
              <td><a href="http://explorer.litecoin.net/block/${BLOCKDETAILS.blockhash}" target="_new"><fmt:formatNumber value="${tdHeight}" pattern="#,###" /></a></td>
              <td>Amount</td>
              <c:set var="tdAmount" value="0"/>
              <c:if test="${BLOCKDETAILS.amount != null}">
              	<c:set var="tdAmount" value="${BLOCKDETAILS.amount}"/>
              </c:if>
              <td>${tdAmount }</td>
              <td>Confirmations</td>
              <td>
              <c:choose>
              	<c:when test="${BLOCKDETAILS.confirmations >= confirmations }">
              		<font color="green">Confirmed</font>	
              	</c:when>
              	<c:when test="${BLOCKDETAILS.confirmations == -1 }">
              		<font color="red">Orphan</font>
              	</c:when>
              	<c:when test="${BLOCKDETAILS.confirmations == null or BLOCKDETAILS.confirmations == 0 }">
              		0
              	</c:when>
              	<c:otherwise>
              		${(confirmations - BLOCKDETAILS.confirmations)} left
              	</c:otherwise>
              </c:choose>
              </td>
            </tr>
            <tr class="even">
              <td>Difficulty</td>
              <c:set var="tdDifficulty" value="0"/>
              <c:if test="${BLOCKDETAILS.difficulty != null}">
              	<c:set var="tdDifficulty" value="${BLOCKDETAILS.difficulty}"/>
              </c:if>
              <td>${tdDifficulty}</td>
              <td>Time</td>
              <c:set var="tdTime" value="0"/>
              <c:if test="${BLOCKDETAILS.time != null}">
              	<c:set var="tdTime" value="${BLOCKDETAILS.time}"/>
              </c:if>
              <td>${tdTime}</td>
              <td>Shares</td>
              <c:set var="tdShares" value="0"/>
              <c:if test="${BLOCKDETAILS.shares != null}">
              	<c:set var="tdShares" value="${BLOCKDETAILS.shares}"/>
              </c:if>
              <fmt:parseNumber var="tdShares" integerOnly="true" value="${tdShares}"/>
              <td>${tdShares}</td>
              <td>Finder</td>
              <c:set var="tdFinder" value="unknown"/>
              <c:if test="${BLOCKDETAILS.finder != null && BLOCKDETAILS.finder eq '' }">
              	<c:set var="tdFinder" value="${BLOCKDETAILS.finder }"/>
              </c:if>
              <td>${tdFinder }</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="panel-footer">
        <div class="submit_link">
          <form action="Round.do" method="POST" id='search'>
            <input type="hidden" name="page" value="{$smarty.request.page|escape}">
            <input type="hidden" name="action" value="{$smarty.request.action|escape}">
            <c:set var="tdText" value="%"/>
            <c:if test="${height != null and height ne ''}">
            	<c:set var="dtText" value="${height }"/>	
            </c:if>
            <input type="text" class="pin" name="search" value="${tdText }">
            <input type="submit" value="Search" class="alt_btn">
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

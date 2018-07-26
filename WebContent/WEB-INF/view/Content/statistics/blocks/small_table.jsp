<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-th fa-fw"></i> Last Found Blocks
      </div>
      <div class="panel-body no-padding table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <thead>
            <tr>
              <th>Block</th>
              <th>Finder</th>
              <th>Time</th>
              <th class="text-right">Actual Shares</th>
            </tr>
          </thead>
          <tbody>
          <c:set var="rank" value="1"/>
          <c:forEach var="block" items="${BLOCKSFOUND }">
          <tr>
          	<!-- need change 
          	  {if ! $GLOBAL.website.blockexplorer.disabled}
              <td><a href="{$GLOBAL.website.blockexplorer.url}{$BLOCKSFOUND[block].blockhash}" target="_new">{$BLOCKSFOUND[block].height}</a></td>
              {else}
              <td>{$BLOCKSFOUND[block].height}</td>
              {/if} 
              -->
              <td>${block.height }</td>
              <c:set var="tdVar" value="unknown"/>
              <c:if test="${block.finder != null and block.finder ne '' }">
              	<c:set var="tdVar" value="${block.finder }"/>
              </c:if>
              <c:if test="${block.is_anonymous == 1 and USERDATA.is_admin == 0}">
              	<c:set var="tdVar" value="anonymous"/>
              </c:if>
              <td>${tdVar }</td>
              <!-- need change
				<td>{$BLOCKSFOUND[block].time|date_format:$GLOBAL.config.date}</td>
			   -->
			  <td>${block.time }</td>
			  <fmt:parseNumber var="shares" integerOnly="true" value="${block.shares}"/>
			  <td class="text-right">${shares }</td>
          </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <c:if test="${payout_system ne 'pps' }">
      	<div class="panel-footer">
          <h6>Note: Round Earnings are not credited until <font class="confirmations">${confirmations}</font> confirms.</h6>
      	</div>
      </c:if>
    </div>
  </div>

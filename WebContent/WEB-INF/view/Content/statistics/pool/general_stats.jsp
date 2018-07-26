<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pf" tagdir="/WEB-INF/tags" %>
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-bar-chart-o fa-fw"></i> General Statistics
      </div>
      <div class="panel-body no-padding table-responsive">
        <table class="table table-striped table-bordered table-hover">
          <tbody>
            <tr>
              <th width="50%">Pool Hash Rate</th>
              <!-- $GLOBAL.hashrate ?  
              <td width="70%"><span id="b-hashrate">{$GLOBAL.hashrate|number_format:"3"}</span> {$GLOBAL.hashunits.pool}</td> -->
              <td width="70%"><span id="b-hashrate"> 0.000 </span> KH/s</td>
            </tr>
            <tr>
              <th>Pool Efficiency</th>
              <td>
              <c:choose>
              	<c:when test="${roundshares.valid > 0 }">
              		<fmt:formatNumber value="${roundshares.valid / (roundshares.valid + roundshares.invalid ) * 100 }" pattern="0.00" />%
              	</c:when>
              	<c:otherwise>
              		0%
              	</c:otherwise>
              </c:choose>
              </td>
            </tr>
            <tr>
              <th>Current Active Workers</th>
              <!-- $GLOBAL.workers ? 
              <td id="b-workers">{$GLOBAL.workers|number_format}</td> -->
              <td id="b-workers">0</td>
            </tr>
            <tr>
              <th>Current Difficulty</th>
              <!-- $GLOBAL.website.chaininfo.disabled ?
              {if ! $GLOBAL.website.chaininfo.disabled}
              <td><a href="{$GLOBAL.website.chaininfo.url}" target="_new"><span id="b-diff">{$NETWORK.difficulty|number_format:"8"}</span></a></td>
              {else}
              <td><span id="b-diff">{$NETWORK.difficulty|number_format:"8"}</span></td>
              {/if} -->
              <td><span id="b-diff"><fmt:formatNumber value="${NETWORK.difficulty }" pattern="0.00000000" /></span>
            </tr>
            <tr>
              <th>Est. Next Difficulty</th>
              <!-- {if ! $GLOBAL.website.chaininfo.disabled}
              <td><a href="{$GLOBAL.website.chaininfo.url}" target="_new">{$NETWORK.EstNextDifficulty|number_format:"8"} (Change in {$NETWORK.BlocksUntilDiffChange} Blocks)</a></td>
              {else}
              <td>{$NETWORK.EstNextDifficulty|number_format:"8"} (Change in {$NETWORK.BlocksUntilDiffChange} Blocks)</td>
              {/if} -->
              <td><fmt:formatNumber value="${NETWORK.estNextDifficulty }" pattern="0.00000000" /> (Change in ${NETWORK.blocksUntilDiffChange} Blocks)</td>
            </tr>
            <tr>
              <th>Est. Avg. Time per Round (Network)</th>
              <pf:seconds_to_words var="tdEstTimePerBlock" timeVal="${NETWORK.estTimePerBlock }"/>
              <td>${tdEstTimePerBlock }</td>
            </tr>
            <tr>
              <th>Est. Avg. Time per Round (Pool)</th>
              <pf:seconds_to_words var="tdESTTIME" timeVal="${ESTTIME }"/>
              <td>${tdESTTIME }</td>
            </tr>
            <tr>
              <th>Est. Shares this Round</th>
              <fmt:parseNumber var="tdVar" integerOnly="true" value="${ESTIMATES.shares}"/>
              <td id="b-target">${tdVar} (done: ${ESTIMATES.percent}%)</td>
            </tr>
           <!--  {if ! $GLOBAL.website.blockexplorer.disabled}
            <tr>
              <th width="50%">Next Network Block</th>
              <td colspan="3">{($CURRENTBLOCK + 1)|number_format} &nbsp;&nbsp; (Current: <a href="{$GLOBAL.website.blockexplorer.url}{$CURRENTBLOCKHASH}" target="_new">{$CURRENTBLOCK|number_format})</a></td>
            </tr>
            {else}
            <tr>
              <th>Next Network Block</th>
              <td colspan="3">{($CURRENTBLOCK + 1)|number_format} &nbsp;&nbsp; (Current: {$CURRENTBLOCK|number_format})</td>
            </tr>
            {/if} -->
            <tr>
              <th>Next Network Block</th>
              <td colspan="3"><fmt:formatNumber value="${CURRENTBLOCK + 1}" pattern="0" /> &nbsp;&nbsp; (Current: <fmt:formatNumber value="${CURRENTBLOCK }" pattern="0" />)</td>
            </tr>
            <tr>
              <th>Last Block Found</th>
              <td colspan="3"><a href="{Round.do?height=${LASTBLOCK}" target="_new"><fmt:formatNumber value="${LASTBLOCK }" pattern="0" /></a></td>
            </tr>
            <tr>
              <th>Time Since Last Block</th>
              <!-- <td colspan="3">{$TIMESINCELAST|seconds_to_words}</td> -->
              <pf:seconds_to_words var="tdTIMESINCELAST" timeVal="${TIMESINCELAST }"/>
              <td colspan="3">${tdTIMESINCELAST }
            </tr>
          </tbody>
        </table>
      </div>
      <div class="panel-footer">
        <!-- <h6>{if !$GLOBAL.website.api.disabled}These stats are also available in JSON format <a href="{$smarty.server.SCRIPT_NAME}?page=api&action=getpoolstatus&api_key={$GLOBAL.userdata.api_key|default:""}">HERE</a>{/if}</h6> -->
        <h6>These stats are also available in JSON format <a href="{$smarty.server.SCRIPT_NAME}?page=api&action=getpoolstatus&api_key={$GLOBAL.userdata.api_key|default:""}">HERE</a></h6>
      </div>
    </div>
  </div>

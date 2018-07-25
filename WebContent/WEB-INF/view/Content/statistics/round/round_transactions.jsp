<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        Round Transactions
      </div>
      <div class="panel-body">
        <div class="table-responsive">
          <table class="table table-striped table-bordered table-hover {if $ROUNDTRANSACTIONS}datatable{/if}">
            <thead>
              <tr>
                <th>User Name</th>
                <th>Type</th>
                <th>Round Shares</th>
                <th>Round %</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach var="txs" items="${ROUNDTRANSACTIONS }">
            <c:set var="styleVar" value=""/>
            <c:if test="${USERDATA.username eq txs.username }">
            	<c:set var="styleVar" value="background-color:#99EB99;"/>
            </c:if>
            <tr style="${styleVar}">
            	<td>
            	<c:set var="tdVar" value="unknown"/>
            	<c:if test="${txs.username != null and txs.username ne ''}">
            		<c:set var="tdVar" value="${txs.username }"/>
            	</c:if>
            	<c:if test="${txs.is_anonymous == 1 and USERDATA.is_admin == 0 }">
            		<c:set var="tdVar" value="anonymous"/>
            	</c:if>
            	${tdVar }
            	</td>
            	<c:set var="tdType" value=""/>
            	<c:if test="${txs.type != null}">
            		<c:set var="tdType" value="${txs.type }"/>
            	</c:if>
            	<td>${tdType }</td>
            	<c:set var="tdUid" value="0"/>
            	<c:if test="${txs.uid != null }">
            		<c:set var="tdUid" value="${txs.uid }"/>
            	</c:if>
            	<fmt:parseNumber var="tdUid" integerOnly="true" value="${tdUid}"/>
            	<td>${tdUid }</td>
            	<td><fmt:formatNumber value="${(( 100 / BLOCKDETAILS.shares) * ROUNDSHARES[txs.uid].valid) }" pattern="0.00" /></td>
            	<c:set var="tdAmount" value="0"/>
            	<c:if test="${txs.amount != null }">
            		<c:set var="tdAmount" value="${txs.amount }"/>
            	</c:if>
            	<!-- need change |number_format:"8" -->
            	<td>${tdAmount}</td>
            	</tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

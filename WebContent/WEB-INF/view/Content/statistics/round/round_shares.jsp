<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
  <div class="col-lg-6">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-refresh fa-fw"></i> Round Shares
      </div>
      <div class="panel-body">
        <div class="table-responsive">
          <table class="table table-striped table-bordered table-hover {if $ROUNDSHARES}datatable{/if}">
            <thead>
              <tr>
                <th>Rank</th>
                <th>User Name</th>
                <th>Valid</th>
                <th>Invalid</th>
                <th>Invalid %</th>
              </tr>
            </thead>
            <tbody> 
            <c:set var="rank" value="1" />
            <c:set var="listed" value="0"/>
            <c:set var="styleVar" value=""/>
            <c:forEach var="data" items="${ROUNDSHARES }">
            <c:if test="${USERDATA.username eq data.username }">
            	<c:set var="listed" value="1"/>
            	<c:set var="styleVar" value="background-color:#99EB99;"/>		
            </c:if>
            <tr style="${styleVar}">	
	            <c:set var="rank" value="${rank + 1 }"/>
          		<td>${rank}</td>
	            <c:set var="tdUsername" value="unknown"/>
	            <c:choose>
	            	<c:when test="${data.is_anonymous == 1 and USERDATA.is_admin == 0 }">
	            		<c:set var="tdUsername" value="anonymous"/>	
	            	</c:when>
	            	<c:when test="${data.username != null and data.username ne '' }">
	            		<c:set var="tdUsername" value="${data.username }"/>
	            	</c:when>
	            </c:choose>
	            <td>${tdUsername }</td>
	            <c:set var="tdValid" value="0"/>
	            <c:if test="${data.valid != null }">
	            	<c:set var="tdValid" value="${data.valid }"/>
	            </c:if>
	            <fmt:parseNumber var="tdValid" integerOnly="true" value="${tdValid}"/>
	            <td>${tdValid }</td>
	            <c:set var="tdInvalid" value="0"/>
	            <c:if test="${data.invalid != null }">
	            	<c:set var="tdInvalid" value="${data.invalid }"/>
	            </c:if>
	            <fmt:parseNumber var="tdInvalid" integerOnly="true" value="${tdInvalid}"/>
	            <td>${tdInvalid }</td>
	            <td>
	            <c:if test="${data.invalid > 0}">
	            	<fmt:formatNumber value="${(data.invalid / data.valid * 100)}" pattern="0.00" />
	            </c:if>
	            <c:if test="${data.invalid <= 0}">
	            	0.00
	            </c:if>
	            </td>
            </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

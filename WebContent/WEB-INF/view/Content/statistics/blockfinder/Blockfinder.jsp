<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
<jsp:include page="/WEB-INF/view/Content/statistics/blockfinder/finder_top.jsp" flush="false" />
<c:if test="${AUTHENTICATED == 1 }">
	<jsp:include page="/WEB-INF/view/Content/statistics/blockfinder/finder_own.jsp" flush="false" />
</c:if> 
</div>
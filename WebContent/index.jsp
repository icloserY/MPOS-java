<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0; charset=UTF-8">
<c:set var="title" value="Home"/>
<c:if test="${titleValue != null}">
	<c:set var="title" value="${titleValue }"/>	
</c:if>	
<title>- ${title }</title>

<!--[if lt IE 9]>
  <link rel="stylesheet" href="{$PATH}/css/ie.css" type="text/css" media="screen" />
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

<!--[if IE]><script type="text/javascript" src="{$PATH}/js/excanvas.js"></script><![endif]-->

<link href="./Resources/site_assets/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/css/bootstrap-switch.min.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/font-awesome/css/font-awesome.min.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/css/plugins/morris/morris-0.5.1.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/css/plugins/timeline/timeline.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/css/plugins/dataTables/dataTables.bootstrap.css"
	rel="stylesheet">
<link href="./Resources/site_assets/bootstrap/css/mpos.css"
	rel="stylesheet">
<link
	href="./Resources/site_assets/bootstrap/css/plugins/metisMenu/metisMenu.css"
	rel="stylesheet">
<link href="./Resources/site_assets/bootstrap/css/sparklines.css"
	rel="stylesheet">

<script src="./Resources/site_assets/bootstrap/js/jquery.min.js"></script>
<script src="./Resources/site_assets/bootstrap/js/jquery.cookie.js"></script>
<script src="./Resources/site_assets/bootstrap/js/jquery.md5.js"></script>
<script src="./Resources/site_assets/bootstrap/js/bootstrap.min.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/bootstrap-switch.min.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/dataTables/jquery.dataTables.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/raphael-2.1.2.min.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/morris/morris-0.5.1.min.js"></script>
<script
	src="./Resources/site_assets/bootstrap/js/plugins/sparkline/jquery.sparkline.min.js"></script>
<script src="./Resources/site_assets/bootstrap/js/mpos.js"></script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/Layout/header.jsp" flush="false" />
		<jsp:include page="/WEB-INF/view/Layout/navigation.jsp" flush="false" />
		<div id="page-wrapper">
			<br />
			<c:if test="${Errors != null}">
				<c:set var="classNameHead" value="alert-dismissable " />
				<c:set var="classNameTail" value=" autohide " />
				<c:forEach var="popup" items="${Errors}">
					<c:set var="classNameDefault" value="${popup.get('Type')}" />
					<c:choose>
						<c:when test="${popup.get('Dismiss') == 'yes'}">
							<c:set var="className"
								value="${classNameHead} ${classNameDefault}" />
						</c:when>
						<c:otherwise>
							<c:set var="className" value="${classNameDefault }" />
						</c:otherwise>
					</c:choose>

					<div class="${className}" id="${popup.get('ID')}">
						<c:if test="${popup.get('Dismiss') == 'yes'}">
							<button id="${popup.get('ID')}" type="button" class="close"
								data-dismiss="alert" aria-hidden="true">&times;</button>
						</c:if>
						<c:choose>
							<c:when test="${popup.get('Type') == 'alert alert-info'}">
								<span class="glyphicon glyphicon-info-sign">&nbsp;</span>
							</c:when>
							<c:when test="${popup.get('Type') == 'alert alert-warning'}">
								<span class="glyphicon glyphicon-info-sign">&nbsp;</span>
							</c:when>
							<c:when test="${popup.get('Type') == 'alert alert-danger'}">
								<span class="glyphicon glyphicon-remove-circle">&nbsp;</span>
							</c:when>
							<c:when test="${popup.get('Type') == 'alert alert-success'}">
								<span class="glyphicon glyphicon-ok-circle">&nbsp;</span>
							</c:when>
						</c:choose>
						${popup.get('Content')}
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${Content != null}">
				<jsp:include page="${Content}" flush="false" />
			</c:if>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/Layout/footer.jsp" flush="false" />
</body>
</html>
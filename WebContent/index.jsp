<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en"> 
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0; charset=UTF-8">
  <title> - {$smarty.request.page|escape|default:"home"|capitalize}</title>
  
  <!--[if lt IE 9]>
  <link rel="stylesheet" href="{$PATH}/css/ie.css" type="text/css" media="screen" />
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <!--[if IE]><script type="text/javascript" src="{$PATH}/js/excanvas.js"></script><![endif]-->
  
  <link href="./Resources/site_assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/bootstrap-switch.min.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/font-awesome/css/font-awesome.min.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/plugins/morris/morris-0.5.1.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/plugins/timeline/timeline.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/mpos.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/plugins/metisMenu/metisMenu.css" rel="stylesheet">
  <link href="./Resources/site_assets/bootstrap/css/sparklines.css" rel="stylesheet">
  
  <script src="./Resources/site_assets/bootstrap/js/jquery.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/jquery.cookie.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/jquery.md5.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/bootstrap.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/bootstrap-switch.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/dataTables/jquery.dataTables.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/dataTables/dataTables.bootstrap.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/metisMenu/jquery.metisMenu.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/raphael-2.1.2.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/morris/morris-0.5.1.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/plugins/sparkline/jquery.sparkline.min.js"></script>
  <script src="./Resources/site_assets/bootstrap/js/mpos.js"></script> 
</head>
<body>
  <div id="wrapper">
    <jsp:include page="/WEB-INF/view/Layout/header.jsp" flush="false" />
    <jsp:include page="/WEB-INF/view/Layout/navigation.jsp" flush="false" />
    <div id="page-wrapper"><br />
    <c:if test="${Content != null}">
    	<jsp:include page="${Content}" flush="false"/>
    </c:if>
   <%--  <c:if test="${POPUP != null}">
    	<c:set var="className1" value=""/>
    	<c:set var="className2" value="alert alert-info"/>
    	<c:set var="tagId" value="static"/> 
    	<c:forEach var="popup" items="${POPUP }">
    	<c:if test= ${popup.dismiss == "yes" }>
    		<c:set var="className1" value="alert-dismissable"/>
    	</c:if>
    	<c:if test= ${popup.type != "alert alert-info" }>
    		<c:set var="className2" value="${popup.type }"/>
    	</c:if>
    	<c:if test ${popup.id != "static" }>
    		<c:set var="tagId" value="${popup.id }"/>
    	</c:if>
    	<div class="${className1 } ${className2 } " id="${tagId }">
    		<c:if test= ${popup.dismiss == "yes" }>
    			<button id="${tagId }" type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    		</c:if>
    		<c:choose>
    			<c:when test="${popup.type == "alert alert-info" }">
    				<span class="glyphicon glyphicon-info-sign">&nbsp;</span>
    			</c:when>
    			<c:when test="${popup.type == "alert alert-warning" }">
    				<span class="glyphicon glyphicon-info-sign">&nbsp;</span>
    			</c:when>
    			<c:when test="${popup.type == "alert alert-danger" }">
    				<span class="glyphicon glyphicon-remove-circle">&nbsp;</span>
    			</c:when>
    			<c:when test="${popup.type == "alert alert-success" }">
    				<span class="glyphicon glyphicon-ok-circle">&nbsp;</span>
    			</c:when>
    		</c:choose>
    		${popup.content }
    	</div>
    	</c:forEach>
    </c:if> 
    {if $CONTENT != "empty" && $CONTENT != ""}
      {if file_exists($smarty.current_dir|cat:"/$PAGE/$ACTION/$CONTENT")}
        {include file="$PAGE/$ACTION/$CONTENT"}
      {else}
        Missing template for this page
      {/if}
    {/if} --%>
    </div>
    </div>
    <jsp:include page="/WEB-INF/view/Layout/footer.jsp" flush="false" />
  </body>
</html>
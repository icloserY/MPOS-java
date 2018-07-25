<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
       <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <ul class="nav navbar-nav navbar-top-links">
                  <li class="dropdown">
                    <a href="#" class="navbar-brand dropdown-toggle" data-toggle="dropdown"><b class="caret"></b></a>
                    <ul class="dropdown-menu">
                      <li class="h4"><a href="{$PoolURL[1]}"><i class="fa fa-angle-double-right fa-fw"></i> </a></li>
                    </ul>
                  </li>
                </ul> 
            </div>

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    	<%-- 로그인에 따라 Text 변경 <i class="fa fa-user fa-fw"></i> ${ loginTest ? username : Guest } <i class="fa fa-caret-down"></i> --%>
                    	<i class="fa fa-user fa-fw"></i> ${ AUTHENTICATED == 1 ? USERDATA.username : 'Guest' } <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                    	<c:choose>
                    		<c:when test="${AUTHENTICATED == 1 }">
		                        <li><a href="DashBoard.do"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
		                        <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=edit"><i class="fa fa-gear fa-fw"></i> Settings</a>
		                        <li><a href="Myworkers.do"><i class="fa fa-desktop fa-fw"></i> Workers</a>
		                        </li>
		                        <li class="divider"></li>
		                        <li><a href="Logout.do"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
		                        </li>
                        	</c:when>
                        	<c:when test="${AUTHENTICATED != 1 }">
		                        <li><a href="Login.do"><i class="fa fa-sign-in fa-fw"></i> Login</a>
		                        <li><a href="SignUp.do"><i class="fa fa-pencil fa-fw"></i> Sign Up</a>
		                        </li>
                        	</c:when>
                        </c:choose>
                    </ul>
                </li>
            </ul>
        </nav>

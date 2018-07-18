<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
	   <!-- href값 커맨드 핸들러 값으로 변경 -->
       <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li> 
                        <a href="index.jsp"><i class="fa fa-home fa-fw"></i> Home</a>
                    </li>
                    <!-- 로그인시 -->
                    <c:if test="로그인 시 ">
                    <li>
                        <a href="{$smarty.server.SCRIPT_NAME}?page=dashboard"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                    </li>
					<%-- account가 현재 활성화 page일때 
					    <li class="${getPage eq "account" ? "active" : "" }"> --%>
					<li class="${ MyAccounts ? 'active' : '' }">
                        <a href="#"><i class="fa fa-user-md fa-fw"></i> My Account<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=edit"><i class="fa fa-edit fa-fw"></i> Edit Account</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=workers"><i class="fa fa-desktop fa-fw"></i> My Workers</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=transactions"><i class="fa fa-credit-card fa-fw"></i> Transactions</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=earnings"><i class="fa fa-money fa-fw"></i> Earnings</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=notifications"><i class="fa fa-bullhorn fa-fw"></i> Notifications</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=invitations"><i class="fa fa-users fa-fw"></i> Invitations</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=account&action=qrcode"><i class="fa fa-qrcode fa-fw"></i> QR Codes</a></li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                    </c:if>
                    <!-- 로그인 상태이고, admin 계정일 때 -->
                    <c:if test="admin계정 로그인">
					<%-- admin이 현재 활성화 page일때
					      <li class="${getPage eq "admin" ? "active" : "" }"> --%>
					<li class="${false ? 'active' : '' }"> 
                        <a href="#"><i class="fa fa-wrench fa-fw"></i> Admin Panel<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                          <%-- System탭의 항목 중 하나가 현재 활성화 page 일때
                           <li class="${getAction eq dashboard || getAction eq monitoring || getAction eq settings || getAction eq setup ? 'active' : '' }" > --%>
                          <li class="${ false ? 'active' : '' }" > 
                            <a href="#"><i class="fa fa-linux fa-fw"></i> System <span class="fa arrow"></span></a>
                            <ul class="nav nav-third-level">
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=setup"><i class="fa fa-book fa-fw"></i> Setup</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=dashboard"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=monitoring"><i class="fa fa-bell-o fa-fw"></i> Monitoring</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=settings"><i class="fa fa-gears fa-fw"></i> Settings</a></li>
                            </ul>
                          </li>
                          <%-- Funds탭의 항목 중 하나가 현재 활성화 page 일때
                             <li class="${ getAction eq wallet || getAction eq transaction ? 'active' : '' }" > --%>
                          <li class="${false ? 'active' : '' }" >
                            <a href="#"><i class="fa fa-usd fa-fw"></i> Funds <span class="fa arrow"></span></a>
                            <ul class="nav nav-third-level">
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=wallet"><i class="fa fa-money fa-fw"></i> Wallet Info</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=transactions"><i class="fa fa-tasks fa-fw"></i> Transactions</a></li>
                            </ul>
                          </li>
                          <%-- News탭의 항목 중 하나가 현재 활성화 page 일때 
                             <li class="${getAction eq news || getAction eq newsletter ? 'active' : '' }" > --%>
                          <li class="${ false ? 'active' : ''}" >
                            <a href="#"><i class="fa fa-info fa-fw"></i> News <span class="fa arrow"></span></a>
                            <ul class="nav nav-third-level">
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=news"><i class="fa fa-list-alt fa-fw"></i> Site News</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=newsletter"><i class="fa fa-list-alt fa-fw"></i> Newsletter</a></li>
                            </ul>
                          </li>
                          <%-- Users탭의 항목 중 하나가 현재 활성화 page 일때
                              <li class="${ getAction eq user || getAction eq reports || getAction eq registrations || getAction eq invitations || getAction eq poolworkers ? 'active' : '' }" > --%>
                          <li class="${ false ? 'active' : '' }" >
                            <a href="#"><i class="fa fa-users fa-fw"></i> Users <span class="fa arrow"></span></a>
                            <ul class="nav nav-third-level">
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=user"><i class="fa fa-user fa-fw"></i> User Info</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=reports"><i class="fa fa-list-ol fa-fw"></i> Reports</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=registrations"><i class="fa fa-pencil-square-o fa-fw"></i> Registrations</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=invitations"><i class="fa fa-users fa-fw"></i> Invitations</a></li>
                              <li><a href="{$smarty.server.SCRIPT_NAME}?page=admin&action=poolworkers"><i class="fa fa-desktop fa-fw"></i> Pool Workers</a></li>
                            </ul>
                          </li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                    </c:if>
					<%-- statistics가 현재 활성화 page일때 <li class="${ getPage eq statistics ? 'active' : '' }" > --%>
					<li class="${ Statistics ? 'active' : '' }" >
                        <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Statistics<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                          <li><a href="/index.php?page=statistics&action=pool"><i class="fa fa-align-left fa-fw"></i> Pool</a></li>
                          <li><a href="/index.php?page=statistics&action=blocks"><i class="fa fa-th-large fa-fw"></i> Blocks</a></li>
                          <li><a href="/index.php?page=statistics&action=round"><i class="fa fa-refresh fa-fw"></i> Round</a></li>
                          <li><a href="/index.php?page=statistics&action=blockfinder"><i class="fa fa-search fa-fw"></i> Blockfinder</a></li>
                          <li><a href="/index.php?page=statistics&action=uptime"><i class="fa fa-clock-o fa-fw"></i> Uptime</a></li>
                          <li><a href="/index.php?page=statistics&action=graphs"><i class="fa fa-signal fa-fw"></i> Graphs</a></li>
                          <li><a href="/index.php?page=statistics&action=donors"><i class="fa fa-bitbucket fa-fw"></i> Donors</a></li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
					<%-- help가 현재 활성화 page일때 <li class="${getPage eq gettingstarted || getPage eq about ? 'active' : '' }" > --%>
					<li class="${ Help ? 'active' : '' }" >
                        <a href="#"><i class="fa fa-question fa-fw"></i> Help<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=gettingstarted"><i class="fa fa-question fa-fw"></i> Getting Started</a></li>
                          <li><a href="/index.php?page=about&action=pool"><i class="fa fa-info fa-fw"></i> About</a></li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
					<%-- Other가 현재 활성화 page일때
					   <li class="${ getPage eq register || getPage eq login || getPage eq logout || getPage eq tac || getPage eq contactform ? 'active' : '' }" > --%>
					<li class="${ Other ? 'active' : '' }" >
                        <a href="#"><i class="fa fa-tasks fa-fw"></i> Other<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                          <c:choose>
                          	<%-- 로그인시 --%>
                          	<c:when test="${AUTHENTICATED == 1 }">
                          		<li><a href="{$smarty.server.SCRIPT_NAME}?page=logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
                          	</c:when>
                          	<%-- 비로그인시 --%>
                          	<c:when test="${AUTHENTICATED != 1 }">
	                          <li><a href="Login.do"><i class="fa fa-sign-in fa-fw"></i> Login</a></li>
	                          <li><a href="SignUp.do"><i class="fa fa-pencil fa-fw"></i> Sign Up</a></li>
                          	</c:when>
                          </c:choose>
                          <li><a href="/index.php?page=contactform&action="><i class="fa fa-envelope fa-fw"></i> Contact</a></li>
                          <li><a href="{$smarty.server.SCRIPT_NAME}?page=tac"><i class="fa fa-book fa-fw"></i> Terms and Conditions</a></li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                </ul>
                <!-- /#side-menu -->
            </div>
            <!-- /.sidebar-collapse -->
        </nav>
        <!-- /.navbar-static-side -->
<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import = "java.text.DecimalFormat" %>
<%@ attribute name="var" type="java.lang.String" 
			rtexprvalue="false" required="true" %>
<%@ attribute name="diff" type="java.lang.Double"%>			
<%@ attribute name="reward" type="java.lang.Double"%>
<%@ attribute name="hashrate" type="java.lang.Double"%>
<%@ variable name-from-attribute="var" alias="result"
	variable-class="java.lang.Double" scope="AT_END" %>

<%
	DecimalFormat formatter = new DecimalFormat("#0.000"); 
	double value = Double.parseDouble( formatter.format(Math.round(reward / (diff * Math.pow(2, 32) / (hashrate * 1000 ) / 3600 / 24 ) )));
%>
<c:set var="result" value="<%= value %>" />  
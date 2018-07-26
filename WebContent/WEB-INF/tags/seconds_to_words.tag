<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="var" type="java.lang.String" 
			rtexprvalue="false" required="true" %>
<%@ attribute name="timeVal" type="java.lang.Double"%>	
<%@ variable name-from-attribute="var" alias="result"
	variable-class="java.lang.String" scope="AT_END" %>

<%
	String strHours = "";
	String strMinutes = "";
	String strSeconds = "";

	int hours = (int) Math.floor(timeVal / 3600);
	int minutes = (int) Math.floor( (timeVal - (hours * 3600)) / 60 );
	int seconds = (int) (timeVal - (hours * 3600) - (minutes * 60));
	
	strHours = hours < 10 ? "0" + hours : String.valueOf(hours);
	strMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
	strSeconds = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
	
	String text = strHours + " hours " + strMinutes + " minutes " + strSeconds + " seconds";
	if(hours == 0 && minutes == 0 && seconds == 0){
		text = "zero seconds";
	}
%>
<c:set var="result" value="<%= text %>" />  
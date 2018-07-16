<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script>
	var protocol = location.protocal;
	
</script>
<body>
	<p>You account has been locked due to too many failed password or
		PIN attempts. Please follow the URL below to unlock your account.</p>
	<p>
		<a href="${url }/unlock.do?token=${token}">${url }/unlock.do?token=${token}</a>
			
	</p>
	<br />
	<br />
</body>
</html>
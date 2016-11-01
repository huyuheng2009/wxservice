<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="include/html_head.jsp" />
		<script type="text/javascript" src="/statics/js/json2.js"></script>
    </head>
    <body>
		<h1>errorCode:${ret.errorCode}</h1>
		<h2>${ret.message}</h2>
    </body>
</html>

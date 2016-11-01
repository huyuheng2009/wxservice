<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="_head.jsp" />
    </head>
    <body>
		<c:choose>
			<c:when test="${param.random == pageContext.session.id}">
				openid:${param.openid}
			</c:when>
			<c:otherwise>
				参数无效
			</c:otherwise>
		</c:choose>
    </body>
</html>
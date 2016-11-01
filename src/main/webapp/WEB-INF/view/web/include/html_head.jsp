<%@page pageEncoding="UTF-8"%>
<title><%
	Object pageTitle = request.getAttribute("page.title");
	out.print(pageTitle != null ? pageTitle : "安全支付平台");
	%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta content="telephone=no" name="format-detection" />
<link rel="stylesheet" href="/statics/css/index.css"/>
<script type="text/javascript" src="/statics/js/jquery-1.8.3.min.js"></script>
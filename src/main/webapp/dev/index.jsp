<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="_head.jsp" />		
		<script type="text/javascript" src="/statics/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript">
			$(function () {
				$("._ajax").submit(function (evt) {
					var $this = $(this);
					evt.preventDefault();
					$.post($this.attr("action"), $this.serialize(), function (ret) {
						if (ret.errorCode === 0) {
							alert("OK");
						} else {
							alert(ret.message);
						}
						console.log(ret);
					});
				});
			});
		</script>
    </head>
    <body>
		<table style="width: 100%;">
			<tr>
				<td style="text-align: right;">
					<a href="javascrpt:;" onclick="window.location.reload(true);">刷新</a>
				</td>
			</tr>
			<tr>
				<td>
					<a href="/web/oauth/register?uid=dev_test&random=${pageContext.session.id}&scope=snsapi_userinfo">获取openid</a>
				</td>
			</tr>
			<tr>
				<td>
					<%
						{
							java.util.Map map = new java.util.TreeMap();
							map.put("uid", "dev_test2");
							map.put("openid", "ob0EbuOJyfTMEEfAklCOCcm-Qyvs");
							map.put("body", "测试商品");
							map.put("trade_no", Long.toHexString(System.currentTimeMillis()));
							map.put("total_fee", 1);
							map.put("sign", com.yogapay.core.WebUtils.sign(map, ""));
							pageContext.setAttribute("map", map);
						}
					%>
					<form action="/web/pay/" method="POST">
						<c:forEach var="e" items="${map}">
							${e.key}:<input type="text" name="${e.key}" value="${e.value}" /><br />
						</c:forEach>
						<input type="submit" value="提交"/>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<%
						{
							java.util.Map map = new java.util.TreeMap();
							map.put("uid", "dev_test_direct");
							map.put("body", "授权支付测试商品");
							map.put("trade_no", Long.toHexString(System.currentTimeMillis()));
							map.put("total_fee", 1);
							map.put("sign", com.yogapay.core.WebUtils.sign(map, "dev_test_direct"));
							pageContext.setAttribute("map", map);
						}
					%>
					<form action="/web/pay/" method="POST">
						<c:forEach var="e" items="${map}">
							${e.key}:<input type="text" name="${e.key}" value="${e.value}" /><br />
						</c:forEach>
						<input type="submit" value="直接支付"/>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<form class="_ajax" action="/web/pay/query" method="POST">
						<input type="text" name="uid" value="dev_test_direct" />
						<input type="text" name="trade_no" value="123456" />
						<input type="submit" value="查询订单"/>
					</form>
				</td>
			</tr>
		</table>
		<br/>
    </body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="openid" value="oT4zbvpAvvUnqO104JHS4P1p9dp4" />
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="_head.jsp" />
		<c:url var="t" value="/web/jsapi_address-config.js">
			<c:param name="uid" value="JinSongMall_pro" />
			<c:param name="openid" value="${openid}" />
		</c:url>
		<script type="text/javascript" src="${t}"></script>
		<script>
			function onBridgeReady() {
				jsapi_address.editAddress = function () {
					jsapi_address.config.timeStamp = jsapi_address.config.timeStamp + "";
					WeixinJSBridge.invoke('editAddress', jsapi_address.config, function (res) {
						var str = "";
						for (var k in res) {
							str += k + ":" + res[k] + "\n";
						}
						alert(str);
					});
				};
			}
			if (typeof WeixinJSBridge == "undefined") {
				if (document.addEventListener) {
					document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
				} else if (document.attachEvent) {
					document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
					document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				}
			} else {
				onBridgeReady();
			}
		</script>
    </head>
    <body>
		openid:${openid}<br />
		<button onclick="jsapi_address.editAddress();">编辑并获取收货地址</button>
	</body>
</html>
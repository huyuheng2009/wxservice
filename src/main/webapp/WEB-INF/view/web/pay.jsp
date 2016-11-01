<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="include/html_head.jsp" />
		<script type="text/javascript" src="/statics/js/json2.js"></script>
		<script type="text/javascript">
			function onBridgeReady() {
				WeixinJSBridge.invoke("getBrandWCPayRequest", ${paramsJson},
						function (res) {
							$("input[name='wxResponse']").val(JSON.stringify(res));
							$("#myform").submit();
						}
				);
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
		<form id="myform" action="finish">
			<input type="hidden" name="uid" value="${uid}" />
			<input type="hidden" name="out_trade_no" value="${out_trade_no}" />
			<input type="hidden" name="trade_no" value="${trade_no}" />
			<input type="hidden" name="wxResponse" value="" />
		</form>
    </body>
</html>

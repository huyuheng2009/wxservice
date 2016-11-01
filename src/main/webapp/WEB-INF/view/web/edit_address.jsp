<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="include/html_head.jsp" />
		<script type="text/javascript" src="/statics/js/json2.js"></script>
		<script type="text/javascript">
			(function () {
				var ret = ${ret};
				var url = "${jsonpUrl}";
				function onBridgeReady() {
					if (ret.errorCode !== 0) {
						alert(ret.message);
						return;
					}
					var config = ret.value;
					WeixinJSBridge.invoke('editAddress', config, function (res) {
						$.ajax({
							type: "post",
							async: false,
							url: url,
							data: {res: JSON.stringify(res)},
							dataType: "jsonp",
							jsonp: "callback",
							success: function (ret) {
								if (ret.errorCode !== 0) {
									alert(ret.message);
								}
								window.history.back();
							}
						});
					});
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
			})();
		</script>
    </head>
    <body>
	</body>
</html>
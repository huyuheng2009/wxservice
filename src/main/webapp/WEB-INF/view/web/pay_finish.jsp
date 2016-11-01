<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
		<jsp:include page="include/html_head.jsp" />
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript">
			var js_config =${js_config};
			js_config.debug = false;
			js_config.jsApiList = ["closeWindow"];
			wx.config(js_config);
			wx.ready(function () {
				$("._hide").removeClass("_hide");
			});
			function done() {
				var c =${doneConfig};
				var params =${doneConfigParams};
				switch (c.type) {
					case 1:
						var query = "";
						for (var k in params) {
							if (query.length > 0)
								query += "&";
							query += k + "=" + encodeURIComponent(params[k]);
						}
						if (c.url.indexOf("?") > 0) {
							window.location.href = c.url + "&" + query;
						} else {
							window.location.href = c.url + "?" + query;
						}
						break;
					default :
						wx.closeWindow();
				}
			}
		</script>
    </head>
    <body>
		<c:choose>
			<c:when test="${success}">
				<div class="order_ing">
					<div class="order_ing_pic"><img alt="支付成功" src="/statics/images/yes.png" width="265" height="265"></div>
					<div class="order_ing_info">支付成功！</div>
					<a href="javascript:;" class="return _hide" onclick="done();">确定</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="order_ing">
					<div class="order_ing_pic"><img alt="支付失败" src="/statics/images/KO.png" width="265" height="265"></div>
					<div class="order_ing_info">对不起，支付失败！</div>
					<div class="order_ing_span">错误码：${errorCode}</div>
					<a href="javascript:;" class="return _hide" onclick="done();">确定</a>
				</div>
			</c:otherwise>
		</c:choose>
    </body>
</html>

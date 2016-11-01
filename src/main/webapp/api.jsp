<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!
	private static List convertParamArray(Object[][] arr) {
		List list = new ArrayList();
		for (Object[] t : arr) {
			Map map = new HashMap();
			int k = 0;
			map.put("name", t[k++]);
			map.put("description", t[k++]);
			map.put("type", t[k++]);
			map.put("length", t[k++]);
			map.put("required", t[k++]);
			map.put("remark", t[k++]);
			list.add(map);
		}
		return list;
	}

	private static List createCommonsParams() {
		return convertParamArray(new Object[][]{
			{"uid", "系统分配给客户端进行接口调用的ID", "int", "", true, "详细见<a href=\"#dh_2\">参数验证说明</a>"},
			{"verify_code", "此次调用中，参数的MD5", "String", "32", true, "详细见<a href=\"#dh_2\">参数验证说明</a>"},
			{"current_time", "当前调用接口的时间", "long", "", true, "详细见<a href=\"#dh_2\">参数验证说明</a>"}
		});
	}
%>
<%
	List apis = new ArrayList();
	{
		Map map = new HashMap();
		map.put("host_part", "company/create_product");
		map.put("name", "创建产品");
		map.put("description", "");
		List params = createCommonsParams();
		params.addAll(convertParamArray(new Object[][]{
			{"code", "编号", "String", "6", true, null},
			{"name", "名称", "String", "32", true, null},
			{"buy_addr", "电商购买地址", "String", "255", false, null},
			{"explain", "产品说明", "String", "255", false, null},
			{"remark", "备注", "String", "255", false, null}
		}));
		map.put("params", params);
		apis.add(map);
	}
	{
		Map map = new HashMap();
		map.put("host_part", "company/assign_tag");
		map.put("name", "绑定标签");
		map.put("description", "");
		List params = createCommonsParams();
		params.addAll(convertParamArray(new Object[][]{
			{"product_id", "产品ID", "int", null, true, null},
			{"begin_number", "起始流水号", "String", "32", true, "绑定的标签包括起始流水号"},
			{"end_number", "结束流水号", "String", "32", true, "绑定的标签包括结束流水号"}
		}));
		map.put("params", params);
		apis.add(map);
	}
	pageContext.setAttribute("apis", apis);
	//
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>API 使用说明</title>
		<style type="text/css">
			body {font-family:"微软雅黑","宋体",Arial;font-size:12px}
			.x010 {border-bottom: 1px solid #E5E5E5; padding: 0 0 10px;}
			.x020 {border: 1px solid #E4E4E4;color: #0088CC;line-height: 20px;}
			.x020 li {margin: 6px 0 6px 0;}
			.hr {height: 2em;}
			.x030 .h2 {
				background: none repeat scroll 0 0 #EEF4F8;
				color: #353735;
				font-size: 14px;
				font-weight: 800;
				padding: 6px;
				margin: 6px 0 6px 0;
			}
			.x030 .h3 {
				color: #333333;
				font-size: 14px;
				font-weight: 800;
				line-height: 2em;
			}
			.x030 .h4 {
				color: #333333;
				font-size: 12px;
				font-weight: 800;
				line-height: 2em;
				margin-top: 5px;
			}
			.x030 .uri {
				background: none repeat scroll 0 0 #FAFAFA;
				border: 1px dashed #2F6FAB;
				color: #0A8021;
				font-family: "Courier New";
				line-height: 1.5em;
				overflow: hidden;
				padding: 1em;
				word-break: break-all;
				word-wrap: break-word;
			}
			.classtable {
				-moz-border-bottom-colors: none;
				-moz-border-left-colors: none;
				-moz-border-right-colors: none;
				-moz-border-top-colors: none;
				border-color: #C8CCD1;
				border-image: none;
				border-style: solid;
				border-width: 1px 0 0 1px;
				width: 100%;
			}
			.classtable th {
				background: none repeat scroll 0 0 #E5EFFB;
				white-space: nowrap;
			}
			.classtable th, .classtable td {
				-moz-border-bottom-colors: none;
				-moz-border-left-colors: none;
				-moz-border-right-colors: none;
				-moz-border-top-colors: none;
				border-color: #C8CCD1;
				border-image: none;
				border-style: solid;
				border-width: 0 1px 1px 0;
				color: #555555;
				font-size: 12px;
				line-height: 18px;
				padding: 5px 10px;
				text-align: left;
				vertical-align: top;
			}
			.api-code {white-space: pre; border: #C8CCD1 solid 1px;padding: 1em 0 1em 0;}
			tr.attribute > td {border-bottom:1px solid #C8CCD1;padding: 0.5em 0 0.5em; }
		</style>
		<%--
		<script type="text/javascript" src="${_cp}/js/jquery-1.6.3.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				$(".api-code").each(function (i, em) {
					var e = $(em);
					$.get("${_cp}/js/" + e.attr("id") + ".txt", null, function (data) {
						e.html(data);
					}, "text");
				});
			});
		</script>
		--%>
	</head>
	<body>
		<div>
			<h1 class="x010">API 使用说明</h1>
			<div class="x020">
				<ol>
					<li><a href="#dh_1">协议形式</a></li>
					<li><a href="#dh_2">参数验证说明</a></li>
					<li><a href="#dh_3">功能说明</a>
						<ol>
							<c:forEach var="t" varStatus="s" items="${apis}">
								<li><a href="#dh_3_${s.index+1}">${t.name}</a></li>
								</c:forEach>
						</ol>
					</li>
				</ol>
			</div>
			<div class="x030">
				<div class="h2" id="dh_1">1 协议形式</div>
				<div class="content">
					本系统使用 HTTP 协议进行接口调用，调用完毕返回 JSON 格式的数据。
					<br />JSON 的结构为：
					<table style="margin-left: 2em;">
						<tr>
							<td colspan="4">{</td>
						</tr>
						<tr>
							<td>　</td>
							<td style="width: 6em;">errorCode:</td>
							<td style="width: 8em;">(int),</td>
							<td>/* 错误码。0 - 调用成功， 其它 - 失败。具体错误码的含义见具体的接口说明 */</td>
						</tr>
						<tr>
							<td>　</td>
							<td>message:</td>
							<td>(String),</td>
							<td>/* 错误码的说明信息(可能为null) */</td>
						</tr>
						<tr>
							<td>　</td>
							<td>value:</td>
							<td>(Object)</td>
							<td>/* 调用完成，返回的值(可能为null)。类型不固定，详细见具体的接口说明 */</td>
						</tr>
						<tr>
							<td colspan="4">}</td>
						</tr>
					</table>
				</div>
				<div class="hr"></div>
				<div class="h2" id="dh_2">2 参数验证说明</div>
				<div class="content">
					本系统使用请求参数加上盐值进行MD5计算的方式，进行参数验证。步骤如下：
					<ol>
						<li>在使用接口之前，接口开发人员会分配给客户端开发人员一个 uid ，及其对应的盐值</li>
						<li>调用接口时，
							<ol>
								<li>在参数列表中加入参数 uid，参数值为分配得到的uid
									<br />（例如：1）</li>
								<li>在参数列表中加入参数 salt，参数值为分配得到的盐值
									<br />（例如：u48c）</li>
								<li>在参数列表中加入参数 current_time，参数值为当前时间与协调世界时 1970 年 1 月 1 日(零时区)午夜之间的时间差（以毫秒为单位测量）
									<br />（例如：1337144191000，对应的时间是：2012-05-16 12:56:31）</li>
							</ol>
						</li>
						<li>使用自然序对参数名进行排序，然后按顺序取出对应的参数值，合并为一个字符串
							<br />（例如：请求的参数为：uid=1&amp;salt=u48c&amp;current_time=1337144191000&amp;reply=<span style="color:#0088cc">%E8%BF%99%E6%98%AF%E4%B8%80%E4%B8%AA%E6%B5%8B%E8%AF%95</span>
							(注：这是一个是用<span style="color: red;">UTF-8</span>转义过后的参数值，未转义的参数值是：这是一个测试)，按照参数名的自然序排列，应该为 current_time=1337144191000&amp;reply=%E8%BF%99%E6%98%AF%E4%B8%80%E4%B8%AA%E6%B5%8B%E8%AF%95&amp;salt=u48c&amp;uid=1。
							<br />此时，再依次取出转义前的参数值，合并为一个字符串——“<span style="color: #0088cc">1337144191000这是一个测试u48c1</span>”）</li>
						<li>以<span style="color: red;">UTF-8</span>编码计算生成的字符串的MD5(32位，不分大小写)
							<br/>（例如：计算“<span style="color: #0088cc">1337144191000这是一个测试u48c1</span>”的md5，应该为 8ae0e5bda13f0f695118ac0305752a34）</li>
						<li>将此MD5作为参数 verify_code 的参数值，加入参数列表中</li>
						<li>从参数列表中移除参数 salt
							<br />（以如上的例子，最终的请求参数为：uid=1&amp;verify_code=8ae0e5bda13f0f695118ac0305752a34&amp;current_time=1337144191000&amp;reply=%E8%BF%99%E6%98%AF%E4%B8%80%E4%B8%AA%E6%B5%8B%E8%AF%95 ）</li>
					</ol>
					如果验证出错，将返回以下类似数据：
					<table style="margin-left: 2em;">
						<tr>
							<td colspan="4">{</td>
						</tr>
						<tr>
							<td>　</td>
							<td style="width: 6em;">errorCode:</td>
							<td style="width: 8em;">(int),</td>
							<td>错误码含义：
								<br />-101 - 参数 uid、verify_code、current_time 中，有一个或多个的参数值为空
								<br />-102 - uid 不存在
								<br />-103 - 请求时间失效。表示参数current_time的时间与服务器端的时间相差3分钟以上
								<br />-104 - md5不正确。表示服务器端计算的 verify_code 参数值，与客户端传入的参数值不一致
							</td>
						</tr>
						<tr>
							<td>　</td>
							<td>message:</td>
							<td>(String),</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>　</td>
							<td>value:</td>
							<td>(Object)</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4">}</td>
						</tr>
					</table>
					<%--
					<div class="hr"></div>
					JAVA 示例代码：
					<div id="api-code-1" class="api-code"></div>
					--%>
				</div>
				<div class="hr"></div>
				<div class="h2" id="dh_3">3 功能说明</div>
				<c:forEach var="t" varStatus="s" items="${apis}">
					<div class="h3" id="dh_3_${s.index + 1}">3.${s.index + 1} ${t.name}</div>
					<div class="content">${t.description}</div>
					<div class="h4">服务地址</div>
					<div class="uri">http://[host][:port]/api/${t.host_part}</div>
					<div class="h4">参数说明</div>
					<table cellspacing="0" class="classtable">
						<tbody>
							<tr><th style="width: 10%">参数名称</th><th style="width: 10%">类型</th><th style="width: 40%">参数说明</th><th style="width: 10%">是否必选</th><th style="width: 30%">备注</th></tr>
									<c:forEach var="t2" varStatus="s2" items="${t.params}">
								<tr style="background:${s2.index % 2 == 0 ? '#fff':'#F5F9FC'};">
									<td class="code">${t2.name}</td><td>${t2.type}</td><td>${t2.description}</td><td>${t2.required ? '必选':''}</td><td>${t2.remark}</td></tr>
									</c:forEach>
						</tbody>
					</table>
					<div class="h4">返回说明</div>
					<div class="content">
						<table cellspacing="0" style="margin-left: 2em;">
							<tr>
								<td colspan="4">{</td>
							</tr>
							<tr class="attribute">
								<td>　</td>
								<td style="width: 6em;">errorCode:</td>
								<td style="width: 8em;">(int),</td>
								<td>错误码含义：
									<br />00 - 调用成功
									<c:choose>
										<c:when test="${s.index == 0}">
											<br />11 - 商品编号重复
										</c:when>
										<c:when test="${s.index == 1}">
											<br />21 - 指定号段的部分标签已经被绑定
										</c:when>
									</c:choose>
								</td>
							</tr>
							<tr class="attribute">
								<td>　</td>
								<td>message:</td>
								<td>(String),</td>
								<td>&nbsp;</td>
							</tr>
							<tr class="attribute">
								<td>　</td>
								<td>value:</td>
								<td>(Object)</td>
								<td>不同错误码的输出值：
									<c:choose>
										<c:when test="${s.index == 0}">
											<br />00 - 调用成功
											<table style="margin-left: 2em;">
												<tr>
													<td colspan="4">{</td>
												</tr>
												<tr>
													<td>　</td><td>productId:</td><td>(int)</td><td>/*产品ID*/</td>
												</tr>
												<tr>
													<td colspan="4">}</td>
												</tr>
											</table>
										</c:when>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td colspan="4">}</td>
							</tr>
						</table>
					</div>
				</c:forEach>
			</div>
		</div>
    </body>
</html>
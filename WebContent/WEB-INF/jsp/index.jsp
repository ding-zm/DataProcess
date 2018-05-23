<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MRP导入导出</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"	src="${pageContext.request.contextPath}/js/easyui/ext/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.serializejson.min.js"	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.ocupload-1.1.2.js" type="text/javascript"></script>
<script type="text/javascript">
	
	function doSearch() {
		$('#searchWindow').window("open");
	}

	function doExport() {
		location.href="${pageContext.request.contextPath}/export";
	}

	function doImport() {
		$("#button-import").upload({
			name:'myFile',
			action:'${pageContext.request.contextPath}/import"',
			onComplete:function(data){
// 				alert(data);
				 if(data == "success"){
					$.messager.alert('友情提示','恭喜你，导入成功');
				} 
				if(data == "error"){
					$.messager.alert('友情提示','导入失败，请按正确的模板数据导入！');
				}
				$('#grid').datagrid('load');
			}
		});
		
	}
	/* function doDraw() {
		$('#chartWindow').window("open");
	} */

	//工具栏
	var toolbar = [{
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo',
		handler : doImport
	}, {
		id : 'button-export',
		text : '导出',
		iconCls : 'icon-undo',
		handler : doExport
	} ];
	// 定义列
	var columns = [ [ {
		field : 'p_id',
		title : '料件编号',
		width : 120,
		align : 'center',
	}, {
		field : 'p_name',
		title : '品名',
		width : 120,
		align : 'center',
	}, {
		field : 'p_guige',
		title : '规格',
		width : 120,
		align : 'center',
	}, {
		field : 'p_xdata',
		title : '行动日期',
		width : 120,
		align : 'center'
	}, {
		field : 'p_jdate',
		title : '交货日期',
		width : 100,
		align : 'center'
	}, {
		field : 'p_descCount',
		title : '排产数量',
		width : 100,
		align : 'center'
	} ] ];

	$(function() {
		/* daoru fenqu */
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({
			visibility : "visible"
		});
		
		// 管理数据表格
		$('#grid').datagrid({
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList : [ 30, 50, 100 ],
			pagination : false,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/show",
			idField : 'p_id',
			columns : columns,
		});
		
		var pager = $('#grid').datagrid('getPager');    // get the pager of datagrid
		pager.pagination({
			showPageList:false,
		});
		

		// 根据版本查询
		$('#searchWindow').window({
			title : '根据版本查询',
			width : 400,
			modal : true,
			shadow : true,
			closed : true,
			height : 400,
			resizable : false
		});
		$("#btn").click(function() {
			var date = $("#findForm").serializeJSON();
			$("#grid").datagrid('load', date);
			$("#searchWindow").window("close");
		});

		$("#save").click(function() {
			if ($("#saveForm").form("validate")) {
				var date = $("#saveForm").serializeJSON();
				$.post("../../subAreaAction_save.action", date, function(d) {
					if (d.success) {
						$('#addWindow').window("close");
						$("#grid").datagrid("reload");
					}
					$.messager.alert("提示", d.message)
				});
			}
		});

	});

	/* function doDblClickRow() {
		alert("双击表格数据...");
	} */
</script>
</head>
<body class="easyui-layout" style="visibility: hidden;">
	<div region="center" border="false">
		<table id="grid"></table>
	</div>
	
	
	<!-- 根据版本查询 -->
	<div class="easyui-window" title="查询版本窗口" id="searchWindow"
		collapsible="false" minimizable="false" maximizable="false"
		style="top: 20px; left: 200px">
		<div style="overflow: auto; padding: 5px;" border="false">
			<form id="findForm" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>版本号</td>
						<td><input type="text" name="area.province"
							class="easyui-validatebox" required="true" /></td>
					</tr>
				
					<tr>
						<td colspan="2"><a id="btn" href="#"
							class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
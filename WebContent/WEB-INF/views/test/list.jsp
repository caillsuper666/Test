<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>考试列表</title>
	<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="../easyui/css/demo.css"><!-- "WebContent/easyui/js/easyui-lang-zh_CN.js" -->
	<script type="text/javascript" src="../easyui/jquery.min.js"></script>
	<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../easyui/js/validateExtends.js"></script>
	<script type="text/javascript" src="../easyui/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	var subjectList = ${subjectListJson};
	$(function() {	
		var table;
		
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'考试列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"get_list?t="+new Date().getTime(),
	        idField:'id', 
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'id',
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50},
 		        {field:'id',title:'ID',width:50, sortable: true},    
 		        {field:'name',title:'考试名',width:150, sortable: true},
 		       {field:'subjectId',title:'所属科目',width:150, sortable: true,
 		        	formatter:function(value,index,row){
 		        		for(var i=0;i<subjectList.length;i++){
 		        			if(subjectList[i].id == value){
 		        				return subjectList[i].name;
 		        			}
 		        		}
 		        		return value;
 		    	   }
 		        },
 		      { field:'beginTime',title:'考试开始日期',width:150,formatter:function(value,index,row){
 					return format(value);
 				}},
 				{ field:'endTime',title:'考试结束日期',width:150,formatter:function(value,index,row){
 					return format(value);
 				}},
 				{ field:'usableTime',title:'可用时间',width:150,formatter:function(value,index,row){
 					return value + '分钟';
 				}},
 				{ field:'questionNum',title:'试题总数',width:100},
 				{ field:'count',title:'总分',width:100},
 				{ field:'singleQuestionNum',title:'单选题数量',width:100},
 				{ field:'muiltQuestionNum',title:'多选题数量',width:100},
 				{ field:'chargeQuestionNum',title:'判断题数量',width:100},
	 		]], 
	        toolbar: "#toolbar"
	    }); 
	    //设置分页控件 
	    var p = $('#dataList').datagrid('getPager'); 
	    $(p).pagination({ 
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    //设置工具类按钮
	    $("#add").click(function(){
	    	table = $("#addTable");
	    	$("#addDialog").dialog("open");
	    });
	    //修改
	    $("#edit").click(function(){
	    	table = $("#editTable");
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
		    	$("#editDialog").dialog("open");
            }
	    });
	    //删除
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var ids = [];
            	$(selectRows).each(function(i, row){
            		ids[i] = row.id;
            	});
            	$.messager.confirm("消息提醒", "如果班级下存在学生信息则无法删除，须先删除班级下属的学生信息？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "delete",
							data: {ids: ids},
							dataType:'json',
							success: function(data){
								if(data.type == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
									$("#dataList").datagrid("uncheckAll");
								} else{
									$.messager.alert("消息提醒",data.msg,"warning");
									return;
								}
							}
						});
            		}
            	});
            }
	    });
	    
	  	//设置添加窗口
	    $("#addDialog").dialog({
	    	title: "添加考试",
	    	width: 450,
	    	height: 400,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'添加',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							var data = $("#addForm").serialize();
							$.ajax({
								type: "post",
								url: "add",
								data: data,
								dataType:'json',
								success: function(data){
									if(data.type == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#add_name").textbox('setValue', "");
										$("#add_subjectId").combobox('setValue', "");
										//重新刷新页面数据
							  			$('#dataList').datagrid("reload");
										
									} else{
										$.messager.alert("消息提醒",data.msg,"warning");
										return;
									}
								}
							});
						}
					}
				},
			],
			onClose: function(){
				$("#add_name").textbox('setValue', "");
				$("#add_remark").textbox('setValue', "");
			}
	    });
	  	
	  	//编辑班级信息
	  	$("#editDialog").dialog({
	  		title: "修改班级信息",
	    	width: 450,
	    	height: 400,
	    	iconCls: "icon-edit",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'提交',
					plain: true,
					iconCls:'icon-edit',
					handler:function(){
						var validate = $("#editForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							
							var data = $("#editForm").serialize();
							
							$.ajax({
								type: "post",
								url: "edit",
								data: data,
								dataType:'json',
								success: function(data){
									if(data.type == "success"){
										$.messager.alert("消息提醒","修改成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										
										//重新刷新页面数据
							  			$('#dataList').datagrid("reload");
							  			$('#dataList').datagrid("uncheckAll");
										
									} else{
										$.messager.alert("消息提醒",data.msg,"warning");
										return;
									}
								}
							});
						}
					}
				},
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值
				$("#edit-id").val(selectRow.id);
				$("#edit_name").textbox('setValue', selectRow.name);
				$("#edit_subjectId").combobox('setValue', selectRow.subjectId);
				/* $("#edit_usableTime").combobox('setValue', selectRow.usableTime);
				$("#edit_singleQuestionNum").combobox('setValue', selectRow.singleQuestionNum);
				$("#edit_muiltQuestionNum").combobox('setValue', selectRow.muiltQuestionNum);
				$("#edit_chargeQuestionNum").combobox('setValue', selectRow.chargeQuestionNum); */
			}
	    });
	   	
	  	function add0(m){return m<10?'0'+m:m }
		function format(shijianchuo){
		//shijianchuo是整数，否则要parseInt转换
			var time = new Date(shijianchuo);
			var y = time.getFullYear();
			var m = time.getMonth()+1;
			var d = time.getDate();
			var h = time.getHours();
			var mm = time.getMinutes();
			var s = time.getSeconds();
			return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
		}
		
	  	//搜索按钮
	  	$("#search-btn").click(function(){
	  		$('#dataList').datagrid('reload',{
	  			name:$("#search-name").textbox('getValue'),
	  			subjectId:$("#search-subject-id").combobox('getValue')
	  		});
	  	});
	});
	</script>
</head>
<body>
	<!-- 数据列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div>
			<a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
			考试名：<input id="search-name" class="easyui-textbox" />
			所属科目：
			<select id="search-subject-id" class="easyui-combobox" style="width: 150px;">
				<option value="">全部</option>
				<c:forEach items="${ subjectList}" var="subject">
	    			<option value="${subject.id }">${subject.name }</option>
	    		</c:forEach>
			</select>
			<a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
		</div>
	</div>
	
	<!-- 添加窗口 -->
	<div id="addDialog" style="padding: 10px;">  
   		<form id="addForm" method="post">
	    	<table id="addTable" cellpadding="8">
	    		<tr >
	    			<td>考试名:</td>
	    			<td>
	    				<input id="add_name"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="name" data-options="required:true, missingMessage:'请填写考试名'"  />
	    			</td>
	    		</tr>
	    		<tr >
	    			<td>所属年级:</td>
	    			<td>
	    				<select id="add_subjectId"  class="easyui-combobox" style="width: 200px;" name="subjectId" data-options="required:true, missingMessage:'请选择所属学科'">
	    					<c:forEach items="${ subjectList}" var="subject">
	    						<option value="${subject.id }">${subject.name }</option>
	    					</c:forEach>
	    				</select>
	    			</td>
	    		</tr>
	    	<tr >
	    			<td>开始时间:</td>
	    			<td>
	    				<input type="text" id="add_beginTime" name="beginTime" class="easyui-datetimebox" editable="false"></td>
	    		</tr>
	    		<tr >
	    			<td>结束时间:</td>
	    			<td>
	    				<input type="text" id="add_endTime" name="endTime" class="easyui-datetimebox" editable="false"></td>
	    		</tr>
	  <tr>
                <td>考试可用时间:</td>
                <td><input type="text" id="add_usableTime" name="usableTime" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试可用时间'" ></td>
            </tr>
            <tr>
                <td>单选题数量:</td>
                <td><input type="text" placeholder="每个单选题${singleScore}分" id="add_singleQuestionNum" name="singleQuestionNum" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试单选题数量'" ></td>
            </tr>
            <tr>
                 <td>多选题数量:</td>
                <td><input type="text" placeholder="每个多选题${muiltScore}分" id="add_muiltQuestionNum" name="muiltQuestionNum" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试多选题数量'" ></td>
            </tr>
            <tr>
                 <td>判断题数量:</td>
                <td><input type="text" placeholder="每个判断题${chargeScore}分" id="add_chargeQuestionNum" name="chargeQuestionNum" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试判断题数量'" ></td>
            </tr> 
	    	</table>
	    </form>
	</div>
	<!-- 修改窗口 -->
	<div id="editDialog" style="padding: 10px">
    	<form id="editForm" method="post">
    		<input type="hidden" name="id" id="edit-id">
	    	<table id="editTable" border=0 cellpadding="8" >
	    		<tr >
	    			<td>班级名:</td>
	    			<td>
	    				<input id="edit_name"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="name" data-options="required:true, missingMessage:'请填写班级名'"  />
	    			</td>
	    		</tr>
	    		<tr >
	    			<td>所属年级:</td>
	    			<td>
	    				<select id="edit_subjectId"  class="easyui-combobox" style="width: 200px;" name="subjectId" data-options="required:true, missingMessage:'请选择所属年级'">
	    					<c:forEach items="${ subjectList}" var="subject">
	    						<option value="${subject.id }">${subject.name }</option>
	    					</c:forEach>
	    				</select>
	    			</td>
	    		</tr>
	    		<tr >
	    			<td>开始时间:</td>
	    			<td>
	    				<input type="text" id="edit_beginTime" name="beginTime" class="easyui-datetimebox" editable="false"></td>
	    		</tr>
	   <tr>
                <td align="right">考试结束时间:</td>
                <td><input type="text" id="edit_endTime" name="endTime" class="easyui-datetimebox easyui-validatebox" editable="false"></td>
            </tr>
             <%-- 	<tr>
                <td align="right">考试限制时间:</td>
                <td><input type="text" id="edit_usableTime" name="usableTime" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试限制时间'" ></td>
            </tr>
            <tr>
                <td align="right">单选题数量:</td>
                <td><input type="text" placeholder="每个单选题${singleScore}分" id="edit_singleQuestionNum" name="singleQuestionNum" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试单选题数量'" ></td>
            </tr>
            <tr>
                <td align="right">多选题数量:</td>
                <td><input type="text" placeholder="每个多选题${muiltScore}分" id="edit_muiltQuestionNum" name="muiltQuestionNum" class=" easyui-validatebox" data-options="required:true, missingMessage:'请填写考试多选题数量'" ></td>
            </tr>
            <tr>
                <td align="right">判断题数量:</td>
                <td><input type="text" placeholder="每个判断题${chargeScore}分" id="edit_chargeQuestionNum" name="chargeQuestionNum" class="easyui-validatebox" data-options="required:true, missingMessage:'请填写考试判断题数量'" ></td>
            </tr> --%>
        </table>
	    </form>
	</div>
	
	
</body>
</html>
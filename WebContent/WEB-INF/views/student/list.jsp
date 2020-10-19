<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="wu-toolbar">
        <div class="wu-toolbar-button">
            <%@include file="../common/menus.jsp"%>
        </div>
        <div class="wu-toolbar-search">
            <label>学生姓名:</label><input id="search-name" class="wu-text" style="width:100px">
            <label>所属学科:</label>
            <select id="search-subject" class="easyui-combobox" panelHeight="auto" style="width:120px">
            	<option value="-1">全部</option>
            	<c:forEach items="${subjectList }" var="subject">
            		<option value="${subject.id }">${subject.name }</option>
            	</c:forEach>
            </select>
            <a href="#" id="search-btn" class="easyui-linkbutton" ">搜索</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
</div>
<!-- Begin of easyui-dialog -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:420px; padding:10px;">
	<form id="add-form" method="post">
        <table>
            <tr>
                <td align="right">学生账号:</td>
                <td><input type="text" id="add-name" name="name" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写学生账号'" ></td>
            </tr>
            <tr>
                <td align="right">所属学科:</td>
                <td>
                	<select name="subjectId" class="easyui-combobox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择学科'">
		                <c:forEach items="${subjectList }" var="subject">
		                <option value="${subject.id }">${subject.name }</option>
		                </c:forEach>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">密码:</td>
                <td><input type="password" id="add-password" name="password" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写学生密码'" ></td>
            </tr>
            <tr>
                <td align="right">学生姓名:</td>
                <td><input type="text" id="add-realname" name="realname" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">手机号:</td>
                <td><input type="text" id="add-phone" name="phone" class="wu-text" ></td>
            </tr>
        </table>
    </form>
</div>
<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="edit-form" method="post">
        <input type="hidden" name="id" id="edit-id">
        <table>
           <tr>
                <td align="right">学生账号:</td>
                <td><input type="text" id="edit-name" name="name" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写学生账号'" ></td>
            </tr>
            <tr>
                <td align="right">所属学科:</td>
                <td>
                	<select id="edit-subjectId" name="subjectId" class="easyui-combobox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择学科'">
		                <c:forEach items="${subjectList }" var="subject">
		                <option value="${subject.id }">${subject.name }</option>
		                </c:forEach>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">密码:</td>
                <td><input type="password" id="edit-password" name="password" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写学生密码'" ></td>
            </tr>
            <tr>
                <td align="right">学生姓名:</td>
                <td><input type="text" id="edit-realname" name="realname" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">手机号:</td>
                <td><input type="text" id="edit-phone" name="phone" class="wu-text" ></td>
            </tr>
        </table>
    </form>
</div>
<%@include file="../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	
	
	
	/**
	*  添加记录
	*/
	function add(){
		var validate = $("#add-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		var data = $("#add-form").serialize();
		$.ajax({
			url:'add',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert('信息提示','添加成功！','info');
					$("#add-name").val('');
					$("#add-password").val('');
					$("#add-realname").val('');
					$("#add-phone").val('');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
				}else{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	function edit(){
		var validate = $("#edit-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查数据!","warning");
			return;
		}
		var data = $("#edit-form").serialize();
		$.ajax({
			url:'edit',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert('信息提示','修改成功！','info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
				}else{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	/**
	* 删除记录
	*/
	function remove(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#data-datagrid').datagrid('getSelected');
				if(item == null || item.length == 0){
					$.messager.alert('信息提示','请选择要删除的数据！','info');
					return;
				}
				$.ajax({
					url:'delete',
					dataType:'json',
					type:'post',
					data:{id:item.id},
					success:function(data){
						if(data.type == 'success'){
							$.messager.alert('信息提示','删除成功！','info');
							$('#data-datagrid').datagrid('reload');
						}else{
							$.messager.alert('信息提示',data.msg,'warning');
						}
					}
				});
			}	
		});
	}
	
	/*
	编辑
	*/
	function openEdit(){
		//$('#add-form').form('clear');
		var item = $('#data-datagrid').datagrid('getSelected');
		if(item == null || item.length == 0){
			$.messager.alert('信息提示','请选择要编辑的数据！','info');
			return;
		}
		$('#edit-dialog').dialog({
			closed: false,
			modal:true,
            title: "编辑学生信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler:edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            	//$("#add-form input").val('');
            	$("#edit-id").val(item.id);
            	$("#edit-name").val(item.name);
            	$("#edit-password").val(item.password);
            	$("#edit-realname").val(item.realname);
            	$("#edit-phone").val(item.phone);
            	$("#edit-subjectId").combobox('setValue',item.subjectId);
            }
        });
	}
	
	/**
	* Name 打开添加窗口
	*/
	function openAdd(){
		//$('#add-form').form('clear');
		$('#add-dialog').dialog({
			closed: false,
			modal:true,
            title: "添加学生信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            	//$("#add-form input").val('');
            }
        });
	}
	
	//搜索按钮监听
	$("#search-btn").click(function(){
		var option = {name:$("#search-name").val()};
		var subjectId = $("#search-subject").combobox('getValue');
		if(subjectId != -1){
			option.subjectId = subjectId;
		}
		$('#data-datagrid').datagrid('reload',option);
	});

	/** 
	* 载入数据
	*/
	$('#data-datagrid').datagrid({
		url:'list',
		rownumbers:true,
		singleSelect:true,
		pageSize:20,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		idField:'id',
	    treeField:'name',
		fit:true,
		columns:[[
			{ field:'chk',checkbox:true},
			{ field:'name',title:'账号',width:100,sortable:true},
			{ field:'subjectId',title:'所属学科',width:100,formatter:function(value,index,row){
				var subjectList = $("#search-subject").combobox('getData');
				console.log(subjectList);
				for(var i=0;i<subjectList.length;i++){
					if(subjectList[i].value == value)return subjectList[i].text;
				}
				return (value);
			}},
			{ field:'password',title:'密码',width:200},
			{ field:'realname',title:'姓名',width:200},
			{ field:'phone',title:'手机号码',width:200},
		]]
	});
</script>
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
            <label>试题题目:</label><input id="search-title" class="wu-text" style="width:100px">
            <label>试题类型:</label>
            <select id="search-question-type" class="easyui-combobox" panelHeight="auto" style="width:120px">
            	<option value="-1">全部</option>
            	<option value="0">单选</option>
            	<option value="1">多选</option>
            	<option value="2">判断</option>
            </select>
            <label>试题科目:</label>
            <select id="search-subject" class="easyui-combobox" panelHeight="auto" style="width:120px">
            	<option value="-1">全部</option>
            	<c:forEach items="${subjectList}" var="subject">
	            	<option value="${subject.id}">${subject.name}</option>
            	</c:forEach>
            </select>
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
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
                <td align="right">试题题目:</td>
                <td><input type="text" id="add-title" name="title" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写试题题目'" ></td>
            </tr>
            <tr>
                <td align="right">所属科目:</td>
                <td>
                	<select name="subjectId" class="easyui-combobox easyui-validatebox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择考试科目'">
		                <c:forEach items="${subjectList}" var="subject">
			            	<option value="${subject.id}">${subject.name}</option>
		            	</c:forEach>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">所属类型:</td>
                <td>
                	<select name="questionType" class="easyui-combobox easyui-validatebox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择试题类型'">
		                <option value="0">单选</option>
		            	<option value="1">多选</option>
		            	<option value="2">判断</option>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">试题选项A:</td>
                <td><input type="text" id="add-optionA" name="optionA" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写选项A'"></td>
            </tr>
            <tr>
                <td align="right">试题选项B:</td>
                <td><input type="text" id="add-optionB" name="optionB" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写选项B'"></td>
            </tr>
            <tr>
                <td align="right">试题选项C:</td>
                <td><input type="text" id="add-option" name="optionC" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">试题选项D:</td>
                <td><input type="text" id="add-optionD" name="optionD" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">正确答案:</td>
                <td><input type="text" id="add-rightanswer" name="rightanswer" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写正确答案'"></td>
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
                <td align="right">试题题目:</td>
                <td><input type="text" id="edit-title" name="title" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写试题题目'" ></td>
            </tr>
            <tr>
                <td align="right">所属科目:</td>
                <td>
                	<select id="edit-subjectId" name="subjectId" class="easyui-combobox easyui-validatebox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择考试科目'">
		                <c:forEach items="${subjectList}" var="subject">
			            	<option value="${subject.id}">${subject.name}</option>
		            	</c:forEach>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">所属类型:</td>
                <td>
                	<select id="edit-questionType" name="questionType" class="easyui-combobox easyui-validatebox" panelHeight="auto" style="width:268px" data-options="required:true, missingMessage:'请选择试题类型'">
		                <option value="0">单选</option>
		            	<option value="1">多选</option>
		            	<option value="2">判断</option>
		            </select>
                </td>
            </tr>
            <tr>
                <td align="right">试题选项A:</td>
                <td><input type="text" id="edit-optionA" name="optionA" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写选项A'"></td>
            </tr>
            <tr>
                <td align="right">试题选项B:</td>
                <td><input type="text" id="edit-optionB" name="optionB" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写选项B'"></td>
            </tr>
            <tr>
                <td align="right">试题选项C:</td>
                <td><input type="text" id="edit-optionC" name="optionC" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">试题选项D:</td>
                <td><input type="text" id="edit-optionD" name="optionD" class="wu-text" ></td>
            </tr>
            <tr>
                <td align="right">正确答案:</td>
                <td><input type="text" id="edit-rightanswer" name="rightanswer" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写正确答案'"></td>
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
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
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
            title: "编辑试题信息",
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
            	$("#edit-title").val(item.title);
            	$("#edit-optionA").val(item.optionA);
            	$("#edit-optionB").val(item.optionB);
            	$("#edit-optionC").val(item.optionC);
            	$("#edit-optionD").val(item.optionD);
            	$("#edit-rightanswer").val(item.rightanswer);
            	$("#edit-questionType").combobox('setValue',item.questionType);
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
            title: "添加试题信息",
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
            	$("#add-form input").val('');
            }
        });
	}
	
	//搜索按钮监听
	$("#search-btn").click(function(){
		var option = {title:$("#search-title").val()};
		var questionType = $("#search-question-type").combobox('getValue');
		var subjectId = $("#search-subject").combobox('getValue');
		if(questionType != -1){
			option.questionType = questionType;
		}
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
	    nowrap:false,
		fit:true,
		columns:[[
			{ field:'chk',checkbox:true},
			{ field:'title',title:'试题题目',width:300,sortable:true},
			{ field:'subjectId',title:'所属学科',width:100,formatter:function(value,index,row){
				var subjectList = $("#search-subject").combobox("getData");
				for(var i=0;i<subjectList.length;i++){
					if(subjectList[i].value == value)return subjectList[i].text;
				}
				return value;
			}},
			{ field:'score',title:'分值',width:50,sortable:true},
			{ field:'questionType',title:'试题类型',width:100,formatter:function(value,index,row){
				switch(value){
					case 0:return '单选';
					case 1:return '多选';
					case 2:return '判断';
					default:return value;
				}
			}},
			{ field:'optionA',title:'选项A',width:200},
			{ field:'optionB',title:'选项B',width:200},
			{ field:'optionC',title:'选项C',width:200},
			{ field:'optionD',title:'选项D',width:200},
			{ field:'rightanswer',title:'正确答案',width:80},
	
		]]
	});
</script>
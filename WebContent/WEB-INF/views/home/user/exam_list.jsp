<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="../common/user_header.jsp"%>
<body>

	<div class="tm_main">
        <div class="tm_container">
        	<div class="tm_navtitle">
				<h1>我的考试</h1>
            </div>
        </div>
        <div class="tm_container">
			<form action="exam_list" method="get">
			<div class="tm_searchbox">
				考试名称 :
				<input type="text" name="name" class="tm_txts" size="10" maxlength="20" value="${name}" /> &nbsp;
				<button class="tm_btns" type="submit">搜索</button>
			</div>
			<!-- 当前日期 -->
			
        	<table width="100%" cellpadding="10" border="0" class="tm_table_list">
            	<thead>
                	<tr>
                        <th>考试名称</th>
                    	<th>时间设定</th>
                        <th>考试时长</th>
                        <th>试卷类型</th>
                      
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
					<c:if test="${empty testList}">
						<tr>
							<td colspan="7">没有可考的考试！</td>
						</tr>
					</c:if>
					<c:forEach items="${testList }" var = "test">
						<tr id="tr-${test.id }" data-key="${test.id }">
								<td >${test.name }</td>
								<td ><fmt:formatDate value="${test.beginTime }" pattern="yyyy-MM-dd HH:mm:ss"/>---<fmt:formatDate value="${test.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td >${test.usableTime }分钟</td>
								<td >${subject.name }</td>
								<td >
									<c:if test="${nowTime > test.endTime.time}">
										<button type="button" class="tm_btn" >考试已结束</button>
									</c:if>
									<c:if test="${nowTime < test.endTime.time}">
										<button type="button" class="tm_btn tm_btn_primary" onclick="tm.startTest('tr-${test.id }');">开始考试</button>
									</c:if>
								</td>
						</tr>
					</c:forEach>
                </tbody>
            </table>
			</form>
			<table width="100%" cellpadding="10" border="0" class="tm_table_list">
				<tfoot>
					<tr>
						<td>
							<div class="tm_pager_foot">
								<a href="exam_list?page=${page - 1}" class="tm_btns" style="color:white;text-decoration:none;">上一页</a>&nbsp;&nbsp;&nbsp;<font size="5" color="red"><b>${page}</b></font>&nbsp;&nbsp;&nbsp;<a href="exam_list?page=${page + 1}" class="tm_btns" style="color:white;text-decoration:none;">下一页</a>&nbsp; 
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
        </div>
    </div>
<script type="text/javascript">
		window.onload = function(){
			$(".tm_table_list tbody tr").mouseover(function(){
				$(this).attr("style","background:#f5f5f5");
			});
			$(".tm_table_list tbody tr").mouseout(function(){
				$(this).attr("style","background:#ffffff");
			});
		}

		var tm = {
				startTest : function(t){
					if(!tmCheckBrowserSupport()){
						alert("抱歉，您的浏览器不被支持，如需继续使用，请更换为：Chrome、Firefox、360极速浏览器。");
						return false;
					}
					var tr = $("#"+t);
					var tid = tr.attr("data-key");
					var html = [];
					html.push('<div style="margin:20px">');
					html.push('	<p style="line-height:20px">确定进入试卷并开始考试吗？</p>');
					
					html.push('	<table style="margin:0 auto; width:350px" border="0" cellpadding="0" cellspacing="0">');
					html.push('	<tr>');
					html.push('		<td width="80"><img src="../../resources/home/images/paper_pencil.png" width="60" /></td>');
					html.push('		<td><p><b>考试名称</b>：'+tr.find("td").eq(0).text()+'<p>');
					html.push('			<p><b>考试时长</b>：'+tr.find("td").eq(2).text()+'<p>');
					
					html.push('		</td>');
					html.push('	</tr>');
					html.push('</table>');

					html.push('<p style="text-align:center; margin-top:30px">');
					html.push('<button class="confir-test tm_btn tm_btn_primary" type="button" onclick="tm.jointest(\''+tid+'\')">确定</button>');
					html.push('</p>');

					html.push('</div>');

					layer.open({
					  type: 1,
					  title: '开始考试',
					  shadeClose: true,
					  shade: 0.8,
					  area: ['450px', '310px'],
					  content: html.join("")
					}); 

					return false;
				},
				jointest : function(tid){
					$(".confir-test").text('请稍等...');
					$(".confir-test").attr("disabled", true);
					$.ajax({
						type: "POST",
						url: "../test/statr_test",
						dataType: "json",
						data: {"testId":tid},
						success: function(data){
							if(data.type == 'success'){
								top.window.location="../test/testing?testId="+tid;
							}else{
								alert(data.msg);
								//$(".tm_btn_primary").text('提交');
								//return;
								window.location.reload();
							}
						},
						error: function(){
							//$(".tm_btn_primary").text('登录');
							alert('系统忙，请稍后再试');
							window.location.reload();
						}
					});
				}
		};
	</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<script>
    $(function(){

    	<c:if test="${!empty msg}">
        $("span.errorMessage").html("${msg}");
        $("div.loginErrorMessageDiv").show();
        </c:if>

        $("form.loginForm input").keyup(function(){
            $("div.loginErrorMessageDiv").hide();
        });



        var left = window.innerWidth/2+162;
        $("div.loginSmallDiv").css("left",left);
    })
</script>


<div id="loginDiv" style="position: relative">

	<div class="simpleLogo">
		<a href="${contextPath}"><img src="img/site/simpleLogo.png"></a>
	</div>


	<img id="loginBackgroundImg" class="loginBackgroundImg"
		src="img/site/loginBackground.png">

	<form class="loginForm" action="findPassword" method="post">
		<div id="loginSmallDiv" class="loginSmallDiv">
			<div class="loginErrorMessageDiv">
				<div class="alert alert-danger">
					<button type="button" class="close" data-dismiss="alert"
						aria-label="Close"></button>
					<span class="errorMessage"></span>
				</div>
			</div>

			<div class="login_acount_text">忘记密码</div>
			<br>
			<span class="text-danger">请输入您的账号（注册邮箱）</span>
			<br>
			<div class="loginInput ">
				<span class="loginInputIcon "> <span
					class=" glyphicon glyphicon-user"></span>
				</span> <input id="name" name="name" placeholder="邮箱" type="text">
			</div>
			<div style="margin-top: 20px">
				<button class="btn btn-block redButton" type="submit">提交</button>
			</div>
		</div>
	</form>


</div>

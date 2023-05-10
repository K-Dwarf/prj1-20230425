<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%><!-- 태그이용을 위한 링크 -->
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 부트 스트랩 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<!--fontawsome-->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
<!-- 부트 스트랩 -->
<title>Insert title here</title>
</head>
<body>

	<t:navBar>
	</t:navBar>

	<t:alert></t:alert>
	<div class="container-lg">

		<div class="row justify-content-center">
			<div class="col-6 col-md-5 col-lg-30">
				<p>(현재는 아무거나 입력가능)</p>
				<h1>회원가입</h1>
				<form action="signup" method="post">

					<div class="mb-3">
						<label for="inputId" class="form-label">아이디</label>
						 <input id="inputId" type="text" class="form-control" name="id" value="${member.id }">
					</div>

					<div class="mb-3">
						<label for="inputPassword" class="form-label">비밀번호</label> 
						<input id="inputPassword" type="password" class="form-control" name="password" value="${member.password }">
					</div>

					<div class="mb-3">
						<label for="inputPasswordCheck" class="form-label">비밀번호 확인</label>
						 <input id="inputPasswordCheck" type="password" class="form-control" value="${member.password }">
					</div>


					<div id="passwordSuccessText" class="d-none form-text text-primary">
						<i class="fa-solid fa-check"></i> 패스워드가 일치 합니다
					</div>

					<div id="passwordFailText" class="d-none form-text text-danger">
						<!-- d-none = display에서 숨김 -->
						<i class="fa-solid fa-triangle-exclamation"></i> 패스워드가 일치하지 않습니다
					</div>

					<div class="mb-3">
						<label for="" class="form-label">닉네임</label> <input id="inputNickName" type="text" class="form-control" name="nickName" value="${member.nickName }">
					</div>


					<div class="mb-3">
						<label for="" class="form-label">이메일</label> <input id="inputEmail" type="email" class="form-control" name="email" v>
					</div>
					<div class="mb-3">
						<input id="signUpSubmit" class="btn btn-secondary disabled" type="submit" value="회원가입" />
					</div>
				</form>

			</div>


		</div>
	</div>



	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
	<!-- 부트 스트랩 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js" integrity="sha512-pumBsjNRGGqkPzKHndZMaAG+bir374sORyzM3uulLV14lN5LyykqNk8eEeUlUkB3U0M4FApyaHraT65ihJhDpQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<!--jquery-->
	

	<script src="/js/member/signup.js"></script>


</body>
</html>
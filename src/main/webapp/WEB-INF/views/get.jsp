<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %><!-- 태그이용을 위한 링크 -->
  <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>


	<t:navBar></t:navBar>
	
	
	<!-- toast(토스트알람) -->
	<div class="toast-container top-0 start-50 translate-middle-x p-3">
  <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
    <div class="toast-header">
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
    <div class="toast-body">
    
    </div>
  </div>
</div>


	<div class="container-lg">

		<!-- .row.justify-content-center>.col-12.col-md-8.col-lg-6 -->
		<div class="row justify-content-center">
			<div class="col-12 col-md-8 col-lg-6">
				<h1>
				<span id="boardIdText">${board.id }</span>
				
				번 게시물
				</h1>
				
				
					<div>
					
					<h1>
						<span id="likeIcon">
							<c:if test="${board.liked }">
								<i class="fa-solid fa-heart"></i>
							</c:if>
							
							<c:if test="${not board.liked }">
								<i class="fa-regular fa-heart"></i>
							</c:if>
						</span>
						<span id="likeNumber">
							${board.likeCount }
						</span>
					</h1>
				</div>
				
				
				<div>
					<div class="mb-3">
						<label for="" class="form-label">제목</label>
						<input type="text" class="form-control" value="${board.title }" readonly />
					</div>
					
					<!-- 이미지 파일 출력 -->
					<div class="mb-3">
					
					<c:forEach items="${board.fileName}" var="fileName">
						<div>
						<%-- localhost:8080/image/게시물번호/fileName --%>
						
						
						<img class="" alt="" src="${bucketUrl}/${board.id}/${fileName}">
						</div>
					</c:forEach>
					</div>
					
					
					
					
					<div class="mb-3">
						<label for="" class="form-label">본문</label>
						<textarea class="form-control" readonly rows="10">${board.body }</textarea>
					</div>
					
					<div class="mb-3">
						<label for="" class="form-label">작성일시</label>
						<input type="text" readonly class="form-control" value="${board.inserted }" />
					</div>
					
						<sec:authorize access="isAuthenticated()"> <!-- 로그인 상태에서만 보임 -->
						<sec:authentication property="name" var="userId"/> <!-- 로그인한 정보를 userId 로 저장 -->
						<c:if test="${userId eq board.writer }"> <!-- 로그인한 이름과 작성자가 같으면 실행 -->
					<div>

						<a class="btn btn-secondary" href="/modify/${board.id }">수정</a>
						<button id="removeButton" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteConfirmModal">삭제</button>
					</div>
					</c:if>
						</sec:authorize>
				</div>
			</div>
		</div>
	</div>


<sec:authorize access="isAuthenticated()"> <!-- 로그인 했을때만 보이도록 -->
	<div class="d-none">
		<form action="/remove" method="post" id="removeForm">
			<input  type="text" name="id" value="${board.id }" />
		</form>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="exampleModalLabel">삭제 확인</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">삭제 하시겠습니까?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
					<button type="submit" class="btn btn-danger" form="removeForm" >삭제</button>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js" integrity="sha512-pumBsjNRGGqkPzKHndZMaAG+bir374sORyzM3uulLV14lN5LyykqNk8eEeUlUkB3U0M4FApyaHraT65ihJhDpQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="/js/board/like.js"></script>
</body>
</html>











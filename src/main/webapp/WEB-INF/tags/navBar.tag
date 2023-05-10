<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="current"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>




<nav class="navbar navbar-expand-lg bg-body-tertiary mb-5">
	<div class="container-lg">
		<a class="navbar-brand" href="/list">로동 신문</a>
		<img src="제목 없음.png">
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			
			<!--  -->
			
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item">
					<a class="nav-link ${current eq 'list' ? 'active' : '' }" href="/list">목록</a>
				</li>
				
				<sec:authorize access="isAuthenticated()">
				
				<li class="nav-item">
					<a class="nav-link ${current eq 'add' ? 'active' : '' }" href="/add/">글작성</a>
				</li>
				</sec:authorize>
				
				<sec:authorize access="isAnonymous()">
				<li class="nav-item">
					<a class="nav-link ${current eq 'signup' ? 'active' : '' }" href="/member/signup">회원가입</a>
				</li>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('admin')">
				<li class="nav-item">
					<a class="nav-link ${current eq 'memberList' ? 'active' : '' }" href="/member/list">회원목록</a>
				</li>
				</sec:authorize>
				
				
				
			<sec:authorize access="isAuthenticated()">

  <li class="nav-item">
    <a class="nav-link ${current == 'memberInfo' ? 'active' : '' }" href="/member/info?id=<sec:authentication property="name" />">회원정보</a>
  </li>
</sec:authorize> 
				
				
				
				
				
				<sec:authorize access="isAnonymous()">
				<li class="nav-item">
					<a class="nav-link ${current eq 'login' ? 'active' : '' }" href="/member/login">로그인</a>
				</li>
				</sec:authorize>
				
					<sec:authorize access="isAuthenticated()">
				<li class="nav-item">
					<a class="nav-link"  href="/member/logout">로그아웃</a>
				</li>
				</sec:authorize>
			</ul>
			
			
			<!-- 검색 카테고리 드랍  -->
			<div class="dropdown">
  <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
    검색 카테고리
  </button>
  <ul class="dropdown-menu">
    <li><a class="dropdown-item" href="#">제목</a></li>
    <li><a class="dropdown-item" href="#">작성자</a></li>
    <li><a class="dropdown-item" href="#">본문</a></li>
  </ul>
</div>
			
			
			
			
			<!-- navbar 우측 위에 떠있는 검색창에 text를 집어넣고 submit 하면 파라미터 name search로 값을 submit함 -->
			<form action = "/list" class="d-flex" role="search">
		<select class="form-select flex-grow-0" name = "type" id= "" style = "width : 100px;">
			<option value= "all">전체</option>
			<option value= "title" ${param.type eq 'title' ? 'selected' : '' }>제목</option>
			<option value= "body" ${param.type eq 'body' ? 'selected' : '' }>본문</option>
			<option value= "writer" ${param.type eq 'writer' ? 'selected' : '' }>작성자</option>
			</select>
				<input value="${param.search }" name="search" class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
				<button class="btn btn-outline-success" type="submit">
					<i class="fa-solid fa-magnifying-glass"></i>
				</button>
			</form>
		</div>
	</div>
</nav>

<!-- 사용자 정보 -->
<div>
<sec:authentication property="principal"/>
</div>


<div>
	<sec:authorize access="isAuthenticated()" var="loggedIn">
		로그인한 상태 에서만 보임
		</sec:authorize>
</div>

<div>
	<sec:authorize access="${loggedIn }">
		로그인한 상태 2
		</sec:authorize>
</div>



<div>
	<sec:authorize access="isAnonymous()">
		로그아웃 상태 에서 만 보임
		</sec:authorize>

</div>

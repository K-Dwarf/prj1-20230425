<%@ tag language="java" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="jakarta.tags.core" %><!-- 태그이용을 위한 링크 -->


<c:if test="${not empty message }">
	<div class="container-lg">
		<div class="alert alert-warning alert-dismissible fade show" role="alert">
			${message }
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
	</div>
</c:if>
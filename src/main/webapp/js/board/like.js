

const toast = new bootstrap.Toast(document.querySelector("#liveToast"));


$("#likeIcon").click(function() {
	// 게시물 번호 request body에 추가
	const boardId = $("#boardIdText").text().trim();
	// const data = {boardId : boardId};
	const data = {boardId};
	
	$.ajax("/like", {
		method: "post",
		contentType: "application/json",
		data: JSON.stringify(data),
		
		success: function(data) {
			if (data.like) {
				// 꽉찬 하트
				$("#likeIcon").html(`<i class="fa-solid fa-heart"></i>`);
			} else {
				// 빈 하트
				$("#likeIcon").html(`<i class="fa-regular fa-heart"></i>`);
			}
			//좋아요 수 업데이트
			$("#likeNumber").text(data.count);
		},
		error: function(jqXHR){
			//console.log("좋아요 실패");
			//console.log(jqXHR);
			//console.log(jqXHR.responseJSON);
			// $("body").prepend(jqXHR.responseJSON.message);
			$(".toast-body").text(jqXHR.responseJSON.message);
			toast.show();
		},
		//complete:,
	});
});



// 클라이언트는 AJAX를 사용하여 서버에 POST 요청을 전송합니다. 
//요청 URL은 "/like"이며, 요청 데이터는 선택한 게시물의 boardId를 JSON 형식으로 전달합니다.
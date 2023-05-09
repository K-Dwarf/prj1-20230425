package com.example.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Board;
import com.example.demo.mapper.BoardMapper;


@Component
public class CustomSecurityChecker {

	@Autowired
	private BoardMapper mapper;

	// 게시물 작성자와 현재 로그인한 사용자가 같은지 비교하는 메소드
	public boolean checkBoardWriter(Authentication authentication, Integer boardId) {
		Board board = mapper.selectById(boardId);
		
		String username = authentication.getName(); // getName 에서 오류가 생기는 경우는 import 잘봐야함 apach가 아니라 springframework 의 authentication 을 임포트해야함
		String writer = board.getWriter();
		
		return username.equals(writer);
	}

	
	//인자로 받은 Authentication 객체에서 현재 로그인한 사용자의 이름을 가져와서 username 변수에 저장합니다. 그리고 boardId를 사용하여 해당 게시물을 데이터베이스에서 조회하고, 조회한 board 객체에서 작성자의 이름을 가져와 writer 변수에 저장합니다.
	//그리고  username과 writer를 비교하여 두 값이 같으면 true를 반환하고, 다르면 false를 반환합니다


}

package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Board;
import com.example.demo.mapper.BoardMapper;

@Service
public class BoardService {

	@Autowired
	private BoardMapper mapper;
	
	
	public List<Board> listBoard() {
		List<Board> list = mapper.selectALL();
		return list;
	}
	


	public Board getBoard(Integer id) {
	
		return mapper.selectById(id);
	}




	public boolean modify(Board board) {
		int cnt = mapper.update(board); // mapper의 update 실행
						// 반환타입 int 업데이트 성공은 1,실패는 0을 반환함
		if (cnt == 1) {
			return true;
		}else {
			return false;
		}
		
	}

	// mapper에서 데이터 받아와서 삭제함

	public boolean remove(Integer id) {
	    int cnt = mapper.deleteById(id);
	    return cnt > 0;
	}
	
	//-------------------- INSERT INTO -------------------------
	public boolean insert(Board board) {
		int cnt = mapper.insert(board);
		if (cnt == 1) {
			return true;
		}else {
			return false;
		}
	}
}

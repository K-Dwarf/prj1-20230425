package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.mapper.boardLikeMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {

	
	
	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private boardLikeMapper likeMapper;
	
	@Autowired
	private BoardService boardService;
	
	// 암호를 암호화 하는 작업
	// ---------------------------------------------------
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public boolean signup(Member member) {
		String plain = member.getPassword();
		member.setPassword(passwordEncoder.encode(plain));
		
		int cnt = mapper.insert(member);
		return cnt == 1;
	}
	// -----------------------------------------------------
	
	
	
	
	
	
	public List<Member> listMember() {
		
		return mapper.selectALL();
	}


	public Member get(String id) {
	    return mapper.selectId(id);	
}


	public boolean remove(Member member) {
		Member oldMember = mapper.selectId(member.getId());
		int cnt = 0;
		
		if (passwordEncoder.matches(member.getPassword(),oldMember.getPassword())) {
			// 입력한 암호와 기존 암호가 같으면?
			
			
			// 이 회원이 작성한 게시물 row 삭제
			boardService.removeByMemberIdWriter(member.getId());
			
			// 이 회원이 좋아요한 레코드 삭제
			likeMapper.deleteByMemberId(member.getId());
			
			
			// 회원 테이블 삭제
			cnt = mapper.deleteById(member.getId());
		}
		
		return cnt == 1;
	}
	

	public boolean modify(Member member, String oldPassword) {
		
		if(!member.getPassword().isBlank()) {
			String plain = member.getPassword();
			member.setPassword(passwordEncoder.encode(plain));
		}
		
		Member oldMember = mapper.selectId(member.getId());

		int cnt = 0;
		if (passwordEncoder.matches(oldPassword, oldMember.getPassword())) {
			
			cnt = mapper.update(member);
		}
		
		return cnt == 1;
	}






	public Map<String, Object> checkId(String id) {
		Member member = mapper.selectId(id);
		
		return Map.of("available",member == null);
	}





	
	public Map<String, Object> checkNickName(String nickName, Authentication authentication) {
		Member member = mapper.selectnickName(nickName);
		if(authentication != null) {
			Member oldMember = mapper.selectId(nickName);
			return Map.of("available",member == null || oldMember.getNickName().equals(nickName));
		}else {
			return Map.of("available",member == null);
			
		}
	}




	public Map<String, Object> checkEmail(String email, Authentication authentication) {
		Member member = mapper.selectEmail(email);
		
		if (authentication != null) {
			Member oldMember = mapper.selectId(email);
			
			return Map.of("available", member == null || oldMember.getEmail().equals(email));
		} else {
			return Map.of("available", member == null);
			
		}
		
	}






	

}
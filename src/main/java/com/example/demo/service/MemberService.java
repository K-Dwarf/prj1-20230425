package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.mapper.MemberMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {

	@Autowired
	private MemberMapper mapper;
	
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
	

}
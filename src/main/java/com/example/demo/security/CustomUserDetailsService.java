package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Member;
import com.example.demo.mapper.MemberMapper;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private MemberMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = mapper.selectId(username);
		System.out.println(member);
		if(member == null) {
			throw new UsernameNotFoundException(username + "해당 회원 조회 불가");
		}
		UserDetails user = User.builder()
				.username(member.getId())
				.password(member.getPassword())
				.authorities(List.of())
				.build();
				
		
		return user;
	}
	
}

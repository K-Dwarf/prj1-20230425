package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;

@Controller
@RequestMapping("member")
@PreAuthorize("isAuthenticated()") //로그인 상태에서만 접근 허용
public class MemberController {

	@Autowired
	private MemberService service;
	
	
	@GetMapping("login")
	@PreAuthorize("isAnonymous()")
	public void loginForm() {
		
	}
	
	
	
	
	
	@GetMapping("signup")
	@PreAuthorize("isAnonymous()") //로그인 안한 상태에서 접근 가능하도록 설정, CustomConfiguration에서 @EnableMethodSecurity 어노테이션 적용해야함
	public void signupForm() {
		
	}
	
	
	@PostMapping("signup")
	public String signupProcess(Member member, RedirectAttributes rttr) {
	
		try {
			service.signup(member);
			rttr.addFlashAttribute("message", "회원 가입되었습니다.");
			return "redirect:/list";
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("member", member); // 문제 발생해도 해당 칸에 적은 정보 유지
			rttr.addFlashAttribute("message", "회원 가입 중 문제가 발생했습니다.");
			return "redirect:/member/signup";
		}
	}
	
	
	@GetMapping("list")
	public void list(Model model) {
		List<Member> list = service.listMember();
		model.addAttribute("memberList", list); //list 객체를 memberList 라는 이름으로 model객체에 담아서 jsp 로 전달
		
		
	}
	
	
	// 경로: /member/info?id=
	@GetMapping("info")
	@PreAuthorize("isAuthenticated()") //로그인 상태에서만 접근 허용
	public void info(String id,Model model) {
		
		   Member member = service.get(id);
		    model.addAttribute("member", member);
	}
	
	
	
	@PostMapping("remove")
	@PreAuthorize("isAnonymous()")
	public String remove(Member member,RedirectAttributes rttr) {
		
		boolean ok = service.remove(member);
		
		if(ok) {
			rttr.addFlashAttribute("message","탈퇴완료");
			return "redirect:/list";
		}else {
			rttr.addFlashAttribute("message","탈퇴 문제 발생");
			return "redirect:/member/info?id=?" + member.getId();
			
		}
	}

	// 1.
	@GetMapping("modify")
	@PreAuthorize("isAnonymous()")
	public void modifyForm(String id,Model model) {
		
		 Member member = service.get(id);
		 model.addAttribute("member",member);
//		model.addAttribute(service.get(id));
		
		
	}
	
	
	//2.
	@PostMapping("modify")
	public String modifyProcess(Member member, RedirectAttributes rttr,String oldPassword) {
		boolean ok = service.modify(member,oldPassword);

		if (ok) {
			rttr.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
			return "redirect:/member/info?id=" + member.getId();
		} else {
			rttr.addFlashAttribute("message", "회원 정보 수정시 문제가 발생하였습니다.");
			return "redirect:/member/modify?id=" + member.getId();
		}
		
	}
	
}
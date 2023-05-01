package com.example.demo.controller;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Board;
import com.example.demo.service.BoardService;

@Controller
@RequestMapping("/")
public class BoardController {
	
	@Autowired
	private BoardService service;	
		// 경로 : http://localhost:8080?page=3
		// 경로 : http://localhost:8080/list?page=2
	// 게시물 목록
//	@RequestMapping({"/","list"}, method = RequestMethod.GET)
	@GetMapping({"/","list"}) // "/" ,"list" 경로로 들어온 get요청 처리
	public String list(Model model,
					@RequestParam(value="page", defaultValue="1") Integer page,
					@RequestParam(value = "search",defaultValue= "")String search,
					@RequestParam(value = "type", required = false) String type) {

		// 1. request param 수집 / 가공
		// 2. business logic 처리
		
//		List<Board> list = service.listBoard();
		Map<String, Object> list = service.listBoard(page, search,type); 
		// 3. add attribute
		
	//	model.addAttribute("boardList",list.get("boardList"));
		model.addAllAttributes(list);
		
		// 4. forward/ redirect
	
		return "list";
	}
//----------------------- update---------------update----------	
	//------------ list.jsp에서 받아온 {board.id} 를 id 변수에 할당----------------
	@GetMapping("/id/{id}")
	public String board(@PathVariable("id")Integer id, Model model) {
		//1. request param
		//2. business logic
	Board board = 	service.getBoard(id);//id변수를 사용해 해당ID의 커럼들을 가져옴
		//3. add attribute
	model.addAttribute("board",board);
		//4. forward/redirect
		return "get"; //수집한 정보들을 attribute에 넣어서 get으로 포워드
	}
	
	// ----------- modify.jsp로 작업넘겨줌 ------------
	@GetMapping("/modify/{id}")
	public String modifyForm(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("board",service.getBoard(id));
		return "modify";
	}

//	@RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
	@PostMapping("/modify/{id}")
	public String modifyProcess(Board board,RedirectAttributes rda) {
		System.out.println(board);
		
		boolean ok = service.modify(board);
		
		if(ok) {
			// 해당 게시물 보기로 리디렉션
			rda.addAttribute("success","success");//파라미터에 sucess를 담아서 넘김
			
			return"redirect:/id/" + board.getId(); //수정 버튼 후 이동할곳 이경우 ("/id/{id}")
		}else {
			// 수정 form 으로 리디렉션
			rda.addAttribute("fail","fail");
			return "redirect:/modify/" + board.getId(); // 문제있을시 띄울 화면
		}
		
	}
// -----------------update END-------------Delete Start---------update END
	@PostMapping("remove")
	public String remove(Integer id,RedirectAttributes rda) {
		boolean ok = service.remove(id); //
		if(ok) {
			// query string에 추가
			//rda.addAttribute("success","삭제완료");
			
			// 모델에 추가
			rda.addFlashAttribute("message",id +"번 게시물 삭제완료"); // 한번의 요청만 세션에 유지됨
			return "redirect:/list";
		}else {
			rda.addAttribute("fail","삭제실패");
			return "redirect:/id/" + id;
		}
	}
//------------------Delete END------- INSERT INTO Start ---------------------------------------------
	@GetMapping("/add/")
	public String addProcess() {
		System.out.println("넘어옴");
		
		return "add";
		
	}
	
	@PostMapping("/add_controller")
	public String addProcess2(Board board) {
	    boolean ok = service.insert(board); //실질적인 작업은 여기서 끝
	    System.out.println(ok);
	    if (ok) {
	    	
	        return "redirect:/id/" + board.getId();
	     // return "redirect:/list"; 위는 작성된 게시물, 이코드는 리스트로   
	    } else {
	        return "redirect:/list";
	    }
	}
}

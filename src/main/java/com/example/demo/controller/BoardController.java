package com.example.demo.controller;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Board;
import com.example.demo.domain.Like;
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
	public String board(@PathVariable("id")Integer id, Model model,Authentication authentication) {
		//1. request param
		//2. business logic
	Board board = 	service.getBoard(id,authentication);//{id}변수를 사용해 해당ID의 커럼들을 가져옴
		//3. add attribute
	model.addAttribute("board",board);
		//4. forward/redirect
		return "get"; //수집한 정보들을 attribute에 넣어서 get으로 포워드
	}
	
	// ----------- modify.jsp로 작업넘겨줌 ------------
	@GetMapping("/modify/{id}")
	@PreAuthorize(
			"isAuthenticated() and @customSecurityChecker"
			+ ".checkBoardWriter(authentication,#id)") 
	public String modifyForm(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("board",service.getBoard(id));
		return "modify";
	}

//	@RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
	@PostMapping("/modify/{id}")
	
// 메소드 실행전 권한 확인(preAuthorize(로그인되어있는지,customSecurityChecker클래스 불러와서 checkBoardWriter메소드 실행)
	@PreAuthorize(
			"isAuthenticated() and @customSecurityChecker"
			+ ".checkBoardWriter(authentication,#board.id)") 
	// 수정하려는 게시물 id : board.getId
	public String modifyProcess(Board board,
			@RequestParam(value= "files",required = false)MultipartFile[] addFiles,
			@RequestParam(value = "removeFiles",required = false) List<String> removeFileNames, 
			RedirectAttributes rda) throws Exception {
		System.out.println(removeFileNames);
		
		boolean ok = service.modify(board,removeFileNames,addFiles);
		
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
	@PreAuthorize("isAuthenticated() and @customSecurityChecker"
			+ ".checkBoardWriter(authentication,#id)") // 로그인된 사용자와 
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
	@PreAuthorize("isAuthenticated()")//로그인 된 상태에서만 접근 허용 CustomConfiguration 에서 @EnableMethodSecurity 어노테이션 적용해야함
	public String addProcess(Board board,RedirectAttributes rttr) {
		System.out.println("넘어옴");
		
		return "add";
		
	}
	
	@PostMapping("/add_controller")
	@PreAuthorize("isAuthenticated()")
	public String addProcess2(@RequestParam("fileList")MultipartFile[] fileList,
			Board board,
			Authentication authentication)throws Exception {
		board.setWriter(authentication.getName());// 현재 로그인한 사용자의 이름을 board 객체의 writer 필드에 설정
	    boolean ok = service.insert(board, fileList); //실질적인 작업은 여기서 끝
	    System.out.println(ok);
	    if (ok) {
	    	
	        return "redirect:/id/" + board.getId();
	     // return "redirect:/list"; 위는 작성된 게시물, 이코드는 리스트로   
	    } else {
	        return "redirect:/list";
	    }
	}
	
	
	@PostMapping("like")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> like(@RequestBody Like like, Authentication authentication ) {
				
		// 만약 로그인 상태가 아니라면
		if (authentication == null) {
			return ResponseEntity.status(403).body(Map.of("message","로그인이 필요한 서비스입니다"));
		}else {
		
			return  ResponseEntity.ok().body(service.like(like,authentication));
		
	}
	// like 메서드는 Like 객체와 Authentication 객체를 파라미터로 받습니다.
	// Like 객체는 클라이언트에서 전송한 JSON 데이터를 자동으로 매핑하여 받아옵니다.(domain의 Like를 이용해서)
}
}


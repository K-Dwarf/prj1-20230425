package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Board;
import com.example.demo.domain.Like;
import com.example.demo.mapper.BoardMapper;
import com.example.demo.mapper.boardLikeMapper;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Transactional(rollbackFor = Exception.class) // 클래스 메소드에 작성해두면 메소드마다 적용
public class BoardService {

	@Autowired
	private S3Client s3;

	@Autowired
	private BoardMapper mapper;
	


	@Autowired
	private boardLikeMapper likeMapper;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	public List<Board> listBoard() {
		List<Board> list = mapper.selectALL();
		return list;
	}

	public Board getBoard(Integer id,Authentication authentication) {
		Board board = mapper.selectById(id);
		
		// 현재 로그인한 사람이 이 게시물에 좋아요 했는지?
		
		if(authentication != null) {
			Like like =likeMapper.select(id,authentication.getName());
			if(like != null) {
				board.setLiked(true);
				
			}
		}
		return board;
}
	public boolean modify(Board board, List<String> removeFileNames, MultipartFile[] addFiles) throws Exception {

		// FileName 테이블 삭제
		if (removeFileNames != null && !removeFileNames.isEmpty()) {
			for (String fileName : removeFileNames) {
				// s3 에서 객체 삭제
				String objectKey = "board/" + board.getId() + "/" + fileName;
				DeleteObjectRequest dor = DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build();
				s3.deleteObject(dor);

				// 테이블에서 삭제
				mapper.deleteFileNameByBoardIdAndFileName(board.getId(), fileName);
			}
		}

		// 새 파일 추가
		for (MultipartFile newFile : addFiles) {
			if (newFile.getSize() > 0) {

				// 테이블에 파일명 추가
				mapper.insertFiles(board.getId(), newFile.getOriginalFilename());

				// s3에 파일(객체) 업로드
				String objectKey = "board/" + board.getId() + "/" + newFile.getOriginalFilename();
				PutObjectRequest por = PutObjectRequest.builder().acl(ObjectCannedACL.PUBLIC_READ).bucket(bucketName)
						.key(objectKey).build();
				RequestBody rb = RequestBody.fromInputStream(newFile.getInputStream(), newFile.getSize());
				s3.putObject(por, rb);

			}
		}

		// 게시물(Board) 테이블 수정
		int cnt = mapper.update(board); // mapper의 update 실행
		// 반환타입 int 업데이트 성공은 1,실패는 0을 반환함
		if (cnt == 1) {
			return true;
		} else {
			return false;
		}

	}

	// mapper에서 데이터 받아와서 삭제함

	public boolean remove(Integer id) {

		// 좋아요 테이블 지우기
		likeMapper.deleteByBoardId(id);
		
		
		// 파일명 조회
		List<String> fileNames = mapper.selectFileNameByBoardId(id);

		// FileName 테이블의 데이터 지우기
		mapper.deleteFileNameByBoardId(id);

		// s3 bucket의 파일(객체){ 지우기
		for (String fileName : fileNames) {
			String objectKey = "board/" + id + "/" + fileName; // 저장위치
			DeleteObjectRequest dor = DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build(); // dor 객체
																												// 생성
			s3.deleteObject(dor); // s3 bucket의 파일(객체) 지우기
		}

		// 하드디스크의 파일 지우기
		/*
		 * for(String fileName : fileNames) { String path =
		 * "C:\\workstation\\upload\\" + id + "\\" + fileName; File file = new
		 * File(path); if(file.exists()) { file.delete(); }
		 * 
		 * 
		 * }
		 */

		// 게시물 테이블의 데이터 지우기

		int cnt = mapper.deleteById(id);
		return cnt == 1;

	}

	// -------------------- INSERT INTO -------------------------

	public boolean insert(Board board, MultipartFile[] fileList) throws Exception {

		// 게시물 insert
		int cnt = mapper.insert(board);
		for (MultipartFile file : fileList) {
			if (file.getSize() > 0) {

				String objectKey = "board/" + board.getId() + "/" + file.getOriginalFilename();

				PutObjectRequest por = PutObjectRequest.builder().bucket(bucketName).key(objectKey)
						.acl(ObjectCannedACL.PUBLIC_READ).build();
				RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());

				s3.putObject(por, rb);

				// db에 관련 정보 저장(insert)
				mapper.insertFiles(board.getId(), file.getOriginalFilename());
			}
		}

		// 게시물 insert
		if (cnt == 1) {
			return true;
		} else {
			return false;
		}

	}

	public Map<String, Object> listBoard(Integer page, String search, String type) {
		// 페이지당 행의 수
		Integer rowPerPage = 10;

		// 쿼리 LIMIT 절에 사용할 시작 인덱스
		Integer startIndex = (page - 1) * rowPerPage;

		// 페이지네이션이 필요한 정보
		// 전체 레코드 수
		Integer numOfRecords = mapper.countAll(search, type);
		// 마지막 페이지 번호
		Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;
		// 페이지네이션 왼쪽번호
		Integer leftPageNum = page - 3;
		// 1보다 작을 수 없음
		leftPageNum = Math.max(leftPageNum, 1);

		// 페이지네이션 오른쪽번호
		Integer rightPageNum = leftPageNum + 4;
		// 마지막페이지보다 클 수 없음
		rightPageNum = Math.min(rightPageNum, lastPageNumber);

		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("rightPageNum", rightPageNum);
		pageInfo.put("leftPageNum", leftPageNum);
		pageInfo.put("currentPageNum", page);
		pageInfo.put("lastPageNum", lastPageNumber);

		// 게시물 목록
		List<Board> list = mapper.selectAllPaging(startIndex, rowPerPage, search, type);

		return Map.of("pageInfo", pageInfo, "boardList", list);
	}

	public void removeByMemberIdWriter(String writer) {
		List<Integer> idList = mapper.selectIdByWriter(writer);

		for (Integer id : idList) {
			remove(id);
		}

	}

	public Map<String,Object> like(Like like,Authentication authentication){
		System.out.println("service 까지 넘어옴");
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("like", false);
		System.out.println(like);
		System.out.println(authentication);
		
		like.setMemberId(authentication.getName());
		Integer deleteCnt = likeMapper.delete(like);//(삭제한 행의 개수를반환)
		System.out.println(deleteCnt);
		
		if (deleteCnt != 1) { //삭제한 개수가 1이 아닐경우 0이므로 좋아요가 눌러지지 않은 하트를 눌렀으므로 좋아요 활성화
			Integer insertCnt = likeMapper.insert(like);//insertCnt 메소드 실행,BoardLike테이블에 {boardId},{memberId} 추가 
			result.put("like", true);//js에 true값 반환,꽉찬하트로 아이콘 바꿈		
		}
		
		Integer count = likeMapper.countByBoardId(like.getBoardId());
		result.put("count",count);
		return result;
	}
	
	public Board getBoard(Integer id) {
		// TODO Auto-generated method stub
		return getBoard(id, null);
	}
}
			
		
		
		

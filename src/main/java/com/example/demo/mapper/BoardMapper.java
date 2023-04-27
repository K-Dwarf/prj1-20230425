package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.domain.Board;

@Mapper
public interface BoardMapper {
	
	@Select("""
			SELECT
			id,
			title,
			writer,
			inserted
			FROM Board
			ORDER BY id DESC
			
			""")
	List<Board> selectALL();

	@Select(
			"""
			SELECT * 
			FROM Board
			WHERE id = #{id}
			"""
			)
	Board selectById(Integer id);

	
	@Update("""
			UPDATE Board
			SET 
			title=#{title},
			body = #{body},
			writer = #{writer},
			inserted = #{inserted}
			WHERE
			id = #{id}
			""")
	int update(Board board);

	
	@Delete("""
			DELETE FROM Board
			WHERE id = #{id}
			""")
	
	
	int deleteById(Integer id);
//------------------	--------------------------------------- ---------------------
	
	@Insert("""
			INSERT INTO Board(title,body,writer)
			VALUES(
			#{title},
			#{body},
			#{writer}
			
		
			)
			""")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Board board);
	// OPtions  INSERT 쿼리 실행 시 자동으로 생성되는 Primary Key 값을 객체에 매핑해주는 역할을 합니다.
     
	/*useGeneratedKeys 속성은 이 기능을 사용할 것인지 여부를 결정합니다. 
	 true로 설정하면 자동 생성된 키를 사용하게 됩니다. 기본값은 false
	
	keyProperty 속성은 INSERT 수행 후 자동 생성된 Primary Key 값을 객체의 어떤 속성에 매핑할 것인지를 결정합니다.
	 예를 들어 keyProperty = "id"로 설정하면 자동 생성된 Primary Key 값을 id 속성에 매핑
	
	
    @Options(useGeneratedKeys = true, keyProperty = "id")는 Board 객체에 id 속성이 있고,
	INSERT 수행 후 자동으로 생성된 Primary Key 값을 id 속성에 매핑하도록 설정한 것
		*/
	
}

package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
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

	@Select("""
			SELECT 
				b.id,
				b.title,
				b.body,
				b.inserted,
				b.writer,
				f.fileName
			FROM Board b LEFT JOIN Files f	ON b.id = f.boardId
			WHERE b.id = #{id}
			""")
	@ResultMap("boardResultMap")      //BoardMapper.xml의 resultmap의 id값
	Board selectById(Integer id);

	
	@Update("""
			UPDATE Board
			SET 
			title=#{title},
			body = #{body},
			inserted = #{inserted}
			WHERE
			id = #{id}
			""")
	int update(Board board);

	//------------------	--------------------------------------- ---------------------
	@Delete("""
			DELETE FROM Board
			WHERE id = #{id}
			""")
	
	int deleteById(Integer id);
	
	//------------------	--------------------------------------- ---------------------	
	
	@Select("""
			SELECT fileName FROM Files
			
			WHERE boardId = #{id}
			""" )
	List<String> selectFileNameByBoardId(Integer id);
	
		
	
	
	
	
	
	@Delete("""
			DELETE FROM Files
			WHERE boardId = #{id}
			""")
	
	
	int deleteFileNameByBoardId(Integer id);
	
	
	
	@Delete("""
			DELETE FROM FileName
			WHERE 	boardId = #{boardId} 
				AND fileName = #{fileName}
			""")

	void deleteFileNameByBoardIdAndFileName(Integer boardId, String fileName);
	
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
	@Select("""
	        <script>
	        <bind name="pattern" value="'%' + search + '%'" />
	        SELECT
	            b.id,
	            b.title,
	            b.writer,
	            b.inserted,
	            COUNT(f.id) fileCount
	        FROM Board b LEFT JOIN Files f ON b.id = f.boardId
	        
	        <where>
	            <if test="(type eq 'all') or (type eq 'title')">
	               title  LIKE #{pattern}
	            </if>
	            <if test="(type eq 'all') or (type eq 'body')">
	            OR body   LIKE #{pattern}
	            </if>
	            <if test="(type eq 'all') or (type eq 'writer')">
	            OR writer LIKE #{pattern}
	            </if>
	        </where>
	        
	        GROUP BY b.id
	        ORDER BY b.id DESC
	        LIMIT #{startIndex}, #{rowPerPage}
	        </script>
	        """)
	List<Board> selectAllPaging(Integer startIndex, Integer rowPerPage, String search, String type);
	
	

	
	@Select("""
			<script>
				<bind name="pattern" value="'%' + search + '%'" />
			SELECT COUNT(*)
			FROM Board
					<where>
				<if test="(type eq 'all') or (type eq 'title')">
				   title  LIKE #{pattern}
				</if>
				<if test="(type eq 'all') or (type eq 'body')">
				OR body   LIKE #{pattern}
				</if>
				<if test="(type eq 'all') or (type eq 'writer')">
				OR writer LIKE #{pattern}
				</if>
			</where>
			</script>
			""")
	
	Integer countAll(String search, String type);
	
	
	@Select("""
			<script>
				<bind name="some" value="type"/>
			SELECT #{some}
			FROM Board
			WHERE
			#{some}
			</script>
			""")
	String droptable(String type);

	
	@Insert("""
			
		INSERT INTO
		Files(boardId,fileName)
		Values(#{boardId},#{fileName});
			
			""")
	
	
	Integer insertFiles(Integer boardId, String fileName);

	
	
}


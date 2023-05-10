package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.domain.Member;
@Mapper
public interface MemberMapper {

	@Insert("""
			INSERT INTO Member (id, password, nickName, email)
			VALUES (#{id}, #{password}, #{nickName}, #{email})
			""")
	int insert(Member member);
	
	
	
	@Select("""
			SELECT * FROM Member
			ORDER BY inserted DESC
			
			""")
	List<Member>selectALL(); // 멤버 리스트 조회 기준 
	
	
	
	
	@Select("""
			SELECT * FROM Member m LEFT JOIN MemberAuthority ma ON m.id = ma.memberId
			WHERE id = #{id}
			""")
	@ResultMap("memberMap")
	Member selectId(String id);


	
	@Delete("""
			DELETE FROM Member
			WHERE id= #{id}
				
			""")

	Integer deleteById(String id);



	@Update("""
			<script>
			
			UPDATE Member
			SET 
				<if test="password neq null and password neq ''">
				password = #{password},
				</if>
				
			    nickName = #{nickName},
			    email = #{email}
			WHERE
				id = #{id}
			
			</script>
			""")
	Integer update(Member member);
	


}

	




package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableMethodSecurity //특정 메소드에 접근 제한을 걸 수 있는 
public class CustomConfiguration {
	
	// -- aws s3 를 사용하기 위한 코드 ----
	@Value("${aws.accessKeyId}")
	private String accessKeyId;
	@Value("${aws.secretAccessKey}")
	private String secretAccessKey;
	
	@Value("${aws.bucketUrl}")
	private String bucketUrl;
	
	@Autowired
	private ServletContext application;
	
	@PostConstruct
	public void init() {
		application.setAttribute("bucketUrl", bucketUrl);
	}
	
	
	// -- Spring security 를 사용하기 위한코드 ----
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
//		http.formLogin(Customizer.withDefaults());   기본 설정( 안하면 페이지마다 로그인)
		http.formLogin().loginPage("/member/login"); // 해당 로그인 페이지 만들어야함 
		http.logout().logoutUrl("/member/logout");//로그아웃 페이지 변경(새로 만들어야함)
		
		

//		http.authorizeHttpRequests().requestMatchers("/add/").authenticated();    //"/add/" 링크로의 접근은 로그인이 필요토록 함
//		http.authorizeHttpRequests().requestMatchers("/member/signup").anonymous();  // 로그인된 상태에서는 접근 거부
//		http.authorizeHttpRequests().requestMatchers("/**").permitAll();   // 다른 링크는 전부 로그인 없이 접근 가능
		

		// 간단하게 하려면 위코드 사용  Expression-Based Access Control 을 사용해서 여러가지 식을 사용하려면 아래 코드들을 사용 
		//							(https://docs.spring.io/spring-security/reference/servlet/authorization/expression-based.html)
//		http.authorizeHttpRequests()
//		.requestMatchers("/add/")
//		.access(new WebExpressionAuthorizationManager("isAuthenticated()"));		
//	
//		
//		
//		http.authorizeHttpRequests()
//		.requestMatchers("/member/signup")
//		.access(new WebExpressionAuthorizationManager("isAnonymous()"));		
//		
//		
//
//		http.authorizeHttpRequests()
//		.requestMatchers("/**")
//		.access(new WebExpressionAuthorizationManager("permitAll"));		
		
		return http.build();
		
		
		
	}
	

	@Bean
	public S3Client s3client() {
		
		AwsBasicCredentials credentials 
		= AwsBasicCredentials.create(accessKeyId,  secretAccessKey);
		AwsCredentialsProvider provider 
		= StaticCredentialsProvider.create(credentials);
		
		S3Client s3client = S3Client.builder()
				.credentialsProvider(provider)
				.region(Region.AP_NORTHEAST_2)
				.build();
		
		return s3client;
	}
}



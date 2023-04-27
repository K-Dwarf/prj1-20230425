package com.example.demo.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Board {

	private Integer id;
	private String title;
	private String body;
	private String writer;
	private LocalDateTime inserted;
	
}

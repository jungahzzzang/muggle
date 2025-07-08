package com.curty.muggle.musical.entity;

import com.curty.muggle.common.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MUSICAL")
public class Musical extends BaseTime{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "MUSICAL_ID", nullable = false)
	private String mt20id;
	
	@Column(name = "THEATER_ID", nullable = false)
	private String theaterId;
	
	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "POSTER", nullable = false)
	private String poster;
	
	@Column(name = "THEATER_NM", nullable = false)
	private String prfplcnm;
	
	@Column(name = "PERIOD", nullable = false)
	private String prfpd;

}

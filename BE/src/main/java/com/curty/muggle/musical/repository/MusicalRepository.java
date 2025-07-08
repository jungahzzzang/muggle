package com.curty.muggle.musical.repository;

import com.curty.muggle.musical.entity.Musical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicalRepository extends JpaRepository<Musical, Long>{
	
	boolean existsByMt20id(String mt20id);

}

package com.curty.muggle.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.curty.muggle.theater.entity.Theater;

import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long>{

	Optional<Theater> findByTheaterId(Long theaterId);

	@Query("SELECT t.theaterId FROM Theater t WHERE t.theaterNm = :name")
	String findIdByTheaterNm(@Param("name") String name);
}

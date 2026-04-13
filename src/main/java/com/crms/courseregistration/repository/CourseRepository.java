package com.crms.courseregistration.repository;

import com.crms.courseregistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByProgramId(Long programId);
    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.seats = c.seats + 1 WHERE c.id = :courseId")
    void updateSeatsOnUnregister(@Param("courseId") Long courseId);
}

package com.ucm.Api_NotificacaoUCM.repo;

import com.ucm.Api_NotificacaoUCM.model.Student;
import com.ucm.Api_NotificacaoUCM.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(User user);
    Optional<Student>findByUserIdId(long id);
    @Query("SELECT s FROM Student s JOIN s.classes c WHERE c.id = :classId")
    List<Student> findAllByClasses_Id(@Param("classId") Long classId);
}

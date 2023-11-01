package hogwarts.ru.magicschool.repository;

import hogwarts.ru.magicschool.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findByFaculty_Id(Long id);

    @Query(value = "SELECT COUNT(s) FROM Student s")
    Long getCountOfStudents();

    @Query(value = "SELECT AVG(age) from Student")
    Double getAverageAgeOfStudents();

    @Query(value = "SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> getLastStudents(Pageable pageable);

}

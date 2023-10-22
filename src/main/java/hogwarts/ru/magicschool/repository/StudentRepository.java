package hogwarts.ru.magicschool.repository;

import hogwarts.ru.magicschool.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findByFaculty_Id(Long id);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Long getNumberOfAllStudents();

    @Query(value = "SELECT AVG(age) from student", nativeQuery = true)
    Double getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastFiveStudents();

}

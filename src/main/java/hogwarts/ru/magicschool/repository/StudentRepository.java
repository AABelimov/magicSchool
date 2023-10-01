package hogwarts.ru.magicschool.repository;

import hogwarts.ru.magicschool.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

}

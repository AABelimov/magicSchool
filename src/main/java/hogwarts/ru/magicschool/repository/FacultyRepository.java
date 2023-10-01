package hogwarts.ru.magicschool.repository;

import hogwarts.ru.magicschool.Entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColor(String color);

}

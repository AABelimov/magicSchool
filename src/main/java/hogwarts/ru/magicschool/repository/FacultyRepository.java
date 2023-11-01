package hogwarts.ru.magicschool.repository;

import hogwarts.ru.magicschool.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String Color, String Name);

    Faculty findOneByNameAndColor(String name, String color);

}

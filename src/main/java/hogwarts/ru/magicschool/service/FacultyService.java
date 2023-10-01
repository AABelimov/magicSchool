package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.Entity.Faculty;
import hogwarts.ru.magicschool.exception.ElementWithThatIdAlreadyExist;
import hogwarts.ru.magicschool.exception.FacultyNotFoundException;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty createFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            throw new ElementWithThatIdAlreadyExist(faculty.getId(), "Faculty");
        }
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty editFaculty(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            throw new FacultyNotFoundException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findByColor(color);
    }
}

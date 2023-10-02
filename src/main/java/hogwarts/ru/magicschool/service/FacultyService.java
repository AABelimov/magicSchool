package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.dto.FacultyDtoIn;
import hogwarts.ru.magicschool.dto.FacultyDtoOut;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Faculty;
import hogwarts.ru.magicschool.exception.FacultyAlreadyExistException;
import hogwarts.ru.magicschool.exception.FacultyNotFoundException;
import hogwarts.ru.magicschool.mapper.FacultyMapper;
import hogwarts.ru.magicschool.mapper.StudentMapper;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import hogwarts.ru.magicschool.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository, FacultyMapper facultyMapper, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }


    public FacultyDtoOut createFaculty(FacultyDtoIn facultyDtoIn) {
        String name = facultyDtoIn.getName();
        String color = facultyDtoIn.getColor();

        if (facultyRepository.findOneByNameAndColor(name, color) == null) {
            return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDtoIn)));
        }
        throw new FacultyAlreadyExistException(name, color);
    }

    public FacultyDtoOut getFaculty(Long id) {
        return facultyMapper.toDto(facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id)));
    }

    public Collection<FacultyDtoOut> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public FacultyDtoOut editFaculty(Long id, FacultyDtoIn facultyDtoIn) {
        Faculty oldFaculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        oldFaculty.setName(facultyDtoIn.getName());
        oldFaculty.setColor(facultyDtoIn.getColor());
        return facultyMapper.toDto(facultyRepository.save(oldFaculty));
    }

    public FacultyDtoOut removeFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public Collection<FacultyDtoOut> getFacultiesByColorOrName(String colorOrName) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName).stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> getStudents(Long id) {
        return studentRepository.findByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .toList();
    }
}

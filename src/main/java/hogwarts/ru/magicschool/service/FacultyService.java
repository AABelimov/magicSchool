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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;
    private static final Logger LOG = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository, FacultyMapper facultyMapper, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }


    public FacultyDtoOut createFaculty(FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method createFaculty");
        String name = facultyDtoIn.getName();
        String color = facultyDtoIn.getColor();

        if (facultyRepository.findOneByNameAndColor(name, color) == null) {
            return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDtoIn)));
        }
        throw new FacultyAlreadyExistException(name, color);
    }

    public FacultyDtoOut getFaculty(Long id) {
        LOG.info("Was invoked method getFaculty");
        return facultyMapper.toDto(facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id)));
    }

    public Collection<FacultyDtoOut> getAllFaculties() {
        LOG.info("Was invoked method getAllFaculties");
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public FacultyDtoOut editFaculty(Long id, FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method editFaculty");
        Faculty oldFaculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        oldFaculty.setName(facultyDtoIn.getName());
        oldFaculty.setColor(facultyDtoIn.getColor());
        return facultyMapper.toDto(facultyRepository.save(oldFaculty));
    }

    public FacultyDtoOut removeFaculty(Long id) {
        LOG.info("Was invoked method removeFaculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public Collection<FacultyDtoOut> getFacultiesByColorOrName(String colorOrName) {
        LOG.info("Was invoked method getFacultiesByColorOrName");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName).stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> getStudents(Long id) {
        LOG.info("Was invoked method getStudents");
        return studentRepository.findByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse(null);
    }

    public Integer getSum() {
        return Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
    }
}

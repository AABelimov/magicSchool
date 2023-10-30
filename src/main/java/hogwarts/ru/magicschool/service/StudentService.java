package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.dto.FacultyDtoOut;
import hogwarts.ru.magicschool.dto.StudentDtoIn;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.exception.FacultyNotFoundException;
import hogwarts.ru.magicschool.exception.StudentNotFoundException;
import hogwarts.ru.magicschool.mapper.FacultyMapper;
import hogwarts.ru.magicschool.mapper.StudentMapper;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import hogwarts.ru.magicschool.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, StudentMapper studentMapper, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }


    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method createStudent");
        return studentMapper.toDto(studentRepository.save(studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut getStudent(Long id) {
        LOG.info("Was invoked method getStudent");
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public Collection<StudentDtoOut> getAllStudents() {
        LOG.info("Was invoked method getAllStudents");
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public StudentDtoOut editStudent(Long id, StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method editStudent");
        Student oldStudent = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        oldStudent.setName(studentDtoIn.getName());
        oldStudent.setAge(studentDtoIn.getAge());
        oldStudent.setFaculty(facultyRepository.findById(studentDtoIn.getFacultyId())
                .orElseThrow(() -> new FacultyNotFoundException(studentDtoIn.getFacultyId())));
        return studentMapper.toDto(studentRepository.save(oldStudent));
    }

    public StudentDtoOut removeStudent(Long id) {
        LOG.info("Was invoked method removeStudent");
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public Collection<StudentDtoOut> getStudentsByAgeBetween(int minAge, int maxAge) {
        LOG.info("Was invoked method getStudentsByAgeBetween");
        return studentRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> getStudentsByAge(int age) {
        LOG.info("Was invoked method getStudentsByAge");
        return studentRepository.findByAge(age).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public FacultyDtoOut getFacultyByStudentId(Long id) {
        LOG.info("Was invoked method getFacultyByStudentId");
        return facultyMapper.toDto(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)).getFaculty());
    }

    public StudentDtoOut uploadAvatar(Long id, MultipartFile multipartFile) throws IOException {
        LOG.info("Was invoked method uploadAvatar");
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        avatarService.createAvatar(student, multipartFile);
        return studentMapper.toDto(student);
    }

    public Long getCountOfStudents() {
        LOG.info("Was invoked method getCountOfStudents");
        return studentRepository.getCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        LOG.info("Was invoked method getAverageAgeOfStudents");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<StudentDtoOut> getLastStudents(int amount) {
        LOG.info("Was invoked method getLastStudents");
        return studentRepository.getLastStudents(Pageable.ofSize(amount)).stream()
                .map(studentMapper::toDto)
                .toList();
    }
}

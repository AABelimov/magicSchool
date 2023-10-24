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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, StudentMapper studentMapper, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }


    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(studentRepository.save(studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut getStudent(Long id) {
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public Collection<StudentDtoOut> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public StudentDtoOut editStudent(Long id, StudentDtoIn studentDtoIn) {
        Student oldStudent = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        oldStudent.setName(studentDtoIn.getName());
        oldStudent.setAge(studentDtoIn.getAge());
        oldStudent.setFaculty(facultyRepository.findById(studentDtoIn.getFacultyId())
                .orElseThrow(() -> new FacultyNotFoundException(studentDtoIn.getFacultyId())));
        return studentMapper.toDto(studentRepository.save(oldStudent));
    }

    public StudentDtoOut removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public Collection<StudentDtoOut> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> getStudentsByAge(int age) {
        return studentRepository.findByAge(age).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public FacultyDtoOut getFacultyByStudentId(Long id) {
        return facultyMapper.toDto(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)).getFaculty());
    }

    public StudentDtoOut uploadAvatar(Long id, MultipartFile multipartFile) throws IOException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        avatarService.createAvatar(student, multipartFile);

        return studentMapper.toDto(student);
    }

    public Long getNumberOfAllStudents() {
        return studentRepository.getNumberOfAllStudents();
    }

    public Double getAverageAgeOfStudents() {
        return studentRepository.getAverageAgeOfStudents();
    }

    public Collection<StudentDtoOut> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents().stream()
                .map(studentMapper::toDto)
                .toList();
    }
}

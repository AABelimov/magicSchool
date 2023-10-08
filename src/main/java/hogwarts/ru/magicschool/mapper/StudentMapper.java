package hogwarts.ru.magicschool.mapper;

import hogwarts.ru.magicschool.dto.StudentDtoIn;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.exception.FacultyNotFoundException;
import hogwarts.ru.magicschool.repository.AvatarRepository;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;

    public StudentMapper(FacultyMapper facultyMapper, FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
    }


    public StudentDtoOut toDto(Student student) {
        StudentDtoOut studentDtoOut = new StudentDtoOut();
        studentDtoOut.setId(student.getId());
        studentDtoOut.setName(student.getName());
        studentDtoOut.setAge(student.getAge());
        studentDtoOut.setFaculty(facultyMapper.toDto(student.getFaculty()));
        avatarRepository.findByStudent_Id(student.getId()).ifPresent(avatar -> studentDtoOut.setAvatarUrl("/avatar/" + avatar.getId() + "/avatar-from-db"));
        return studentDtoOut;
    }

    public Student toEntity(StudentDtoIn studentDtoIn) {
        Student student = new Student();
        student.setName(studentDtoIn.getName());
        student.setAge(studentDtoIn.getAge());
        student.setFaculty(facultyRepository.findById(studentDtoIn.getFacultyId())
                .orElseThrow(() -> new FacultyNotFoundException(studentDtoIn.getFacultyId())));
        return student;
    }
}

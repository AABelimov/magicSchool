package hogwarts.ru.magicschool.service;

import hogwarts.ru.magicschool.Entity.Student;
import hogwarts.ru.magicschool.exception.ElementWithThatIdAlreadyExist;
import hogwarts.ru.magicschool.exception.StudentNotFoundException;
import hogwarts.ru.magicschool.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            throw new ElementWithThatIdAlreadyExist(student.getId(), "Student");
        }
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student editStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new StudentNotFoundException(student.getId());
        }
        return studentRepository.save(student);
    }

    public Student removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }
}

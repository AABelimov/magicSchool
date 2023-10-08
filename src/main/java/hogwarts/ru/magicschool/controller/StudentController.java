package hogwarts.ru.magicschool.controller;

import hogwarts.ru.magicschool.dto.FacultyDtoOut;
import hogwarts.ru.magicschool.dto.StudentDtoIn;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public StudentDtoOut createStudent(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.createStudent(studentDtoIn);
    }

    @GetMapping("{id}")
    public StudentDtoOut getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public Collection<StudentDtoOut> getStudents(@RequestParam(required = false) Integer minAge,
                                                 @RequestParam(required = false) Integer maxAge,
                                                 @RequestParam(required = false) Integer age) {
        if (minAge != null && maxAge != null) {
            return studentService.getStudentsByAgeBetween(minAge, maxAge);
        } else if (age != null) {
            return studentService.getStudentsByAge(age);
        }
        return studentService.getAllStudents();
    }

    @GetMapping("{id}/faculty")
    public FacultyDtoOut getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }

    @PutMapping("{id}")
    public StudentDtoOut editStudent(@PathVariable Long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.editStudent(id, studentDtoIn);
    }

    @DeleteMapping("{id}")
    public StudentDtoOut removeStudent(@PathVariable Long id) {
        return studentService.removeStudent(id);
    }

    @PatchMapping(value = "{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDtoOut uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) {
        try {
            return studentService.uploadAvatar(id, avatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package hogwarts.ru.magicschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Faculty;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.mapper.AvatarMapper;
import hogwarts.ru.magicschool.mapper.FacultyMapper;
import hogwarts.ru.magicschool.mapper.StudentMapper;
import hogwarts.ru.magicschool.repository.AvatarRepository;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import hogwarts.ru.magicschool.repository.StudentRepository;
import hogwarts.ru.magicschool.service.AvatarService;
import hogwarts.ru.magicschool.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hogwarts.ru.magicschool.constants.FacultyConstantsForTests.*;
import static hogwarts.ru.magicschool.constants.StudentsConstantsForTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWithMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @MockBean
    AvatarRepository avatarRepository;

    @SpyBean
    StudentService studentService;
    @SpyBean
    AvatarService avatarService;

    @SpyBean
    StudentMapper studentMapper;
    @SpyBean
    FacultyMapper facultyMapper;
    @SpyBean
    AvatarMapper avatarMapper;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void testCreateStudent() throws Exception {

        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_1);
        faculty.setName(FACULTY_NAME_1);
        faculty.setColor(FACULTY_COLOR_1);
        student.setId(STUDENT_ID_1);
        student.setName(STUDENT_NAME_1);
        student.setAge(STUDENT_AGE_1);
        student.setFaculty(faculty);

        when(studentRepository.save(eq(student))).thenReturn(student);
        when(facultyRepository.findById(STUDENT_ID_1)).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(objectMapper.writeValueAsString(STUDENT_DTO_IN_1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID_1))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME_1))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE_1))
                .andExpect(jsonPath("$.faculty").value(FACULTY_DTO_OUT_1));


        when(facultyRepository.findById(INCORRECT_FACULTY_ID)).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/student")
                                .content(objectMapper.writeValueAsString(INCORRECT_STUDENT_DTO_IN))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID + " not found!",
                            responseString);
                });
    }

    @Test
    void testGetStudent() throws Exception {

        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_2);
        faculty.setName(FACULTY_NAME_2);
        faculty.setColor(FACULTY_COLOR_2);
        student.setId(STUDENT_ID_2);
        student.setName(STUDENT_NAME_2);
        student.setAge(STUDENT_AGE_2);
        student.setFaculty(faculty);

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student/" + STUDENT_ID_2)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID_2))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME_2))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE_2))
                .andExpect(jsonPath("$.faculty").value(FACULTY_DTO_OUT_2));


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student/" + INCORRECT_STUDENT_ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!",
                            responseString);
                });
    }

    @Test
    void testGetStudents() throws Exception {

        Faculty faculty1 = facultyMapper.toEntity(FACULTY_DTO_IN_1);
        faculty1.setId(FACULTY_ID_1);
        Faculty faculty2 = facultyMapper.toEntity(FACULTY_DTO_IN_2);
        faculty2.setId(FACULTY_ID_2);
        Student student1 = new Student();
        student1.setId(STUDENT_ID_1);
        student1.setName(STUDENT_NAME_1);
        student1.setAge(STUDENT_AGE_1);
        student1.setFaculty(faculty1);
        Student student2 = new Student();
        student2.setId(STUDENT_ID_2);
        student2.setName(STUDENT_NAME_2);
        student2.setAge(STUDENT_AGE_2);
        student2.setFaculty(faculty2);
        Student student3 = new Student();
        student3.setId(STUDENT_ID_3);
        student3.setName(STUDENT_NAME_3);
        student3.setAge(STUDENT_AGE_1);
        student3.setFaculty(faculty1);
        List<Student> students = new ArrayList<>(List.of(
                student1,
                student2,
                student3
        ));
        List<Student> studentsWithOneAge = new ArrayList<>(List.of(
                student1,
                student3
        ));
        List<StudentDtoOut> studentsDto = students.stream().map(e -> studentMapper.toDto(e)).toList();
        List<StudentDtoOut> studentsWithOneAgeDto = studentsWithOneAge.stream().map(e -> studentMapper.toDto(e)).toList();

        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.findByAge(eq(STUDENT_AGE_1))).thenReturn(studentsWithOneAge);
        String expectedAllStudents = objectMapper.writeValueAsString(studentsDto);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedAllStudents, result.getResponse().getContentAsString()));


        String expectedFacultiesWithOneAge = objectMapper.writeValueAsString(studentsWithOneAgeDto);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student?age=" + STUDENT_AGE_1)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedFacultiesWithOneAge, result.getResponse().getContentAsString()));
    }

    @Test
    void testGetFacultyByStudentId() throws Exception {

        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_2);
        faculty.setName(FACULTY_NAME_2);
        faculty.setColor(FACULTY_COLOR_2);
        student.setId(STUDENT_ID_2);
        student.setName(STUDENT_NAME_2);
        student.setAge(STUDENT_AGE_2);
        student.setFaculty(faculty);

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student/" + STUDENT_ID_2 + "/faculty")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID_2))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME_2))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR_2));


        when(studentRepository.findById(eq(INCORRECT_STUDENT_ID))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student/" + INCORRECT_STUDENT_ID + "/faculty")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!",
                            responseString);
                });
    }

    @Test
    void testEditStudent() throws Exception {

        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_1);
        faculty.setName(FACULTY_NAME_1);
        faculty.setColor(FACULTY_COLOR_1);
        student.setId(STUDENT_ID_3);
        student.setName(STUDENT_NAME_3);
        student.setAge(STUDENT_AGE_1);
        student.setFaculty(faculty);
        Faculty newFaculty = new Faculty();
        newFaculty.setId(FACULTY_ID_4);
        newFaculty.setName(FACULTY_NAME_4);
        newFaculty.setColor(FACULTY_COLOR_4);

        when(studentRepository.findById(eq(STUDENT_ID_3))).thenReturn(Optional.of(student));
        when(facultyRepository.findById(FACULTY_ID_4)).thenReturn(Optional.of(newFaculty));
        when(studentRepository.save(eq(student))).thenReturn(student);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/student/" + STUDENT_ID_3)
                                .content(objectMapper.writeValueAsString(STUDENT_DTO_IN_3_EDIT))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID_3))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME_EDIT))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE_EDIT))
                .andExpect(jsonPath("$.faculty").value(FACULTY_DTO_OUT_4));


        when(studentRepository.findById(eq(INCORRECT_STUDENT_ID))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/student/" + INCORRECT_FACULTY_ID)
                                .content(objectMapper.writeValueAsString(STUDENT_DTO_IN_3_EDIT))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!", responseString);
                });
    }

    @Test
    void testRemoveStudent() throws Exception {

        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_2);
        faculty.setName(FACULTY_NAME_2);
        faculty.setColor(FACULTY_COLOR_2);
        student.setId(STUDENT_ID_2);
        student.setName(STUDENT_NAME_2);
        student.setAge(STUDENT_AGE_2);
        student.setFaculty(faculty);

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/student/" + STUDENT_ID_2)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID_2))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME_2))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE_2))
                .andExpect(jsonPath("$.faculty").value(FACULTY_DTO_OUT_2));

        verify(studentRepository, new Times(1)).delete(any());


        when(studentRepository.findById(eq(INCORRECT_STUDENT_ID))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/student/" + INCORRECT_STUDENT_ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!", responseString);
                });
    }
}

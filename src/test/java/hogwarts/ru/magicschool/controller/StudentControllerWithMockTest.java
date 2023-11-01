package hogwarts.ru.magicschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        when(studentRepository.save(eq(STUDENT_1))).thenReturn(STUDENT_1);
        when(facultyRepository.findById(STUDENT_ID_1)).thenReturn(Optional.of(FACULTY_1));

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

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(STUDENT_2));

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

        when(studentRepository.findAll()).thenReturn(ALL_STUDENTS);
        when(studentRepository.findByAge(eq(STUDENT_AGE_1))).thenReturn(STUDENTS_WITH_AGE_1);
        String expectedAllStudents = objectMapper.writeValueAsString(ALL_STUDENT_DTO_OUT);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedAllStudents, result.getResponse().getContentAsString()));


        String expectedFacultiesWithOneAge = objectMapper.writeValueAsString(STUDENT_DTO_OUT_WITH_AGE_1);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student?age=" + STUDENT_AGE_1)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedFacultiesWithOneAge, result.getResponse().getContentAsString()));
    }

    @Test
    void testGetFacultyByStudentId() throws Exception {

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(STUDENT_2));

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

        when(studentRepository.findById(eq(STUDENT_ID_3))).thenReturn(Optional.of(STUDENT_3));
        when(facultyRepository.findById(FACULTY_ID_4)).thenReturn(Optional.of(FACULTY_4));
        when(studentRepository.save(eq(STUDENT_3))).thenReturn(STUDENT_3);

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

        when(studentRepository.findById(eq(STUDENT_ID_2))).thenReturn(Optional.of(STUDENT_2));

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

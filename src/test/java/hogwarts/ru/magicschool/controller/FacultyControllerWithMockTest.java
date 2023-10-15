package hogwarts.ru.magicschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Faculty;
import hogwarts.ru.magicschool.entity.Student;
import hogwarts.ru.magicschool.mapper.FacultyMapper;
import hogwarts.ru.magicschool.mapper.StudentMapper;
import hogwarts.ru.magicschool.repository.AvatarRepository;
import hogwarts.ru.magicschool.repository.FacultyRepository;
import hogwarts.ru.magicschool.repository.StudentRepository;
import hogwarts.ru.magicschool.service.FacultyService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWithMockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FacultyRepository facultyRepository;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    AvatarRepository avatarRepository;

    @SpyBean
    FacultyService facultyService;
    @SpyBean
    FacultyMapper facultyMapper;
    @SpyBean
    StudentMapper studentMapper;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void testCreateFaculty() throws Exception {

        Faculty faculty = facultyMapper.toEntity(FACULTY_DTO_IN_1);
        faculty.setId(FACULTY_ID_1);

        when(facultyRepository.save(eq(faculty))).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/faculty")
                                .content(objectMapper.writeValueAsString(FACULTY_DTO_IN_1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID_1))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME_1))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR_1));


        when(facultyRepository.findOneByNameAndColor(any(), any())).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/faculty")
                                .content(objectMapper.writeValueAsString(FACULTY_DTO_IN_1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Faculty with name = " + FACULTY_NAME_1 + " and color = " + FACULTY_COLOR_1 + " already exist!",
                            responseString);
                });
    }

    @Test
    void testGetFaculty() throws Exception {

        Faculty faculty = facultyMapper.toEntity(FACULTY_DTO_IN_2);
        faculty.setId(FACULTY_ID_2);

        when(facultyRepository.findById(eq(FACULTY_ID_2))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty/" + FACULTY_ID_2)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID_2))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME_2))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR_2));


        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty/" + INCORRECT_FACULTY_ID)
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
    void testGetFaculties() throws Exception {

        Faculty faculty1 = facultyMapper.toEntity(FACULTY_DTO_IN_1);
        faculty1.setId(FACULTY_ID_1);
        Faculty faculty2 = facultyMapper.toEntity(FACULTY_DTO_IN_2);
        faculty2.setId(FACULTY_ID_2);
        Faculty faculty3 = facultyMapper.toEntity(FACULTY_DTO_IN_3);
        faculty3.setId(FACULTY_ID_3);
        List<Faculty> faculties = new ArrayList<>(List.of(
                faculty1,
                faculty2,
                faculty3
        ));
        List<Faculty> facultiesWithOneColor = new ArrayList<>(List.of(
                faculty1,
                faculty3
        ));

        when(facultyRepository.findAll()).thenReturn(faculties);
        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(facultiesWithOneColor);
        String expectedAllFaculties = objectMapper.writeValueAsString(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedAllFaculties, result.getResponse().getContentAsString()));

        String expectedFacultiesByColor = objectMapper.writeValueAsString(facultiesWithOneColor);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty?colorOrName=" + FACULTY_COLOR_1)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedFacultiesByColor, result.getResponse().getContentAsString()));
    }

    @Test
    void testGetStudents() throws Exception {

        Faculty faculty = facultyMapper.toEntity(FACULTY_DTO_IN_1);
        faculty.setId(FACULTY_ID_1);

        when(facultyRepository.findById(FACULTY_ID_1)).thenReturn(Optional.of(faculty));

        Student student1 = studentMapper.toEntity(STUDENT_DTO_IN_1);
        student1.setId(STUDENT_ID_1);
        Student student3 = studentMapper.toEntity(STUDENT_DTO_IN_3);
        student3.setId(STUDENT_ID_3);
        List<Student> students = new ArrayList<>(List.of(
                student1,
                student3
        ));
        List<StudentDtoOut> studentsDto = students.stream().map(e -> studentMapper.toDto(e)).toList();
        String expected = objectMapper.writeValueAsString(studentsDto);

        when(studentRepository.findByFaculty_Id(FACULTY_ID_1)).thenReturn(students);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty/" + FACULTY_ID_1 + "/students")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void testEditFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_3);
        faculty.setName(FACULTY_NAME_3);
        faculty.setColor(FACULTY_COLOR_1);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(eq(faculty))).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/faculty/" + FACULTY_ID_3)
                                .content(objectMapper.writeValueAsString(FACULTY_DTO_IN_3_EDIT))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID_3))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME_EDIT))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR_EDIT));


        when(facultyRepository.findById(eq(INCORRECT_FACULTY_ID))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/faculty/" + INCORRECT_FACULTY_ID)
                                .content(objectMapper.writeValueAsString(FACULTY_DTO_IN_3_EDIT))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID + " not found!", responseString);
                });
    }

    @Test
    void testRemoveFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID_1);
        faculty.setName(FACULTY_NAME_1);
        faculty.setColor(FACULTY_COLOR_1);

        when(facultyRepository.findById(eq(FACULTY_ID_1))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/faculty/" + FACULTY_ID_1)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID_1))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME_1))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR_1));

        verify(facultyRepository, new Times(1)).delete(any());


        when(facultyRepository.findById(eq(INCORRECT_FACULTY_ID))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/faculty/" + INCORRECT_FACULTY_ID)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertNotNull(responseString);
                    assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID + " not found!", responseString);
                });
    }
}

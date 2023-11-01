package hogwarts.ru.magicschool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.ru.magicschool.mapper.AvatarMapper;
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
    @SpyBean
    AvatarMapper avatarMapper;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void testCreateFaculty() throws Exception {

        when(facultyRepository.save(eq(FACULTY_1))).thenReturn(FACULTY_1);

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


        when(facultyRepository.findOneByNameAndColor(any(), any())).thenReturn(FACULTY_1);

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

        when(facultyRepository.findById(eq(FACULTY_ID_2))).thenReturn(Optional.of(FACULTY_2));

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

        when(facultyRepository.findAll()).thenReturn(ALL_FACULTIES);
        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(ALL_FACULTIES_WITH_COLOR_1);
        String expectedAllFaculties = objectMapper.writeValueAsString(ALL_FACULTIES);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedAllFaculties, result.getResponse().getContentAsString()));


        String expectedFacultiesByColor = objectMapper.writeValueAsString(ALL_FACULTIES_WITH_COLOR_1);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty?colorOrName=" + FACULTY_COLOR_1)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expectedFacultiesByColor, result.getResponse().getContentAsString()));
    }

    @Test
    void testGetStudents() throws Exception {

        when(facultyRepository.findById(FACULTY_ID_1)).thenReturn(Optional.of(FACULTY_1));
        when(studentRepository.findByFaculty_Id(FACULTY_ID_1)).thenReturn(STUDENTS_FROM_FACULTY_1);
        String expected = objectMapper.writeValueAsString(STUDENT_DTO_OUT_WITH_FACULTY_1);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty/" + FACULTY_ID_1 + "/students")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @Test
    void testEditFaculty() throws Exception {

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY_3));
        when(facultyRepository.save(eq(FACULTY_3))).thenReturn(FACULTY_3);

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

        when(facultyRepository.findById(eq(FACULTY_ID_1))).thenReturn(Optional.of(FACULTY_1));

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

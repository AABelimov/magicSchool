package hogwarts.ru.magicschool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.ru.magicschool.dto.FacultyDtoOut;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;

import static hogwarts.ru.magicschool.constants.FacultyConstantsForTests.*;
import static hogwarts.ru.magicschool.constants.StudentsConstantsForTests.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FacultyControllerWithRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public void beforeAll() {

        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_1, FacultyDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_2, FacultyDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_3, FacultyDtoOut.class);

        restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_1, StudentDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_2, StudentDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_3, StudentDtoOut.class);
    }


    @Test
    void contextLoads() {

        assertNotNull(facultyController);
    }

    @Test
    void testCreateFaculty() {

        assertEquals(FACULTY_DTO_OUT_4, restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_4, FacultyDtoOut.class));


        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_1, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Faculty with name = " + FACULTY_NAME_1 + " and color = " + FACULTY_COLOR_1 + " already exist!", responseEntity.getBody());
    }

    @Test
    void testGetFaculty() {

        assertEquals(FACULTY_DTO_OUT_1, restTemplate.getForObject("http://localhost:" + port + "/faculty/" + FACULTY_ID_1, FacultyDtoOut.class));

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + INCORRECT_FACULTY_ID, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID + " not found!", responseEntity.getBody());
    }

    @Test
    void testGetFaculties() throws JsonProcessingException {

        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/faculty", Collection.class);
        String expected = objectMapper.writeValueAsString(ALL_FACULTY_DTO_OUT);
        String actual = objectMapper.writeValueAsString(answer);

        assertEquals(expected, actual);


        answer = restTemplate.getForObject("http://localhost:" + port + "/faculty?colorOrName=" + FACULTY_COLOR_1, Collection.class);
        expected = objectMapper.writeValueAsString(ALL_FACULTY_DTO_OUT_WITH_COLOR_1);
        actual = objectMapper.writeValueAsString(answer);

        assertEquals(expected, actual);
    }

    @Test
    void testGetStudents() throws JsonProcessingException {

        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + FACULTY_ID_1 + "/students", Collection.class);
        String expected = objectMapper.writeValueAsString(STUDENT_DTO_OUT_WITH_FACULTY_1);
        String actual = objectMapper.writeValueAsString(answer);

        assertEquals(expected, actual);
    }

    @Test
    void testEditFaculty() throws JsonProcessingException {

        ResponseEntity<FacultyDtoOut> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + FACULTY_ID_3,
                HttpMethod.PUT,
                new HttpEntity<>(FACULTY_DTO_IN_3_EDIT),
                FacultyDtoOut.class
        );

        assertEquals(FACULTY_DTO_OUT_3_EDIT, responseEntity.getBody());


        String expectedListAfterEdit = objectMapper.writeValueAsString(ALL_FACULTY_DTO_OUT_AFTER_EDIT);
        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/faculty", Collection.class);
        String actualListAfterEdit = objectMapper.writeValueAsString(answer);

        assertEquals(expectedListAfterEdit, actualListAfterEdit);


        ResponseEntity<String> badResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + INCORRECT_FACULTY_ID,
                HttpMethod.PUT,
                new HttpEntity<>(FACULTY_DTO_IN_3_EDIT),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, badResponseEntity.getStatusCode());
        assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID +" not found!", badResponseEntity.getBody());
    }

    @Test
    void testRemoveFaculty() throws JsonProcessingException {

        restTemplate.delete("http://localhost:" + port + "/student/2");
        ResponseEntity<FacultyDtoOut> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + FACULTY_ID_2,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                FacultyDtoOut.class
        );

        assertEquals(FACULTY_DTO_OUT_2, responseEntity.getBody());


        String expectedListAfterRemove = objectMapper.writeValueAsString(ALL_FACULTY_DTO_OUT_AFTER_REMOVE);
        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/faculty", Collection.class);
        String actualListAfterRemove = objectMapper.writeValueAsString(answer);

        assertEquals(expectedListAfterRemove, actualListAfterRemove);


        ResponseEntity<String> badResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + INCORRECT_FACULTY_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, badResponseEntity.getStatusCode());
        assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID +" not found!", badResponseEntity.getBody());
    }

}

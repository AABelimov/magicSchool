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

import java.util.Collection;

import static hogwarts.ru.magicschool.constants.FacultyConstantsForTests.*;
import static hogwarts.ru.magicschool.constants.StudentsConstantsForTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentControllerWithRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public void beforeAll() {

        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_1, FacultyDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_2, FacultyDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_3, FacultyDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/faculty", FACULTY_DTO_IN_4, FacultyDtoOut.class);

        restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_1, StudentDtoOut.class);
        restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_2, StudentDtoOut.class);
    }


    @Test
    void contextLoads() {

        assertNotNull(studentController);
    }

    @Test
    void testCreateStudent() {

        assertEquals(STUDENT_DTO_OUT_3, restTemplate.postForObject("http://localhost:" + port + "/student", STUDENT_DTO_IN_3, StudentDtoOut.class));


        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/student", INCORRECT_STUDENT_DTO_IN, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Faculty with id = " + INCORRECT_FACULTY_ID + " not found!", responseEntity.getBody());
    }

    @Test
    void testGetStudent() {

        assertEquals(STUDENT_DTO_OUT_1, restTemplate.getForObject("http://localhost:" + port + "/student/" + STUDENT_ID_1, StudentDtoOut.class));

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/student/" + INCORRECT_STUDENT_ID, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!", responseEntity.getBody());
    }

    @Test
    void testGetStudents() throws JsonProcessingException {

        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/student", Collection.class);
        String expected = objectMapper.writeValueAsString(ALL_STUDENTS);
        String actual = objectMapper.writeValueAsString(answer);

        assertEquals(expected, actual);


        answer = restTemplate.getForObject("http://localhost:" + port + "/student?minAge=" + STUDENT_AGE_1 + "&maxAge=" + STUDENT_AGE_1, Collection.class);
        expected = objectMapper.writeValueAsString(STUDENTS_WITH_AGE_1);
        actual = objectMapper.writeValueAsString(answer);

        assertEquals(expected, actual);
    }

    @Test
    void testGetFacultyByStudentId() {
        assertEquals(FACULTY_DTO_OUT_2, restTemplate.getForObject("http://localhost:" + port + "/student/" + STUDENT_ID_2 + "/faculty", FacultyDtoOut.class));


        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/student/" + INCORRECT_STUDENT_ID + "/faculty", String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student with id = " + INCORRECT_STUDENT_ID + " not found!", responseEntity.getBody());
    }

    @Test
    void testEditStudent() throws JsonProcessingException {

        ResponseEntity<StudentDtoOut> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + STUDENT_ID_3,
                HttpMethod.PUT,
                new HttpEntity<>(STUDENT_DTO_IN_3_EDIT),
                StudentDtoOut.class
        );

        assertEquals(STUDENT_DTO_OUT_3_EDIT, responseEntity.getBody());


        String expectedListAfterEdit = objectMapper.writeValueAsString(ALL_STUDENTS_AFTER_EDIT);
        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/student", Collection.class);
        String actualListAfterEdit = objectMapper.writeValueAsString(answer);

        assertEquals(expectedListAfterEdit, actualListAfterEdit);


        ResponseEntity<String> badResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + INCORRECT_STUDENT_ID,
                HttpMethod.PUT,
                new HttpEntity<>(STUDENT_DTO_IN_3_EDIT),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, badResponseEntity.getStatusCode());
        assertEquals("Student with id = " + INCORRECT_STUDENT_ID +" not found!", badResponseEntity.getBody());
    }

    @Test
    void testRemoveStudent() throws JsonProcessingException {

        ResponseEntity<StudentDtoOut> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + STUDENT_ID_2,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                StudentDtoOut.class
        );

        assertEquals(STUDENT_DTO_OUT_2, responseEntity.getBody());


        String expectedListAfterRemove = objectMapper.writeValueAsString(ALL_STUDENTS_AFTER_REMOVE);
        Collection<?> answer = restTemplate.getForObject("http://localhost:" + port + "/student", Collection.class);
        String actualListAfterRemove = objectMapper.writeValueAsString(answer);

        assertEquals(expectedListAfterRemove, actualListAfterRemove);


        ResponseEntity<String> badResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + INCORRECT_STUDENT_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, badResponseEntity.getStatusCode());
        assertEquals("Student with id = " + INCORRECT_STUDENT_ID +" not found!", badResponseEntity.getBody());
    }
}

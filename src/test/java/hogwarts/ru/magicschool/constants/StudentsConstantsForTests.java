package hogwarts.ru.magicschool.constants;

import hogwarts.ru.magicschool.dto.StudentDtoIn;
import hogwarts.ru.magicschool.dto.StudentDtoOut;
import hogwarts.ru.magicschool.entity.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static hogwarts.ru.magicschool.constants.FacultyConstantsForTests.*;

public class StudentsConstantsForTests {

    public static final Long STUDENT_ID_1 = 1L;
    public static final Long STUDENT_ID_2 = 2L;
    public static final Long STUDENT_ID_3 = 3L;
    public static final Long INCORRECT_STUDENT_ID = 25L;
    public static final String STUDENT_NAME_1 = "TestStudentName1";
    public static final String STUDENT_NAME_2 = "TestStudentName2";
    public static final String STUDENT_NAME_3 = "TestStudentName3";
    public static final String STUDENT_NAME_EDIT = "TestStudentNameEdit";
    public static final int STUDENT_AGE_1 = 12;
    public static final int STUDENT_AGE_2 = 11;
    public static final int STUDENT_AGE_EDIT = 15;
    public static final StudentDtoIn STUDENT_DTO_IN_1 = new StudentDtoIn(STUDENT_NAME_1, STUDENT_AGE_1, FACULTY_ID_1);
    public static final StudentDtoIn STUDENT_DTO_IN_2 = new StudentDtoIn(STUDENT_NAME_2, STUDENT_AGE_2, FACULTY_ID_2);
    public static final StudentDtoIn STUDENT_DTO_IN_3 = new StudentDtoIn(STUDENT_NAME_3, STUDENT_AGE_1, FACULTY_ID_1);
    public static final StudentDtoIn STUDENT_DTO_IN_3_EDIT = new StudentDtoIn(STUDENT_NAME_EDIT, STUDENT_AGE_EDIT, FACULTY_ID_4);
    public static final StudentDtoIn INCORRECT_STUDENT_DTO_IN = new StudentDtoIn(STUDENT_NAME_EDIT, STUDENT_AGE_EDIT, INCORRECT_FACULTY_ID);
    public static final StudentDtoOut STUDENT_DTO_OUT_1 = new StudentDtoOut(STUDENT_ID_1, STUDENT_NAME_1, STUDENT_AGE_1, FACULTY_DTO_OUT_1, null);
    public static final StudentDtoOut STUDENT_DTO_OUT_2 = new StudentDtoOut(STUDENT_ID_2, STUDENT_NAME_2, STUDENT_AGE_2, FACULTY_DTO_OUT_2, null);
    public static final StudentDtoOut STUDENT_DTO_OUT_3 = new StudentDtoOut(STUDENT_ID_3, STUDENT_NAME_3, STUDENT_AGE_1, FACULTY_DTO_OUT_1, null);
    public static final StudentDtoOut STUDENT_DTO_OUT_3_EDIT = new StudentDtoOut(STUDENT_ID_3, STUDENT_NAME_EDIT, STUDENT_AGE_EDIT, FACULTY_DTO_OUT_4, null);
    public static final Student STUDENT_1 = new Student(STUDENT_ID_1, STUDENT_NAME_1, STUDENT_AGE_1, FACULTY_1);
    public static final Student STUDENT_2 = new Student(STUDENT_ID_2, STUDENT_NAME_2, STUDENT_AGE_2, FACULTY_2);
    public static final Student STUDENT_3 = new Student(STUDENT_ID_3, STUDENT_NAME_3, STUDENT_AGE_1, FACULTY_1);
    public static final List<StudentDtoOut> STUDENT_DTO_OUT_WITH_FACULTY_1 = new ArrayList<>(List.of(
            STUDENT_DTO_OUT_1,
            STUDENT_DTO_OUT_3
    ));
    public static final List<StudentDtoOut> ALL_STUDENT_DTO_OUT = new ArrayList<>(List.of(
            STUDENT_DTO_OUT_1,
            STUDENT_DTO_OUT_2,
            STUDENT_DTO_OUT_3
    ));
    public static final Collection<StudentDtoOut> ALL_STUDENT_DTO_OUT_AFTER_EDIT = new ArrayList<>(List.of(
            STUDENT_DTO_OUT_1,
            STUDENT_DTO_OUT_2,
            STUDENT_DTO_OUT_3_EDIT
    ));
    public static final Collection<StudentDtoOut> ALL_STUDENT_DTO_OUT_AFTER_REMOVE = new ArrayList<>(List.of(
            STUDENT_DTO_OUT_1,
            STUDENT_DTO_OUT_3_EDIT
    ));
    public static final List<StudentDtoOut> STUDENT_DTO_OUT_WITH_AGE_1 = new ArrayList<>(List.of(
            STUDENT_DTO_OUT_1,
            STUDENT_DTO_OUT_3
    ));
    public static final List<Student> ALL_STUDENTS = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_2,
            STUDENT_3
    ));
    public static final List<Student> STUDENTS_WITH_AGE_1 = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_3
    ));
    public static final List<Student> STUDENTS_FROM_FACULTY_1 = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_3
    ));
}

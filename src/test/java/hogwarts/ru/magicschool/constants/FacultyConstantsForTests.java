package hogwarts.ru.magicschool.constants;

import hogwarts.ru.magicschool.dto.FacultyDtoIn;
import hogwarts.ru.magicschool.dto.FacultyDtoOut;
import hogwarts.ru.magicschool.entity.Faculty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FacultyConstantsForTests {

    public static final Long FACULTY_ID_1 = 1L;
    public static final Long FACULTY_ID_2 = 2L;
    public static final Long FACULTY_ID_3 = 3L;
    public static final Long FACULTY_ID_4 = 4L;
    public static final Long INCORRECT_FACULTY_ID = 25L;
    public static final String FACULTY_NAME_1 = "TestFacultyName1";
    public static final String FACULTY_NAME_2 = "TestFacultyName2";
    public static final String FACULTY_NAME_3 = "TestFacultyName3";
    public static final String FACULTY_NAME_4 = "TestFacultyName4";
    public static final String FACULTY_NAME_EDIT = "TestFacultyNameEDIT";
    public static final String FACULTY_COLOR_1 = "TestColor1";
    public static final String FACULTY_COLOR_2 = "TestColor2";
    public static final String FACULTY_COLOR_4 = "TestColor4";
    public static final String FACULTY_COLOR_EDIT = "TestColorEDIT";
    public static final FacultyDtoIn FACULTY_DTO_IN_1 = new FacultyDtoIn(FACULTY_NAME_1, FACULTY_COLOR_1);
    public static final FacultyDtoIn FACULTY_DTO_IN_2 = new FacultyDtoIn(FACULTY_NAME_2, FACULTY_COLOR_2);
    public static final FacultyDtoIn FACULTY_DTO_IN_3 = new FacultyDtoIn(FACULTY_NAME_3, FACULTY_COLOR_1);
    public static final FacultyDtoIn FACULTY_DTO_IN_4 = new FacultyDtoIn(FACULTY_NAME_4, FACULTY_COLOR_4);
    public static final FacultyDtoIn FACULTY_DTO_IN_3_EDIT = new FacultyDtoIn(FACULTY_NAME_EDIT, FACULTY_COLOR_EDIT);
    public static final FacultyDtoOut FACULTY_DTO_OUT_1 = new FacultyDtoOut(FACULTY_ID_1, FACULTY_NAME_1, FACULTY_COLOR_1);
    public static final FacultyDtoOut FACULTY_DTO_OUT_2 = new FacultyDtoOut(FACULTY_ID_2, FACULTY_NAME_2, FACULTY_COLOR_2);
    public static final FacultyDtoOut FACULTY_DTO_OUT_3 = new FacultyDtoOut(FACULTY_ID_3, FACULTY_NAME_3, FACULTY_COLOR_1);
    public static final FacultyDtoOut FACULTY_DTO_OUT_4 = new FacultyDtoOut(FACULTY_ID_4, FACULTY_NAME_4, FACULTY_COLOR_4);
    public static final FacultyDtoOut FACULTY_DTO_OUT_3_EDIT = new FacultyDtoOut(FACULTY_ID_3, FACULTY_NAME_EDIT, FACULTY_COLOR_EDIT);
    public static final Faculty FACULTY_1 = new Faculty(FACULTY_ID_1, FACULTY_NAME_1, FACULTY_COLOR_1);
    public static final Faculty FACULTY_2 = new Faculty(FACULTY_ID_2, FACULTY_NAME_2, FACULTY_COLOR_2);
    public static final Faculty FACULTY_3 = new Faculty(FACULTY_ID_3, FACULTY_NAME_3, FACULTY_COLOR_1);
    public static final Faculty FACULTY_4 = new Faculty(FACULTY_ID_4, FACULTY_NAME_4, FACULTY_COLOR_4);
    public static final Collection<FacultyDtoOut> ALL_FACULTY_DTO_OUT = new ArrayList<>(List.of(
            FACULTY_DTO_OUT_1,
            FACULTY_DTO_OUT_2,
            FACULTY_DTO_OUT_3,
            FACULTY_DTO_OUT_4
    ));
    public static final Collection<FacultyDtoOut> ALL_FACULTY_DTO_OUT_WITH_COLOR_1 = new ArrayList<>(List.of(
            FACULTY_DTO_OUT_1,
            FACULTY_DTO_OUT_3
    ));
    public static final Collection<FacultyDtoOut> ALL_FACULTY_DTO_OUT_AFTER_EDIT = new ArrayList<>(List.of(
            FACULTY_DTO_OUT_1,
            FACULTY_DTO_OUT_2,
            FACULTY_DTO_OUT_4,
            FACULTY_DTO_OUT_3_EDIT
    ));
    public static final Collection<FacultyDtoOut> ALL_FACULTY_DTO_OUT_AFTER_REMOVE = new ArrayList<>(List.of(
            FACULTY_DTO_OUT_1,
            FACULTY_DTO_OUT_4,
            FACULTY_DTO_OUT_3_EDIT
    ));
    public static final List<Faculty> ALL_FACULTIES = new ArrayList<>(List.of(
            FACULTY_1,
            FACULTY_2,
            FACULTY_3,
            FACULTY_4
    ));
    public static final List<Faculty> ALL_FACULTIES_WITH_COLOR_1 = new ArrayList<>(List.of(
            FACULTY_1,
            FACULTY_3
    ));
}

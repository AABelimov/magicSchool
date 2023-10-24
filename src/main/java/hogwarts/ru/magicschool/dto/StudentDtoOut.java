package hogwarts.ru.magicschool.dto;

import java.util.Objects;

public class StudentDtoOut {

    private Long id;
    private String name;
    private int age;
    private FacultyDtoOut faculty;
    private AvatarDto avatar;

    public StudentDtoOut() {

    }

    public StudentDtoOut(Long id, String name, int age, FacultyDtoOut faculty, AvatarDto avatar) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FacultyDtoOut getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDtoOut faculty) {
        this.faculty = faculty;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDto avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDtoOut that = (StudentDtoOut) o;
        return age == that.age && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(faculty, that.faculty) && Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, faculty, avatar);
    }
}

package hogwarts.ru.magicschool.exception;

public class FacultyAlreadyExistException extends RuntimeException {

    private final String name;
    private final String color;

    public FacultyAlreadyExistException(String name, String color) {
        this.name = name;
        this.color = color;
    }


    @Override
    public String getMessage() {
        return "Faculty with name = " + name + " and color = " + color + " already exist!";
    }
}

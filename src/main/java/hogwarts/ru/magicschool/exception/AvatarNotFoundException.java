package hogwarts.ru.magicschool.exception;

public class AvatarNotFoundException extends RuntimeException {

    private final long id;

    public AvatarNotFoundException(long id) {
        this.id = id;
    }


    @Override
    public String getMessage() {
        return "Avatar with id = " + id + " not found!";
    }
}

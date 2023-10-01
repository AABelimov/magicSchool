package hogwarts.ru.magicschool.exception;

public class ElementWithThatIdAlreadyExist extends RuntimeException{

    private final long id;
    private final String entity;

    public ElementWithThatIdAlreadyExist(long id, String entity) {
        this.id = id;
        this.entity = entity;
    }


    @Override
    public String getMessage() {
        return entity + " with id = " + id + " already exist!";
    }
}

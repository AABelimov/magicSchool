package hogwarts.ru.magicschool;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class MagicSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagicSchoolApplication.class, args);
	}

}

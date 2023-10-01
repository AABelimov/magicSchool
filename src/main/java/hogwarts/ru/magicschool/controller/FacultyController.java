package hogwarts.ru.magicschool.controller;

import hogwarts.ru.magicschool.Entity.Faculty;
import hogwarts.ru.magicschool.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> getFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return facultyService.getFacultiesByColor(color);
        }
        return facultyService.getAllFaculties();
    }

    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public Faculty removeFaculty(@PathVariable Long id) {
        return facultyService.removeFaculty(id);
    }
}

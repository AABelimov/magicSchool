select student.name as student_name, student.age, faculty.name as faculty
from student
inner join faculty on student.faculty_id = faculty.id


select student.name
from student
inner join avatar on avatar.student_id = student.id
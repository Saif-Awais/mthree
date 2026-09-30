package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE
        String sql = "INSERT INTO student (sid, fName, lName) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, student.getStudentId(),
                student.getStudentFirstName(), student.getStudentLastName());

        return student;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        String sql = "SELECT * FROM student";

        List<Student> students = jdbcTemplate.query(sql, new StudentMapper());


        return students;

        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE

        String sql = "SELECT * FROM student WHERE sid = ?";

        Student student  = jdbcTemplate.queryForObject(sql, new StudentMapper(), id);
        return student;

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE

        String sql = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";

        jdbcTemplate.update(sql, student.getStudentFirstName(), student.getStudentLastName(),
                student.getStudentId());


        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE

        String sql = "DELETE FROM student WHERE sid = ?";

        jdbcTemplate.update(sql, id);


        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        String sql = "INSERT INTO course_student (student_id, course_id) " +
                "VALUES ( ?, ? )";

        jdbcTemplate.update(sql, studentId, courseId);


        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        String sql = "DELETE FROM course_student " +
                "WHERE student_id = ? AND course_id = ?";

        jdbcTemplate.update(sql, studentId, courseId);



        //YOUR CODE ENDS HERE
    }
}

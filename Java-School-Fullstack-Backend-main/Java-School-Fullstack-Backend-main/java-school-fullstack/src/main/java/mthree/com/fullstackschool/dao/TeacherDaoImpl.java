package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        String sql = "INSERT INTO teacher (tid, tFName, tLName, dept) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, teacher.getTeacherId(), teacher.getTeacherFName(),
                teacher.getTeacherLName(), teacher.getDept());

        return teacher;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE

        String sql = "SELECT * FROM teacher";

        List<Teacher> teachers = jdbcTemplate.query(sql, new TeacherMapper());

        return teachers;

        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE

        String sql = "SELECT * FROM teacher " +
                "WHERE tid = ?";

        return jdbcTemplate.queryForObject(sql, new TeacherMapper(), id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE

        jdbcTemplate.update("UPDATE teacher SET tFName = ?, tLName = ?, dept = ?  WHERE tid = ?",
                t.getTeacherFName(), t.getTeacherLName(), t.getDept(), t.getTeacherId());


        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE

        String sql = "DELETE FROM teacher " +
                "WHERE tid = ?";

        jdbcTemplate.update(sql, id);


        //YOUR CODE ENDS HERE
    }
}

package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.StudentDto;

import java.util.List;

public interface StudentDao {

  void insertDataStudent(StudentDto studentDto);

  int findStudentIdByUserId(int userId);

  int findVersionNumberFrom(StudentDto studentDto);

  StudentDto findStudentById(int studentId);

  List<StudentDto> findStudents();

  void updateDataStudent(StudentDto studentDto);

}

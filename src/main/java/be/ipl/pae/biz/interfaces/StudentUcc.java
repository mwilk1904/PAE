package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.StudentDto;

public interface StudentUcc {
  StudentDto getPersonalData(int id, boolean isStudentId);

  void updatePersonalData(StudentDto student);
}

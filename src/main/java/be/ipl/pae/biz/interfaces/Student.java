package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.StudentDto;

public interface Student extends StudentDto {
  boolean checkTitle();

  boolean checkSex();

  boolean checkBirthDate();


}

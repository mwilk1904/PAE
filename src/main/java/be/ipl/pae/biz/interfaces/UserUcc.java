package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.UserDto;

import java.util.List;

public interface UserUcc {

  UserDto signUp(UserDto user);

  UserDto login(String pseudo, String password);

  UserDto authentification(int id);

  List<UserDto> getUsers();

  void setTeacher(int userId);

  List<UserDto> getUsersWithStudentRole();

}

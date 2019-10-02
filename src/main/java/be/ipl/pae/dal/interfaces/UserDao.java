package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.UserDto;

import java.util.List;

public interface UserDao {

  void insertNewUser(UserDto userDto);

  UserDto findUserByPseudo(String pseudo);

  UserDto findUserByEmail(String email);

  UserDto findRespTeacher();

  UserDto findUserById(int id);

  List<UserDto> findUsers();

  void updateUser(UserDto user);

  List<UserDto> findStudents();

}

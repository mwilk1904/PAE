package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.UserDto;

public interface User extends UserDto {

  boolean checkPassword(String passwordDb);

  boolean checkUser();

  boolean checkEmail();

  boolean encryptPassword();
}

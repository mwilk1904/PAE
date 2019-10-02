package be.ipl.pae.biz.dto;

import java.util.Date;

public interface UserDto {
  String getPseudo();

  String getPassword();

  String getLastName();

  String getFirstName();

  String getEmail();

  int getId();
  
  Date getRecordDate();

  String getRole();

  void setPseudo(String pseudo);

  void setPassword(String password);

  void setLastName(String lastName);

  void setFirstName(String firstName);

  void setEmail(String email);

  void setRole(String role);

  void setId(int id);
  
  void setRecordDate(Date recordDate);

  int getVersionNumber();

  void setVersionNumber(int versionNumber);

}

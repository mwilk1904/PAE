package be.ipl.pae.biz.dto;

public interface MobilityPostulantDto {

  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String lastName);

  int getMobilitiesCount();

  void setMobilitiesCount(int count);

  int getUnconfirmedMobilitiesCount();

  void setUnconfirmedMobilitiesCount(int count);
  
  int getStudentId();
  
  void setStudentId(int id);

}

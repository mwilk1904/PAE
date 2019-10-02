package be.ipl.pae.biz.dto;

public interface MobilityCsvDto {

  int getApplicationNumber();
  
  void setApplicationNumber(int applicationNumber);
  
  String getFirstName();
  
  void setFirstName(String firstName);
  
  String getLastName();
  
  void setLastName(String lastName);
  
  String getDepartment();
  
  void setDepartment(String department);
  
  int getPreferenceOrderNumber();
  
  void setPreferenceOrderNumber(int preferenceOrderNumber);
  
  String getProgram();
  
  void setProgram(String program);
  
  String getMobilityCode();
  
  void setMobilityCode(String mobilityCode);
  
  String getSemester();
  
  void setSemester(String semester);
  
  String getPartner();
  
  void setPartner(String partner);
  
}

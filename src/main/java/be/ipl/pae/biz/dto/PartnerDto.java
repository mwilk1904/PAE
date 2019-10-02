package be.ipl.pae.biz.dto;

public interface PartnerDto {

  int getId();



  String getLegalName();

  String getBusinessName();

  String getFullName();

  String getDepartment();

  String getOrganisationType();

  int getEmployeesCount();

  String getEmail();

  String getWebsite();

  String getPhoneNumber();

  String getAddress();

  String getZipCode();

  String getArea();

  String getCity();

  String getMobilityCode();

  int getCountryId();

  void setId(int id);

  void setLegalName(String legalName);

  void setBusinessName(String businessName);

  void setFullName(String fullName);

  void setDepartment(String department);

  void setOrganisationType(String organisationType);

  void setEmployeesCount(int employeesCount);

  void setEmail(String email);

  void setWebsite(String website);

  void setPhoneNumber(String phoneNumber);

  void setAddress(String address);

  void setZipCode(String string);

  void setArea(String commune);

  void setCity(String city);

  void setMobilityCode(String mobilityCode);

  void setCountryId(int countryId);
}

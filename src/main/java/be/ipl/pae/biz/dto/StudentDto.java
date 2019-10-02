package be.ipl.pae.biz.dto;

import java.util.Date;

public interface StudentDto {

  int getId();

  void setId(int id);

  String getTitle();

  void setTitle(String title);

  Date getBirthDate();

  void setBirthDate(Date date);

  int getNationalityId();

  void setNationalityId(int nationalityId);

  String getStreet();

  void setStreet(String street);

  int getStreetNumber();

  void setStreetNumber(int streetNumber);

  String getBox();

  void setBox(String box);

  int getZipCode();

  void setZipCode(int zipCode);

  String getCommune();

  void setCommune(String commune);

  String getCity();

  void setCity(String city);

  String getPhoneNumber();

  void setPhoneNumber(String phoneNumber);

  char getSex();

  void setSex(char sex);

  int getNumberHighSchollYearsSucceded();

  void setNumberHighSchollYearsSucceded(int numberHighSchollYearsSucceded);

  String getBankCardNumber();

  void setBankCardNumber(String bankCardNumber);

  String getAccountOwner();

  void setAccountOwner(String accountOwner);

  String getBankName();

  void setBankName(String bankName);

  String getBicCode();

  void setBicCode(String bicCode);

  int getUserId();

  void setUser(int userId);

  int getVersionNumber();

  void setVersionNumber(int versionNumber);

}

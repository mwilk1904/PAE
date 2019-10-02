package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.MobilityCsv;

public class MobilityCsvImpl implements MobilityCsv {
  
  private int applicationNumber;
  private String firstName;
  private String lastName;
  private String department;
  private int preferenceOrderNumber;
  private String program;
  private String mobilityCode;
  private String semester;
  private String partner;

  @Override
  public int getApplicationNumber() {
    return this.applicationNumber;
  }

  @Override
  public void setApplicationNumber(int applicationNumber) {
    this.applicationNumber = applicationNumber;
  }

  @Override
  public String getFirstName() {
    return this.firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getLastName() {
    return this.lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getDepartment() {
    return this.department;
  }

  @Override
  public void setDepartment(String department) {
    this.department = department;
  }

  @Override
  public int getPreferenceOrderNumber() {
    return this.preferenceOrderNumber;
  }

  @Override
  public void setPreferenceOrderNumber(int preferenceOrderNumber) {
    this.preferenceOrderNumber = preferenceOrderNumber;
  }

  @Override
  public String getProgram() {
    return this.program;
  }

  @Override
  public void setProgram(String program) {
    this.program = program;
  }

  @Override
  public String getMobilityCode() {
    return this.mobilityCode;
  }

  @Override
  public void setMobilityCode(String mobilityCode) {
    this.mobilityCode = mobilityCode;
  }

  @Override
  public String getSemester() {
    return this.semester;
  }

  @Override
  public void setSemester(String semester) {
    this.semester = semester;
  }

  @Override
  public String getPartner() {
    return this.partner;
  }

  @Override
  public void setPartner(String partner) {
    this.partner = partner;
  }

}

package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.Partner;

public class PartnerImpl implements Partner {

  private int id;
  private String legalName;
  private String businessName;
  private String fullName;
  private String department;
  private String organisationType;
  private int employeesCount;
  private String address;
  private int countryId;
  private String area;
  private String zipCode;
  private String city;
  private String email;
  private String website;
  private String phoneNumber;
  private String mobilityCode;


  /**
   * Crée un objet PartnerImpl.
   * 
   * @param id l'id du partenaire.
   * @param legalName le nom légal du partenaire.
   * @param businessName le nom business du partenaire.
   * @param fullName le nom entier du partenaire.
   * @param department le département du partenaire.
   * @param organisationType le type d'organisation.
   * @param employeesCount le nombre d'employés.
   * @param address l'adresse
   * @param countryId l'id du pays.
   * @param area la zone.
   * @param zipCode le code postal
   * @param city la ville.
   * @param email le mail.
   * @param website le site web.
   * @param phoneNumber le numéro de telephone.
   * @param mobilityCode le code de mobilité.
   */
  public PartnerImpl(int id, String legalName, String businessName, String fullName,
      String department, String organisationType, int employeesCount, String address, int countryId,
      String area, String zipCode, String city, String email, String website, String phoneNumber,
      String mobilityCode) {
    super();
    this.id = id;
    this.legalName = legalName;
    this.businessName = businessName;
    this.fullName = fullName;
    this.department = department;
    this.organisationType = organisationType;
    this.employeesCount = employeesCount;
    this.address = address;
    this.countryId = countryId;
    this.area = area;
    this.zipCode = zipCode;
    this.city = city;
    this.email = email;
    this.website = website;
    this.phoneNumber = phoneNumber;
    this.mobilityCode = mobilityCode;
  }

  public PartnerImpl() {
    super();
  }

  public String getLegalName() {
    return legalName;
  }

  public void setLegalName(String legalName) {
    this.legalName = legalName;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getOrganisationType() {
    return organisationType;
  }

  public void setOrganisationType(String organisationType) {
    this.organisationType = organisationType;
  }

  public int getEmployeesCount() {
    return employeesCount;
  }

  public void setEmployeesCount(int employeesCount) {
    this.employeesCount = employeesCount;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getMobilityCode() {
    return mobilityCode;
  }

  public void setMobilityCode(String mobilityCode) {
    this.mobilityCode = mobilityCode;
  }

  public int getCountryId() {
    return countryId;
  }

  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }



  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

}

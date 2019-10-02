package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.Student;

import utils.Logs;

import java.time.LocalDate;
import java.util.Date;

public class StudentImpl implements Student {

  private int id;
  private String title;
  private Date birthDate;
  private int nationalityId;
  private String street;
  private int streetNumber;
  private String box;
  private int zipCode;
  private String commune;
  private String city;
  private String phoneNumber;
  private char sex;
  private int numberHighSchollYearsSucceded;
  private String bankCardNumber;
  private String accountOwner;
  private String bankName;
  private String bicCode;
  private int userId;
  private int versionNumber;

  /**
   * crée un objet StudentImpl.
   * 
   * @param id le numéro de l'étudiant.
   * @param title le titre de l'étudiant.
   * @param birthDate sa date de naissance.
   * @param nationalityId l'id de sa nationalité.
   * @param street sa rue.
   * @param streetNumber son numéro de rue.
   * @param box sa boite.
   * @param zipCode son code postal.
   * @param commune sa commune.
   * @param city sa ville.
   * @param phoneNumber son numéro de téléphone.
   * @param sex son sexe.
   * @param numberHighSchollYearsSucceded le nombre d'année qu'il a réussies.
   * @param bankCardNumber son numéro de carte bancaire.
   * @param bankName le nom de sa banque.
   * @param bicCode son numéro de bic.
   * @param userId l'id du user auquel il correspond.
   * @param versionNumber son numéro de version.
   */
  public StudentImpl(int id, String title, Date birthDate, int nationalityId, String street,
      int streetNumber, String box, int zipCode, String commune, String city, String phoneNumber,
      char sex, int numberHighSchollYearsSucceded, String bankCardNumber, String bankName,
      String bicCode, int userId, int versionNumber) {
    super();
    this.setId(id);
    this.title = title;
    this.birthDate = birthDate;
    this.nationalityId = nationalityId;
    this.street = street;
    this.streetNumber = streetNumber;
    this.box = box;
    this.zipCode = zipCode;
    this.commune = commune;
    this.city = city;
    this.phoneNumber = phoneNumber;
    this.sex = sex;
    this.numberHighSchollYearsSucceded = numberHighSchollYearsSucceded;
    this.bankCardNumber = bankCardNumber;
    this.bankName = bankName;
    this.bicCode = bicCode;
    this.userId = userId;
    this.versionNumber = versionNumber;
    // this.myMobilities = new ArrayList<Mobility>();
  }

  public StudentImpl() {
    super();
  }

  // public void addMobility(Mobility mob) {
  // myMobilities.add(mob);
  // }
  /**
   * vérifie le titre de l'étudiant.
   * 
   * @return true si il correspond à l'étudiant, false sinon.
   */
  public boolean checkTitle() {
    Logs.fine("Vérification de la validité du titre de l'étudiant");
    if (title != null && (title.equals("M.") || title.equals("Mme") || title.equals("Mlle"))) {
      return true;
    }
    Logs.fine("Le titre " + title + " n'est pas valide pour l'étudiant associé à l'utilisateur "
        + userId);
    return false;
  }

  /**
   * vérifie le sexe de l'étudiant.
   * 
   * @return true si il correspond à l'étudiant, false sinon.
   */
  public boolean checkSex() {
    Logs.fine("Vérification de la validité du sexe de l'étudiant");
    if (sex == 'M' || sex == 'F') {
      return true;
    }
    Logs.fine(
        "Le sexe " + sex + " n'est pas valide pour l'étudiant associé à l'utilisateur " + userId);
    return false;
  }

  /**
   * vérifie sa date de naissance.
   * 
   * @return true si elle correspond à l'étudiant, false sinon.
   */
  @SuppressWarnings("deprecation")
  public boolean checkBirthDate() {
    Logs.fine("Vérification de la validité de la date de naissance de l'étudiant.");
    int thisYear = LocalDate.now().getYear();
    if (birthDate != null && birthDate.getYear() + 1900 > thisYear - 150
        && birthDate.getYear() + 1900 < thisYear - 16) {
      return true;
    }
    Logs.fine("La date de naissance n'est pas valide" + "pour l'étudiant associé à l'utilisateur "
        + userId);
    return false;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public int getNationalityId() {
    return nationalityId;
  }

  public void setNationalityId(int nationalityId) {
    this.nationalityId = nationalityId;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public int getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(int streetNumber) {
    this.streetNumber = streetNumber;
  }

  public String getBox() {
    return box;
  }

  public void setBox(String box) {
    this.box = box;
  }

  public int getZipCode() {
    return zipCode;
  }

  public void setZipCode(int zipCode) {
    this.zipCode = zipCode;
  }

  public String getCommune() {
    return commune;
  }

  public void setCommune(String commune) {
    this.commune = commune;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public char getSex() {
    return sex;
  }

  public void setSex(char sex) {
    this.sex = sex;
  }

  public int getNumberHighSchollYearsSucceded() {
    return numberHighSchollYearsSucceded;
  }

  public void setNumberHighSchollYearsSucceded(int numberHighSchollYearsSucceded) {
    this.numberHighSchollYearsSucceded = numberHighSchollYearsSucceded;
  }

  public String getBankCardNumber() {
    return bankCardNumber;
  }

  public void setBankCardNumber(String bankCardNumber) {
    this.bankCardNumber = bankCardNumber;
  }

  public String getAccountOwner() {
    return accountOwner;
  }

  public void setAccountOwner(String accountOwner) {
    this.accountOwner = accountOwner;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBicCode() {
    return bicCode;
  }

  public void setBicCode(String bicCode) {
    this.bicCode = bicCode;
  }

  public int getUserId() {
    return userId;
  }

  public void setUser(int userId) {
    this.userId = userId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }
}

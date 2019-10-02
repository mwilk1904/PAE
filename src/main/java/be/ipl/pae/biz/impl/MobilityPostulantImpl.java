package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.MobilityPostulant;

public class MobilityPostulantImpl implements MobilityPostulant {

  int studentId;
  String firstName;
  String lastName;
  int mobilitiesCount;
  int unconfirmedMobilitiesCount;

  public MobilityPostulantImpl() {
    super();
  }

  /**
   * crée un objet MobilityPostulantImpl
   * 
   * @param studentId le numéro de l'étudiant.
   * @param firstName son prénom.
   * @param lastName son nom.
   * @param mobilitiesCount le nombre de mobilités
   * @param unconfirmedMobCount le nombre de mobilités non confirmées.
   */
  public MobilityPostulantImpl(int studentId, String firstName, String lastName,
      int mobilitiesCount, int unconfirmedMobCount) {
    super();
    this.studentId = studentId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobilitiesCount = mobilitiesCount;
    this.unconfirmedMobilitiesCount = unconfirmedMobCount;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public int getMobilitiesCount() {
    return mobilitiesCount;
  }

  @Override
  public void setMobilitiesCount(int count) {
    this.mobilitiesCount = count;
  }

  @Override
  public int getUnconfirmedMobilitiesCount() {
    return unconfirmedMobilitiesCount;
  }

  @Override
  public void setUnconfirmedMobilitiesCount(int count) {
    this.unconfirmedMobilitiesCount = count;
  }

  @Override
  public int getStudentId() {
    return this.studentId;
  }

  @Override
  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

}

package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.MobilityAll;

import java.util.Date;

public class MobilityAllImpl implements MobilityAll {
  private String mobilityCode;
  private int countryId;
  private int partnerId;
  private String firstName;
  private String lastName;
  private String state;
  private int preferenceOrderNumber;
  private int mobId;
  private String cancellationReason;
  private int programId;
  private int applicationNumber;
  private boolean confirmed;
  private boolean firstPayRequired;
  private boolean secondPayRequired;
  private Date introductionDate;

  public MobilityAllImpl() {
    super();
  }

  /**
   * Crée un objet MobilityAllImpl.
   * 
   * @param mobilityCode le code de la mobilité.
   * @param countryId l'id du pays.
   * @param partnerId l'id du partenaire.
   * @param firstName le prenom de l'étudiant.
   * @param lastName le nom de l'étudiant.
   * @param state l'état de la mobilité.
   * @param preferenceOrder l'ordre de préférence de la mobilité.
   * @param mobId l'id de la mobilité.
   * @param cancellationReason la raison d'annulation de la mobilité.
   * @param programId l'id du programme.
   * @param applicationNumber le numéro de la candidature.
   * @param confirmed si la mobilité est confirmée.
   * @param firstPayRequired si le premier paiement est payé.
   * @param secondPayRequired si le second paiement est payé.
   * @param introductionDate la date d'introduction.
   */
  public MobilityAllImpl(String mobilityCode, int countryId, int partnerId, String firstName,
      String lastName, String state, int preferenceOrder, int mobId, String cancellationReason,
      int programId, int applicationNumber, boolean confirmed, boolean firstPayRequired,
      boolean secondPayRequired, Date introductionDate) {
    super();
    this.mobilityCode = mobilityCode;
    this.countryId = countryId;
    this.partnerId = partnerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.state = state;
    this.preferenceOrderNumber = preferenceOrder;
    this.mobId = mobId;
    this.cancellationReason = cancellationReason;
    this.programId = programId;
    this.applicationNumber = applicationNumber;
    this.confirmed = confirmed;
    this.firstPayRequired = firstPayRequired;
    this.secondPayRequired = secondPayRequired;
    this.setIntroductionDate(introductionDate);
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
  public int getCountryId() {

    return this.countryId;
  }

  @Override
  public void setCountryId(int countryId) {
    this.countryId = countryId;

  }

  @Override
  public int getPartnerId() {

    return this.partnerId;
  }

  @Override
  public void setPartnerId(int partnerId) {
    this.partnerId = partnerId;

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
  public void setState(String state) {
    this.state = state;

  }

  @Override
  public String getState() {

    return this.state;
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
  public int getMobId() {

    return this.mobId;
  }

  @Override
  public void setMobId(int mobId) {
    this.mobId = mobId;
  }

  @Override
  public String getCancellationReason() {

    return this.cancellationReason;
  }

  @Override
  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;

  }

  @Override
  public int getProgramId() {

    return this.programId;
  }

  @Override
  public void setProgramId(int programId) {
    this.programId = programId;

  }

  @Override
  public int getApplicationNumber() {

    return this.applicationNumber;
  }

  @Override
  public void setApplicationNumber(int applicationNumber) {
    this.applicationNumber = applicationNumber;

  }

  @Override
  public boolean isConfirmed() {

    return this.confirmed;
  }

  @Override
  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;

  }

  @Override
  public boolean getFirstPayReq() {

    return this.firstPayRequired;
  }

  @Override
  public void setFirstPayReq(boolean firstPayReq) {
    this.firstPayRequired = firstPayReq;

  }

  @Override
  public boolean getSecondPayReq() {

    return this.secondPayRequired;
  }

  @Override
  public void setSecondPayReq(boolean secondPayReq) {
    this.secondPayRequired = secondPayReq;

  }

  public Date getIntroductionDate() {
    return introductionDate;
  }

  public void setIntroductionDate(Date introductionDate) {
    this.introductionDate = introductionDate;
  }



}

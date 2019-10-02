package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.interfaces.Mobility;

import com.owlike.genson.annotation.JsonIgnore;

import utils.Logs;

import java.time.LocalDate;
import java.util.Date;

public class MobilityImpl implements Mobility {

  private int id;
  private int applicationNumber;
  private int preferenceOrderNumber;
  private String mobilityCode;
  private int semester;
  private int partnerId;
  private Date introductionDate;
  private boolean confirmed;
  private String cancellationReason;
  private boolean firstPayReq;
  private boolean secondPayReq;
  private int studentId;
  private int countryId;
  private int programId;
  private String state;
  private int versionNumber;
  private boolean cancelledByTeacher;

  // documents prealables
  private boolean scholarshipContract;
  private boolean internshipStudyAgreement;
  private boolean passingLangTestProofGo;
  private boolean studentChart;
  private boolean commitmentDoc;

  // documents retour
  private boolean stayCertificate;
  private boolean transcripts;
  private boolean stageCertificate;
  private String finalReport;
  private boolean passingLangTestProofBack;

  // logiciel
  private boolean proEco;
  private boolean mobilityTool;
  private boolean mobi;

  public MobilityImpl() {

  }

  /**
   * Crée un objet MobilityImpl.
   * 
   * @param id le numéro de la mobilité
   * @param applicationNumber le numéro de candidature de la mobilité
   * @param preferenceOrderNumber l'ordre de préférence parmi les choix de mobilité
   * @param mobilityCode le code de la mobilité
   * @param semester le semestre auquel la mobilité va se dérouler
   * @param partnerId le numéro du partenaire de la mobilité
   * @param studentId le numéro de l'étudiant participant à cette mobilité
   */
  public MobilityImpl(int id, int applicationNumber, int preferenceOrderNumber, String mobilityCode,
      int semester, int partnerId, int studentId) {
    super();
    this.id = id;
    this.applicationNumber = applicationNumber;
    this.preferenceOrderNumber = preferenceOrderNumber;
    if (checkMobilityCode(mobilityCode)) {
      this.mobilityCode = mobilityCode;
    } else {
      this.mobilityCode = null;
    }

    this.semester = semester;
    this.partnerId = partnerId;
    this.studentId = studentId;
    this.introductionDate = new Date();
    this.confirmed = false;
  }

  @SuppressWarnings("deprecation")
  @JsonIgnore
  @Override
  public boolean isThisAcademicYear() {
    LocalDate now = LocalDate.now();
    if (now.getMonthValue() >= 9 && introductionDate.getYear() + 1900 == now.getYear()
        && introductionDate.getMonth() >= 9
        || now.getMonthValue() < 9 && (introductionDate.getYear() + 1900 == now.getYear()
            || introductionDate.getYear() + 1900 == now.getYear() - 1
                && introductionDate.getMonth() >= 9)) {
      return true;
    }
    return false;
  }

  @JsonIgnore
  @Override
  public boolean checkMobility() {
    Logs.fine("Vérification de la validité de la mobilité");
    if (this.applicationNumber < 0 || this.preferenceOrderNumber < 1 || this.mobilityCode == null
        || this.semester != 1 && this.semester != 2 || this.partnerId < 1 && this.countryId < 1
        || this.introductionDate == null) {
      Logs.fine("La mobilité " + id + " n'est pas valide");
      return false;
    }
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCancelled() {
    if (cancellationReason == null) {
      return false;
    }
    return true;
  }



  @JsonIgnore
  @Override
  public boolean isBeforeDocsFilled(ProgramDto program) {
    if ((this.isScholarshipContract() || "Suisse".equals(program.getProgramName()))
        && this.isInternshipStudyAgreement() && this.isStudentChart() && this.isCommitmentDoc()
        && (this.isPassingLangTestProofGo() || !"Erasmus+".equals(program.getProgramName()))) {
      return true;
    }
    return false;
  }

  @JsonIgnore
  @Override
  public boolean isAllDocsFilled(ProgramDto program) {
    if (this.isBeforeDocsFilled(program) && this.isStayCertificate() && this.isTranscripts()
        && this.isStageCertificate()
        && (this.isPassingLangTestProofBack() || !"Erasmus+".equals(program.getProgramName()))
        && this.getFinalReport() != null) {
      return true;
    }
    return false;
  }

  @JsonIgnore
  @Override
  public boolean isOneDocFilled(ProgramDto program) {
    if (this.isScholarshipContract() && !"Suisse".equals(program.getProgramName())
        || this.isInternshipStudyAgreement() || this.isStudentChart() || this.isCommitmentDoc()
        || this.isPassingLangTestProofGo() && "Erasmus+".equals(program.getProgramName())) {
      return true;
    }
    return false;
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
  public int getPreferenceOrderNumber() {

    return this.preferenceOrderNumber;
  }


  @Override
  public void setPreferenceOrderNumber(int preferenceOrderNumber) {
    this.preferenceOrderNumber = preferenceOrderNumber;

  }


  @Override
  public String getMobilityCode() {
    if (checkMobilityCode(this.mobilityCode)) {
      return this.mobilityCode;
    }
    return null;

  }


  @Override
  public void setMobilityCode(String mobilityCode) {
    if (checkMobilityCode(mobilityCode)) {
      this.mobilityCode = mobilityCode;
    } else {
      this.mobilityCode = null;
    }
  }


  @Override
  public int getSemester() {

    return this.semester;
  }


  @Override
  public void setSemester(int semester) {
    this.semester = semester;

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
  public Date getIntroductionDate() {

    return this.introductionDate;
  }

  @Override
  public void setIntroductionDate(Date introductionDate) {
    this.introductionDate = introductionDate;

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
  public int getStudentId() {
    return studentId;
  }

  @Override
  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }



  @Override
  public boolean isScholarshipContract() {

    return this.scholarshipContract;
  }


  @Override
  public void setScholarshipContract(boolean scholarshipContract) {
    this.scholarshipContract = scholarshipContract;

  }


  @Override
  public boolean isInternshipStudyAgreement() {

    return this.internshipStudyAgreement;
  }


  @Override
  public void setInternshipStudyAgreement(boolean internshipStudyAgreement) {
    this.internshipStudyAgreement = internshipStudyAgreement;

  }


  @Override
  public boolean isPassingLangTestProofGo() {

    return this.passingLangTestProofGo;
  }


  @Override
  public void setPassingLangTestProofGo(boolean passingLangTestProofGo) {
    this.passingLangTestProofGo = passingLangTestProofGo;

  }


  @Override
  public boolean isStudentChart() {

    return this.studentChart;
  }


  @Override
  public void setStudentChart(boolean studentChart) {
    this.studentChart = studentChart;

  }


  @Override
  public boolean isCommitmentDoc() {

    return this.commitmentDoc;
  }


  @Override
  public void setCommitmentDoc(boolean commitmentDoc) {
    this.commitmentDoc = commitmentDoc;

  }


  @Override
  public boolean isStayCertificate() {

    return this.stayCertificate;
  }


  @Override
  public void setStayCertificate(boolean stayCertificate) {
    this.stayCertificate = stayCertificate;

  }


  @Override
  public boolean isTranscripts() {

    return this.transcripts;
  }


  @Override
  public void setTranscripts(boolean transcripts) {
    this.transcripts = transcripts;

  }


  @Override
  public boolean isStageCertificate() {

    return this.stageCertificate;
  }


  @Override
  public void setStageCertificate(boolean stageCertificate) {
    this.stageCertificate = stageCertificate;

  }


  @Override
  public String getFinalReport() {

    return this.finalReport;
  }


  @Override
  public void setFinalReport(String finalReport) {
    this.finalReport = finalReport;

  }


  @Override
  public boolean isPassingLangTestProofBack() {

    return this.passingLangTestProofBack;
  }


  @Override
  public void setPassingLangTestProofBack(boolean passingLangTestProofBack) {
    this.passingLangTestProofBack = passingLangTestProofBack;

  }

  @Override
  public boolean isConfirmed() {
    return confirmed;
  }

  @Override
  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }


  public int getCountryId() {
    return countryId;
  }


  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }



  public String getState() {
    return state;
  }


  public void setState(String state) {
    this.state = state;
  }


  public int getId() {

    return id;
  }


  public void setId(int id) {
    this.id = id;

  }

  public boolean isProEco() {
    return proEco;
  }

  public void setProEco(boolean proEco) {
    this.proEco = proEco;
  }

  public boolean isMobilityTool() {
    return mobilityTool;
  }

  public void setMobilityTool(boolean mobilityTool) {
    this.mobilityTool = mobilityTool;
  }

  public boolean isMobi() {
    return mobi;
  }

  public void setMobi(boolean mobi) {
    this.mobi = mobi;
  }

  public boolean getFirstPayReq() {
    return firstPayReq;
  }


  public void setFirstPayReq(boolean firstPayReq) {
    this.firstPayReq = firstPayReq;
  }


  public boolean getSecondPayReq() {
    return secondPayReq;
  }


  public void setSecondPayReq(boolean secondPayReq) {
    this.secondPayReq = secondPayReq;
  }

  public int getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  private boolean checkMobilityCode(String mobilityCode) {
    if (mobilityCode != null && (mobilityCode.equals("SMS") || mobilityCode.equals("SMP"))) {
      return true;
    }
    return false;

  }

  @Override
  public int getProgramId() {
    return programId;
  }

  @Override
  public void setProgramId(int programId) {
    this.programId = programId;
  }

  public boolean isCancelledByTeacher() {
    return cancelledByTeacher;
  }

  public void setCancelledByTeacher(boolean cancelledByTeacher) {
    this.cancelledByTeacher = cancelledByTeacher;
  }



}

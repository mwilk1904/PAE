package be.ipl.pae.biz.dto;

import java.util.Date;

public interface MobilityDto {

  int getId();

  void setId(int id);

  void setApplicationNumber(int applicationNumber);

  int getPreferenceOrderNumber();

  void setPreferenceOrderNumber(int preferenceOrderNumber);

  String getMobilityCode();

  void setMobilityCode(String mobilityCode);

  int getSemester();

  void setSemester(int semester);

  Date getIntroductionDate();

  void setIntroductionDate(Date introductionDate);

  String getCancellationReason();

  void setCancellationReason(String cancellationReason);

  boolean isScholarshipContract();

  void setScholarshipContract(boolean scholarshipContract);

  boolean isInternshipStudyAgreement();

  void setInternshipStudyAgreement(boolean internshipStudyAgreement);

  boolean isPassingLangTestProofGo();

  void setPassingLangTestProofGo(boolean passingLangTestProofGo);

  boolean isStudentChart();

  void setStudentChart(boolean studentChart);

  boolean isCommitmentDoc();

  void setCommitmentDoc(boolean commitmentDoc);

  boolean isStayCertificate();

  void setStayCertificate(boolean stayCertificate);

  boolean isTranscripts();

  void setTranscripts(boolean transcripts);

  boolean isStageCertificate();

  void setStageCertificate(boolean stageCertificate);

  String getFinalReport();

  void setFinalReport(String finalReport);

  boolean isPassingLangTestProofBack();

  void setPassingLangTestProofBack(boolean passingLangTestProofBack);

  int getStudentId();

  void setStudentId(int studentId);

  boolean isConfirmed();

  void setConfirmed(boolean confirmed);

  int getApplicationNumber();

  void setPartnerId(int partnerId);

  int getPartnerId();

  boolean getFirstPayReq();


  void setFirstPayReq(boolean firstPayReq);


  boolean getSecondPayReq();


  void setSecondPayReq(boolean secondPayReq);

  void setState(String string);

  String getState();

  boolean isProEco();

  void setProEco(boolean proEco);

  boolean isMobilityTool();

  void setMobilityTool(boolean mobilityTool);

  boolean isMobi();

  void setMobi(boolean mobi);

  void setCountryId(int int1);

  int getCountryId();

  int getProgramId();

  void setProgramId(int programId);

  int getVersionNumber();

  void setVersionNumber(int versionNumber);
  
  boolean isCancelledByTeacher();
  
  void setCancelledByTeacher(boolean cancelledByTeacher);

}

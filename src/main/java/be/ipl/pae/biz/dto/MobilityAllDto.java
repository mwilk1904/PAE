package be.ipl.pae.biz.dto;

public interface MobilityAllDto {
  String getMobilityCode();

  void setMobilityCode(String mobilityCode);

  void setState(String state);

  String getState();

  int getCountryId();

  void setCountryId(int countryId);

  int getPartnerId();

  void setPartnerId(int partnerId);

  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String lastName);

  int getPreferenceOrderNumber();

  void setPreferenceOrderNumber(int preferenceOrderNumber);

  int getMobId();

  void setMobId(int id);

  String getCancellationReason();

  void setCancellationReason(String cancellationReason);

  int getProgramId();

  void setProgramId(int id);

  int getApplicationNumber();

  void setApplicationNumber(int applicationNumber);

  boolean isConfirmed();

  void setConfirmed(boolean confirmed);

  boolean getFirstPayReq();


  void setFirstPayReq(boolean firstPayReq);


  boolean getSecondPayReq();


  void setSecondPayReq(boolean secondPayReq);
}

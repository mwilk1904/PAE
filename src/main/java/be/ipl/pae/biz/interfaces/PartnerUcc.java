package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.PartnerDto;

import java.util.List;

public interface PartnerUcc {
  List<PartnerDto> getPartners();

  List<PartnerDto> searchPartners(String keyword);

  void insertPartner(PartnerDto partnerDto);

  PartnerDto getPartnerData(int partnerId);

}

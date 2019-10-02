package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.PartnerDto;

import java.util.List;

public interface PartnerDao {
  List<PartnerDto> findPartners();

  void insertNewPartner(PartnerDto partnerDto);

  PartnerDto findPartnerById(int partnerId);

  List<PartnerDto> getKeywordPartners(String keyword);
}

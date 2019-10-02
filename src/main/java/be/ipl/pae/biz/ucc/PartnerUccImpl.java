package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.interfaces.Partner;
import be.ipl.pae.biz.interfaces.PartnerUcc;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.Collections;
import java.util.List;

public class PartnerUccImpl implements PartnerUcc {
  PartnerDao partnerDao;
  private DalServices dalServices;

  /**
   * Crée un objet PartnerUccImpl.
   * 
   * @param partnerDao un objet PartnerDao.
   * @param dalServices un objet DalServices.
   */
  public PartnerUccImpl(PartnerDao partnerDao, DalServices dalServices) {
    super();
    this.partnerDao = partnerDao;
    this.dalServices = dalServices;
  }

  @Override
  public List<PartnerDto> getPartners() {
    Logs.fine("Récupération des partenaires");
    List<PartnerDto> partners;
    try {
      dalServices.startTransaction();
      partners = partnerDao.findPartners();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(partners);
  }

  @Override
  public List<PartnerDto> searchPartners(String keyword) {
    Logs.fine("Recherche de partenaires");
    List<PartnerDto> listPartners;
    try {
      dalServices.startTransaction();
      listPartners = partnerDao.getKeywordPartners(keyword);
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException();
    }
    dalServices.commit();
    return Collections.unmodifiableList(listPartners);
  }

  @Override
  public void insertPartner(PartnerDto partnerDto) {
    Partner partner = (Partner) partnerDto;
    Logs.fine("Insertion des données du partenaire");
    dalServices.startTransaction();
    try {
      // partenaire deja utilise
      if (partnerDao.findPartnerById(partner.getId()) != null) {
        throw new BizException("Partner deja utilisé.", "partner");

      }

      partnerDao.insertNewPartner(partner);
      dalServices.commit();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }

  }

  @Override
  public PartnerDto getPartnerData(int partnerId) {
    if (partnerId >= 1) {
      PartnerDto partner;
      dalServices.startTransaction();
      try {
        partner = partnerDao.findPartnerById(partnerId);
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
      dalServices.commit();
      return partner;
    } else {
      throw new BizException("L'id " + partnerId + " est incorrect", "partnerId");
    }
  }

}

package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;
import java.util.List;

public class PartnerDaoMock implements PartnerDao {

  private boolean findPartnersNull;
  private boolean findPartnerByIdNull;
  private boolean getKeywordPartnersNull;
  private boolean testFatalException;
  private BizFactory bizFactory;

  /**
   * Crée un objet PartnerDaoMock.
   * 
   * @param findPartnersNull vérifie si findPartners est null.
   * @param findPartnerByIdNull vérifie si findPartnerById est null.
   * @param getKeywordPartnersNull vérifie si getKeywordPartners est null.
   */
  public PartnerDaoMock(boolean findPartnersNull, boolean findPartnerByIdNull,
      boolean getKeywordPartnersNull, boolean testFatalException) {
    super();
    this.findPartnersNull = findPartnersNull;
    this.findPartnerByIdNull = findPartnerByIdNull;
    this.getKeywordPartnersNull = getKeywordPartnersNull;
    this.testFatalException = testFatalException;
    this.bizFactory = new BizFactoryStub();
  }

  @Override
  public List<PartnerDto> findPartners() {
    testFatalException();
    if (findPartnersNull) {
      return null;
    }
    List<PartnerDto> partners = new ArrayList<>();
    partners.add(bizFactory.getPartnerDto());
    return partners;
  }

  @Override
  public void insertNewPartner(PartnerDto partnerDto) {
    testFatalException();

  }

  @Override
  public PartnerDto findPartnerById(int partnerId) {
    testFatalException();
    if (findPartnerByIdNull) {
      return null;
    }
    return bizFactory.getPartnerDto();

  }

  @Override
  public List<PartnerDto> getKeywordPartners(String keyword) {
    testFatalException();
    if (getKeywordPartnersNull) {
      return null;
    }
    List<PartnerDto> partners = new ArrayList<>();
    partners.add(bizFactory.getPartnerDto());
    return partners;
  }

  private void testFatalException() {
    if (testFatalException) {
      throw new DalException();
    }
  }

}

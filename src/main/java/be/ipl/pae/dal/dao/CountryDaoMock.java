package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;
import java.util.List;

public class CountryDaoMock implements CountryDao {
  private boolean findCountriesByProgramNull;
  private boolean findCountryByIdNull;
  private boolean testDalException;
  private BizFactory bizFactory;

  /**
   * Crée un objet CountryDaoMock.
   * 
   * @param findCountriesByProgramNull verifie si findCountriesByProgram est null.
   * @param findCountryByIdNull verifie si findCountryById est null.
   * @param testDalException lance une DalException.
   */
  public CountryDaoMock(boolean findCountriesByProgramNull, boolean findCountryByIdNull,
      boolean testDalException) {
    super();
    this.findCountriesByProgramNull = findCountriesByProgramNull;
    this.findCountryByIdNull = findCountryByIdNull;
    this.testDalException = testDalException;
    this.bizFactory = new BizFactoryStub();
  }

  @Override
  public List<CountryDto> findCountriesByProgram() {
    testDalException();
    if (findCountriesByProgramNull) {
      return null;
    }
    List<CountryDto> countries = new ArrayList<>();
    countries.add(bizFactory.getCountryDto());
    return countries;
  }

  @Override
  public CountryDto findCountryById(int countryId) {
    testDalException();
    if (findCountryByIdNull) {
      return null;
    }
    return bizFactory.getCountryDto();
  }

  private void testDalException() {
    if (testDalException) {
      throw new DalException();
    }
  }

}

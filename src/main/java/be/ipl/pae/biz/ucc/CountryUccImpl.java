package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.interfaces.CountryUcc;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.Collections;
import java.util.List;

public class CountryUccImpl implements CountryUcc {
  private CountryDao countryDao;
  private DalServices dalServices;

  /**
   * Crée un objet CountryUccImpl.
   * 
   * @param countryDao un objet CountryDao.
   * @param dalServices un objet DalServices.
   */
  public CountryUccImpl(CountryDao countryDao, DalServices dalServices) {
    super();
    this.countryDao = countryDao;
    this.dalServices = dalServices;
  }

  @Override
  public List<CountryDto> getCountries() {
    Logs.fine("Récupération des pays");
    List<CountryDto> countries;
    try {
      dalServices.startTransaction();
      countries = countryDao.findCountriesByProgram();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(countries);
  }

}

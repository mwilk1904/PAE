package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.CountryUcc;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestCountryUcc {
  CountryDao countryDao;
  Constructor<?> countryDaoConstruct;
  CountryUcc countryUcc;
  Constructor<?> countryUccConstruct;
  BizFactory bizFactory;
  DalServices dalServices;
  CountryDto countryDto;

  {
    try {
      Config.init("tests.properties");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @BeforeEach
  void setUp() throws Exception {
    bizFactory = (BizFactory) Class.forName(Config.getValueOfKey(BizFactory.class.getName()))
        .getConstructor().newInstance();
    countryDto = bizFactory.getCountryDto();
    dalServices = (DalServices) Class.forName(Config.getValueOfKey(DalServices.class.getName()))
        .getConstructor().newInstance();
    countryDaoConstruct = Class.forName(Config.getValueOfKey(CountryDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class);
    countryUccConstruct = Class.forName(Config.getValueOfKey(CountryUcc.class.getName()))
        .getConstructor(CountryDao.class, DalServices.class);
  }

  @Test
  @DisplayName("")
  final void testCountries() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    countryDao = (CountryDao) countryDaoConstruct.newInstance(false, false, true);
    countryUcc = (CountryUcc) countryUccConstruct.newInstance(countryDao, dalServices);
    assertThrows(FatalException.class, () -> countryUcc.getCountries());
  }

  @Test
  @DisplayName("")
  final void testCountries2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    countryDao = (CountryDao) countryDaoConstruct.newInstance(false, false, false);
    countryUcc = (CountryUcc) countryUccConstruct.newInstance(countryDao, dalServices);
    countryUcc.getCountries();
  }

}

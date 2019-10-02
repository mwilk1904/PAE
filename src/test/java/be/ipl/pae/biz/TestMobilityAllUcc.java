package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.MobilityAllUcc;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestMobilityAllUcc {
  MobilityAllUcc mobilityAllUcc;
  Constructor<?> mobilityAllUccConstruct;
  BizFactory bizFactory;
  MobilityAllDto mobilityAllDto;
  MobilityDao mobilityDao;
  StudentDao studentDao;
  DalServices dalServices;
  PartnerDao partnerDao;
  ProgramDao programDao;
  CountryDao countryDao;
  UserDao userDao;

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
    dalServices = (DalServices) Class.forName(Config.getValueOfKey(DalServices.class.getName()))
        .getConstructor().newInstance();
    mobilityAllDto = bizFactory.getMobilityAllDto();

    studentDao = (StudentDao) Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false);
    programDao = (ProgramDao) Class.forName(Config.getValueOfKey(ProgramDao.class.getName()))
        .getConstructor(boolean.class, boolean.class).newInstance(false, false);
    partnerDao = (PartnerDao) Class.forName(Config.getValueOfKey(PartnerDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false, false);
    countryDao = (CountryDao) Class.forName(Config.getValueOfKey(CountryDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false);
    userDao = (UserDao) Class.forName(Config.getValueOfKey(UserDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class, boolean.class)
        .newInstance(true, true, true, false, false, false, false, false);
    mobilityDao = (MobilityDao) Class.forName(Config.getValueOfKey(MobilityDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false, false, false, false, false, false, false);

    mobilityAllUccConstruct = Class.forName(Config.getValueOfKey(MobilityAllUcc.class.getName()))
        .getConstructor(DalServices.class, StudentDao.class, MobilityDao.class, PartnerDao.class,
            CountryDao.class, UserDao.class, ProgramDao.class);
  }

  @Test
  void testOverview()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    mobilityDao = (MobilityDao) Class.forName(Config.getValueOfKey(MobilityDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false, false, false, false, false, false, true);
    mobilityAllUcc = (MobilityAllUcc) mobilityAllUccConstruct.newInstance(dalServices, studentDao,
        mobilityDao, partnerDao, countryDao, userDao, programDao);
    assertThrows(FatalException.class, () -> mobilityAllUcc.overview());
  }

  @Test
  void testOverview2()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    mobilityAllUcc = (MobilityAllUcc) mobilityAllUccConstruct.newInstance(dalServices, studentDao,
        mobilityDao, partnerDao, countryDao, userDao, programDao);
    mobilityAllUcc.overview();
  }

  @Test
  void testOverview3()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    programDao = (ProgramDao) Class.forName(Config.getValueOfKey(ProgramDao.class.getName()))
        .getConstructor(boolean.class, boolean.class).newInstance(false, true);
    partnerDao = (PartnerDao) Class.forName(Config.getValueOfKey(PartnerDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class)
        .newInstance(false, true, false, false);
    countryDao = (CountryDao) Class.forName(Config.getValueOfKey(CountryDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class)
        .newInstance(false, true, false);
    mobilityAllUcc = (MobilityAllUcc) mobilityAllUccConstruct.newInstance(dalServices, studentDao,
        mobilityDao, partnerDao, countryDao, userDao, programDao);
    mobilityAllUcc.overview();
  }


}

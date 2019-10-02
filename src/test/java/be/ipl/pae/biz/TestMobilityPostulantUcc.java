package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.MobilityPostulantUcc;
import be.ipl.pae.dal.interfaces.MobilityDao;
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

class TestMobilityPostulantUcc {
  MobilityPostulantUcc mobilityPostulantUcc;
  Constructor<?> mobilityPostulantUccConstruct;
  BizFactory bizFactory;
  MobilityPostulantDto mobilityPostulantDto;
  MobilityDao mobilityDao;
  StudentDao studentDao;
  DalServices dalServices;
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
    mobilityPostulantDto = bizFactory.getMobilityPostulantDto();

    studentDao = (StudentDao) Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
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

    mobilityPostulantUccConstruct =
        Class.forName(Config.getValueOfKey(MobilityPostulantUcc.class.getName()))
            .getConstructor(DalServices.class, StudentDao.class, MobilityDao.class, UserDao.class);
  }

  @Test
  void testStudentOverview()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    mobilityDao = (MobilityDao) Class.forName(Config.getValueOfKey(MobilityDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false, false, false, false, false, false, true);
    mobilityPostulantUcc = (MobilityPostulantUcc) mobilityPostulantUccConstruct
        .newInstance(dalServices, studentDao, mobilityDao, userDao);
    assertThrows(FatalException.class, () -> mobilityPostulantUcc.studentOverview());
  }

  @Test
  void testStudentOverview2()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    mobilityPostulantUcc = (MobilityPostulantUcc) mobilityPostulantUccConstruct
        .newInstance(dalServices, studentDao, mobilityDao, userDao);
    mobilityPostulantUcc.studentOverview();
  }

}

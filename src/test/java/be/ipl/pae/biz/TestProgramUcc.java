package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.ProgramUcc;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.services.DalServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestProgramUcc {
  ProgramDao programDao;
  Constructor<?> programDaoConstruct;
  ProgramUcc programUcc;
  Constructor<?> programUccConstruct;
  BizFactory bizFactory;
  DalServices dalServices;
  ProgramDto programDto;

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
    programDto = bizFactory.getProgramDto();
    programDaoConstruct = Class.forName(Config.getValueOfKey(ProgramDao.class.getName()))
        .getConstructor(boolean.class, boolean.class);
    programUccConstruct = Class.forName(Config.getValueOfKey(ProgramUcc.class.getName()))
        .getConstructor(ProgramDao.class, DalServices.class);
  }

  @Test
  @DisplayName("test ok")
  final void testGetPrograms() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    programDao = (ProgramDao) programDaoConstruct.newInstance(false, false);
    programUcc = (ProgramUcc) programUccConstruct.newInstance(programDao, dalServices);
    assertEquals(programUcc.getPrograms().get(0).getId(), programDto.getId());
  }

  @Test
  @DisplayName("test null")
  final void testGetPrograms2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    programDao = (ProgramDao) programDaoConstruct.newInstance(true, false);
    programUcc = (ProgramUcc) programUccConstruct.newInstance(programDao, dalServices);
    assertThrows(NullPointerException.class, () -> programUcc.getPrograms());
  }


}

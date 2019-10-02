package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.StudentUcc;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.OptimisticLockException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestStudentUcc {
  StudentDao studentDao;
  Constructor<?> studentDaoConstruct;
  StudentUcc studentUcc;
  Constructor<?> studentUccConstruct;
  BizFactory bizFactory;
  StudentDto studentDto;
  DalServices dalServices;

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
    studentDto = bizFactory.getStudentDto();
    studentDaoConstruct = Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class);
    studentUccConstruct = Class.forName(Config.getValueOfKey(StudentUcc.class.getName()))
        .getConstructor(StudentDao.class, DalServices.class);
  }

  @Test
  @DisplayName("id incorrect")
  final void testId1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(true, false, false);
    studentUcc = (StudentUcc) studentUccConstruct.newInstance(studentDao, dalServices);
    studentDto.setId(0);
    assertThrows(BizException.class, () -> studentUcc.getPersonalData(studentDto.getId(), true));

  }

  @Test
  @DisplayName("userId != studentId")
  final void testId2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(true, false, false);
    studentUcc = (StudentUcc) studentUccConstruct.newInstance(studentDao, dalServices);
    assertThrows(BizException.class, () -> studentUcc.getPersonalData(studentDto.getId(), false));
  }

  @Test
  @DisplayName("test ok")
  final void testGetPersonalData() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    studentUcc = (StudentUcc) studentUccConstruct.newInstance(studentDao, dalServices);
    assertEquals(studentUcc.getPersonalData(studentDto.getId(), true).getId(), studentDto.getId());
  }

  @Test
  @DisplayName("test ok")
  final void testUpdatePersonalData() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    studentUcc = (StudentUcc) studentUccConstruct.newInstance(studentDao, dalServices);
    studentUcc.updatePersonalData(studentDto);
  }


  @Test
  @DisplayName("Optimist test")
  final void testOptimist() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, true, false);
    studentUcc = (StudentUcc) studentUccConstruct.newInstance(studentDao, dalServices);
    assertThrows(OptimisticLockException.class, () -> studentUcc.updatePersonalData(studentDto));
  }

}

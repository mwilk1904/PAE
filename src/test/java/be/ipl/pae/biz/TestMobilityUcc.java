package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.MobilityUcc;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestMobilityUcc {
  MobilityDao mobilityDao;
  StudentDao studentDao;

  Constructor<?> studentDaoConstruct;

  ProgramDao programDao;

  Constructor<?> mobilityDaoConstruct;
  MobilityUcc mobilityUcc;
  Constructor<?> mobilityUccConstruct;
  BizFactory bizFactory;
  MobilityDto mobilityDto;
  UserDto userDto;
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
    mobilityDto = bizFactory.getMobilityDto();
    userDto = bizFactory.getUserDto();

    studentDaoConstruct = Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class);


    studentDao = (StudentDao) Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false);
    programDao = (ProgramDao) Class.forName(Config.getValueOfKey(ProgramDao.class.getName()))
        .getConstructor(boolean.class, boolean.class).newInstance(false, false);
    mobilityDaoConstruct = Class.forName(Config.getValueOfKey(MobilityDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
            boolean.class, boolean.class, boolean.class, boolean.class);
    mobilityUccConstruct = Class.forName(Config.getValueOfKey(MobilityUcc.class.getName()))
        .getConstructor(MobilityDao.class, StudentDao.class, ProgramDao.class, DalServices.class);
  }

  @Test
  @DisplayName("id incorrect")
  final void testGetMobilities1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityDto.setId(0);
    assertThrows(BizException.class, () -> mobilityUcc.getMobilities(mobilityDto.getId(), false));
  }

  @Test
  @DisplayName("")
  final void testGetMobilities2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityDto.setId(0);
    assertThrows(BizException.class, () -> mobilityUcc.getMobilities(mobilityDto.getId(), true));
  }

  @Test
  @DisplayName("")
  final void testGetMobilities3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, true, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(NullPointerException.class,
        () -> mobilityUcc.getMobilities(mobilityDto.getId(), true));
  }

  @Test
  @DisplayName("")
  final void testGetMobilities4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertEquals(mobilityUcc.getMobilities(mobilityDto.getId(), true).get(0).getId(),
        mobilityDto.getId());
  }

  @Test
  @DisplayName("")
  final void testGetMobilities5() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.getMobilities(mobilityDto.getId(), true));

  }

  @Test
  @DisplayName("")
  final void testGetAllMobilities1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertEquals(mobilityUcc.getAllMobilities().get(0).getId(), mobilityDto.getId());
  }

  @Test
  @DisplayName("")
  final void testGetAllMobilities2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.getAllMobilities());

  }

  @Test
  @DisplayName("")
  final void testCancelMobilities() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(true, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);

    assertThrows(BizException.class, () -> mobilityUcc.cancelMobilities(userDto, ""));
  }

  @Test
  @DisplayName("")
  final void testCancelMobilities2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityUcc.cancelMobilities(userDto, "");
  }

  @Test
  @DisplayName("")
  final void testCancelMobilities3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.cancelMobilities(userDto, ""));
  }

  @Test
  @DisplayName("")
  final void testCancelMobility() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    userDto.setRole("student");
    assertThrows(BizException.class,
        () -> mobilityUcc.cancelMobility(userDto, "", mobilityDto.getId()));
  }

  @Test
  @DisplayName("")
  final void testCancelMobility2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    userDto.setRole("teacher");
    mobilityUcc.cancelMobility(userDto, "", mobilityDto.getId());
  }

  @Test
  @DisplayName("")
  final void testCancelMobility3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    userDto.setRole("resp_teacher");
    mobilityUcc.cancelMobility(userDto, "", mobilityDto.getId());
  }

  @Test
  @DisplayName("")
  final void testCancelMobility4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    userDto.setRole("resp_teacher");
    assertThrows(FatalException.class,
        () -> mobilityUcc.cancelMobility(userDto, "", mobilityDto.getId()));
    userDto.setRole("teacher_resp");
    // mobilityUcc.cancelMobility(userDto, "", mobilityDto);

  }

  @Test
  @DisplayName("")
  final void testConfirmMobility() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityDto.setId(0);
    assertThrows(BizException.class, () -> mobilityUcc.confirmMobility(mobilityDto.getId()));

  }

  @Test
  @DisplayName("")
  final void testConfirmMobility2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityUcc.confirmMobility(mobilityDto.getId());

  }

  @Test
  @DisplayName("")
  final void testConfirmMobility3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.confirmMobility(mobilityDto.getId()));

  }

  @Test
  @DisplayName("")
  final void testMobilityChoice() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    setAllTrue(mobilityDto);


    mobilityUcc.setMobilityChoice(mobilityDto);

  }

  @Test
  @DisplayName("")
  final void testMobilityChoice2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityUcc.setMobilityChoice(mobilityDto);

  }

  @Test
  @DisplayName("")
  final void testMobilityChoice3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.setMobilityChoice(mobilityDto));
  }


  @Test
  @DisplayName("")
  final void testGetMobility() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertEquals(mobilityUcc.getMobility(mobilityDto.getId()).getId(), mobilityDto.getId());

  }

  @Test
  @DisplayName("")
  final void testGetMobility2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.getMobility(mobilityDto.getId()));

  }

  @Test
  @DisplayName("")
  final void testUpdateMobility() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    setAllTrue(mobilityDto);
    mobilityUcc.updateMobility(mobilityDto);

  }

  @Test
  @DisplayName("")
  final void testUpdateMobility2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityUcc.updateMobility(mobilityDto);

  }

  @Test
  @DisplayName("")
  final void testUpdateMobility3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.updateMobility(mobilityDto));
  }

  @Test
  @DisplayName("")
  final void testGetCancellationReasons() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertEquals(mobilityUcc.getCancellationReasons().size(), 0);
  }

  @Test
  @DisplayName("")
  final void testGetCancellationReasons2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, true, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);

    assertEquals(mobilityUcc.getCancellationReasons().size(), 1);
  }

  @Test
  @DisplayName("")
  final void testGetCancellationReasons3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, true, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    assertThrows(FatalException.class, () -> mobilityUcc.getCancellationReasons());

  }

  @Test
  @DisplayName("")
  final void testDeclareMobilities() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(true, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    MobilityDto[] mobilities = {mobilityDto};
    assertThrows(BizException.class,
        () -> mobilityUcc.declareMobilities(userDto.getId(), mobilities));
  }

  @Test
  @DisplayName("")
  final void testDeclareMobilities2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    mobilityDto.setMobilityCode(null);
    MobilityDto[] mobilities = {mobilityDto};
    assertThrows(BizException.class,
        () -> mobilityUcc.declareMobilities(userDto.getId(), mobilities));


  }

  @Test
  @DisplayName("")
  final void testDeclareMobilities3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, false);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    MobilityDto[] mobilities = {mobilityDto};
    assertEquals(mobilityUcc.declareMobilities(userDto.getId(), mobilities)[0].getId(),
        mobilityDto.getId());
  }

  @Test
  @DisplayName("")
  final void testDeclareMobilities4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    studentDao = (StudentDao) studentDaoConstruct.newInstance(false, false, false);
    mobilityDao = (MobilityDao) mobilityDaoConstruct.newInstance(false, false, false, false, false,
        false, false, false, true);
    mobilityUcc = (MobilityUcc) mobilityUccConstruct.newInstance(mobilityDao, studentDao,
        programDao, dalServices);
    MobilityDto[] mobilities = {mobilityDto};
    assertThrows(FatalException.class,
        () -> mobilityUcc.declareMobilities(userDto.getId(), mobilities));

  }

  private void setAllTrue(MobilityDto mobilityDto) {
    mobilityDto.setScholarshipContract(true);
    mobilityDto.setInternshipStudyAgreement(true);
    mobilityDto.setStudentChart(true);
    mobilityDto.setCommitmentDoc(true);
    mobilityDto.setPassingLangTestProofGo(true);
    mobilityDto.setTranscripts(true);
    mobilityDto.setStayCertificate(true);
    mobilityDto.setStageCertificate(true);
    mobilityDto.setPassingLangTestProofBack(true);
    mobilityDto.setFinalReport("ok");
  }


}

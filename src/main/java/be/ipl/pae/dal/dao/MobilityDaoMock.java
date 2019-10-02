package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;
import java.util.List;

public class MobilityDaoMock implements MobilityDao {
  private boolean findMobByIdNull;
  private boolean getMobilitiesNull;
  private boolean countMobilitiesByStudentIdWrong;
  private boolean countConfirmedByStudentIdWrong;
  private boolean findVersionNumberFromWrong;
  private boolean findHighestPreferenceOrderNumberWrong;
  private boolean getAllMobilitiesNull;
  private boolean canceledTeacherTest;
  private boolean testFatalException;
  private BizFactory bizFactory;

  /**
   * Crée un objet MobilityDaoMock.
   * 
   * @param findMobByIdNull vérifie si findMobById est null.
   * @param getMobilitiesNull vérifie si getMobilities est null
   * @param countMobilitiesByStudentIdWrong verifie si countMobilities est null.
   * @param countConfirmedByStudentIdWrong vérifie si countUnConfirmed est null.
   * @param findVersionNumberFromWrong vérifie si findVersionNumberFromWrong est mauvais.
   * @param findHighestPreferenceOrderNumberWrong vérifie si findHighestPreferenceOrderNumber est
   *        mauvais.
   * @param getAllMobilitiesNull vérifie si getAllMobilities est null.
   */
  public MobilityDaoMock(boolean findMobByIdNull, boolean getMobilitiesNull,
      boolean countMobilitiesByStudentIdWrong, boolean countConfirmedByStudentIdWrong,
      boolean findVersionNumberFromWrong, boolean findHighestPreferenceOrderNumberWrong,
      boolean getAllMobilitiesNull, boolean canceledTeacherTest, boolean testFatalException) {
    super();
    this.findMobByIdNull = findMobByIdNull;
    this.getMobilitiesNull = getMobilitiesNull;
    this.countMobilitiesByStudentIdWrong = countMobilitiesByStudentIdWrong;
    this.countConfirmedByStudentIdWrong = countConfirmedByStudentIdWrong;
    this.findVersionNumberFromWrong = findVersionNumberFromWrong;
    this.findHighestPreferenceOrderNumberWrong = findHighestPreferenceOrderNumberWrong;
    this.getAllMobilitiesNull = getAllMobilitiesNull;
    this.canceledTeacherTest = canceledTeacherTest;
    this.testFatalException = testFatalException;
    this.bizFactory = new BizFactoryStub();
  }

  @Override
  public MobilityDto findMobById(int mobId) {
    testFatalException();
    if (findMobByIdNull) {
      return null;
    }
    return bizFactory.getMobilityDto();
  }

  @Override
  public void updateMobility(MobilityDto mob) {
    testFatalException();

  }

  @Override
  public void confirmMobility(int mobId) {
    // TODO Auto-generated method stub

  }

  @Override
  public void confirmDocumentReception(MobilityDto mobDto) {
    // TODO Auto-generated method stub

  }

  @Override
  public void insertMobility(MobilityDto mobDto) {
    testFatalException();
    // TODO Auto-generated method stub

  }

  @Override
  public List<MobilityDto> getMobilities(int studentId) {
    testFatalException();
    if (getMobilitiesNull) {
      return null;
    }
    List<MobilityDto> mobilities = new ArrayList<>();
    mobilities.add(bizFactory.getMobilityDto());
    return mobilities;
  }

  @Override
  public int countMobilitiesByStudentId(int studentId) {
    testFatalException();
    if (countMobilitiesByStudentIdWrong) {
      return 0;
    }
    return 1;
  }

  @Override
  public int countUnconfirmedByStudentId(int studentId) {
    testFatalException();
    if (countConfirmedByStudentIdWrong) {
      return 0;
    }
    return 1;
  }

  @Override
  public int findVersionNumberFrom(MobilityDto mobDto) {
    testFatalException();
    if (findVersionNumberFromWrong) {
      return 0;
    }
    return mobDto.getVersionNumber() + 1;
  }

  @Override
  public int findHighestPreferenceOrderNumberForStudent(int studentId) {
    testFatalException();
    if (findHighestPreferenceOrderNumberWrong) {
      return 0;
    }
    return bizFactory.getMobilityDto().getPreferenceOrderNumber();
  }

  @Override
  public List<MobilityDto> getAllMobilities() {
    testFatalException();
    if (getAllMobilitiesNull) {
      return null;
    }
    MobilityDto mobDto = bizFactory.getMobilityDto();
    if (canceledTeacherTest) {
      mobDto.setCancelledByTeacher(true);
    }
    List<MobilityDto> mobilities = new ArrayList<>();
    mobilities.add(mobDto);
    return mobilities;
  }

  private void testFatalException() {
    if (testFatalException) {
      throw new DalException();
    }
  }

}

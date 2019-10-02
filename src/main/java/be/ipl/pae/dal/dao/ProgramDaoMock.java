package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.ProgramDao;

import java.util.ArrayList;
import java.util.List;

public class ProgramDaoMock implements ProgramDao {
  private boolean findAllProgramsNull;
  private boolean findProgramByIdNull;
  private BizFactory bizFactory;

  /**
   * Crée un objet ProgramDaoMock.
   * 
   * @param findAllProgramsNull vérifie si findAllPrograms est null.
   * @param findProgramByIdNull vérifie si findProgramById est null.
   */
  public ProgramDaoMock(boolean findAllProgramsNull, boolean findProgramByIdNull) {
    super();
    this.findAllProgramsNull = findAllProgramsNull;
    this.findProgramByIdNull = findProgramByIdNull;
    this.bizFactory = new BizFactoryStub();
  }

  @Override
  public List<ProgramDto> findAllPrograms() {
    if (findAllProgramsNull) {
      return null;
    }
    List<ProgramDto> programs = new ArrayList<>();
    programs.add(bizFactory.getProgramDto());
    return programs;
  }

  @Override
  public ProgramDto findProgramById(int id) {
    if (findProgramByIdNull) {
      return null;
    }
    return bizFactory.getProgramDto();
  }

}

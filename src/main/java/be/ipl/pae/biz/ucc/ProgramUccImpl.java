package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.interfaces.ProgramUcc;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.Collections;
import java.util.List;

public class ProgramUccImpl implements ProgramUcc {
  private ProgramDao programDao;
  private DalServices dalServices;

  /**
   * Crée un objet ProgramUccImpl.
   * 
   * @param programDao un objet ProgramDao.
   * @param dalServices un objet DalServices.
   */
  public ProgramUccImpl(ProgramDao programDao, DalServices dalServices) {
    super();
    this.programDao = programDao;
    this.dalServices = dalServices;
  }

  @Override
  public List<ProgramDto> getPrograms() {
    Logs.fine("Récupération des programmes");
    List<ProgramDto> programs;
    try {
      dalServices.startTransaction();
      programs = programDao.findAllPrograms();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(programs);
  }

}

package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.interfaces.StudentUcc;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.exceptions.OptimisticLockException;

import utils.Logs;

public class StudentUccImpl implements StudentUcc {
  private StudentDao studentDao;
  private DalServices dalServices;

  /**
   * Crée un objet StudentUccImpl.
   * 
   * @param studentDao un objet StudentDao.
   * @param dalServices un objet DalServices.
   */
  public StudentUccImpl(StudentDao studentDao, DalServices dalServices) {
    super();
    this.studentDao = studentDao;
    this.dalServices = dalServices;
  }

  @Override
  public StudentDto getPersonalData(int id, boolean isStudentId) {
    Logs.fine("Récupération des données personnelles associées à l'id d'"
        + (isStudentId ? "étudiant " : "utilisateur ") + id);
    dalServices.startTransaction();
    StudentDto student;
    if (!isStudentId) {
      int oldId = id;
      id = studentDao.findStudentIdByUserId(id);
      if (id == 0) {
        dalServices.rollback();
        throw new BizException(
            "L'id d'utilisateur " + oldId + " ne correspond à aucun id d'étudiant en db");
      }
    }
    if (id >= 1) {
      try {
        student = studentDao.findStudentById(id);
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
    } else {
      throw new BizException("L'id " + id + " est incorrect", isStudentId ? "id" : null);
    }
    dalServices.commit();
    return student;
  }

  @Override
  public void updatePersonalData(StudentDto studentDto) {
    Logs.fine("Mise à jour des données personnelles de l'étudiant" + studentDto.getId());
    dalServices.startTransaction();
    try {
      Logs.fine("Numéro de version pré-insertion: " + studentDto.getVersionNumber());
      studentDao.updateDataStudent(studentDto);
      if (studentDto.getVersionNumber() + 1 != studentDao.findVersionNumberFrom(studentDto)) {
        dalServices.rollback();
        throw new OptimisticLockException();
      }
      Logs.fine("Numéro de version post-inscription: " + studentDto.getVersionNumber());
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();

  }

}

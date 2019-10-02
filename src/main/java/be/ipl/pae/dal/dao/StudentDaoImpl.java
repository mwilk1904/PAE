package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.exceptions.OptimisticLockException;

import utils.Logs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;

  /**
   * Cree un objet UserDaoImpl.
   * 
   * @param dalBackendServices un objet DalBackendServices.
   * @param bizFactory un objet BizFactory.
   */
  public StudentDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    super();
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }


  @Override
  public void insertDataStudent(StudentDto studentDto) {
    Logs.fine("Insertion de l'utilisateur dans la base de données");
    try {
      PreparedStatement insertNewStudent = dalBackendServices.getPreparedStatement(
          "INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,"
              + "NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,?,1)");
      insertNewStudent.setInt(1, studentDto.getUserId());
      try {
        insertNewStudent.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de l'insertion de l'étudiant.", sqle);
      }
    } catch (SQLException sqle) {
      throw new FatalException("Erreur lors de la préparation de l'insertion de l'étudiant.", sqle);
    }
  }


  @Override
  public void updateDataStudent(StudentDto studentDto) {
    Logs.fine("Mise à jour des informations de l'utilisateur " + studentDto.getId()
        + " dans la base de données");
    try {
      PreparedStatement updateStudent = dalBackendServices.getPreparedStatement(
          "UPDATE projet_ae.students SET title=?,birth_date=?,nationality=?,street=?,"
              + "street_number=?,box=?,zip_code=?," + "commune=?,city=?," + "tel_number=?,sex=?,"
              + "number_high_school_years_succeded=?,bank_card_number=?,"
              + "account_owner=?,bank_name=?,BIC_code=?," + "user_id=?,version_number=?"
              + " WHERE version_number=? AND student_id=? ");
      updateStudent.setString(1, studentDto.getTitle());
      updateStudent.setObject(2, studentDto.getBirthDate(), java.sql.Types.TIMESTAMP);
      updateStudent.setInt(3, studentDto.getNationalityId());
      updateStudent.setString(4, studentDto.getStreet());
      updateStudent.setInt(5, studentDto.getStreetNumber());
      updateStudent.setString(6, studentDto.getBox());
      updateStudent.setInt(7, studentDto.getZipCode());
      updateStudent.setString(8, studentDto.getCommune());
      updateStudent.setString(9, studentDto.getCity());
      updateStudent.setString(10, studentDto.getPhoneNumber());
      if (studentDto.getSex() == ' ') {
        updateStudent.setNull(11, java.sql.Types.CHAR);
      } else {
        updateStudent.setString(11, String.valueOf(studentDto.getSex()));
      }
      updateStudent.setInt(12, studentDto.getNumberHighSchollYearsSucceded());
      updateStudent.setString(13, studentDto.getBankCardNumber());
      updateStudent.setString(14, studentDto.getAccountOwner());
      updateStudent.setString(15, studentDto.getBankName());
      updateStudent.setString(16, studentDto.getBicCode());
      updateStudent.setInt(17, studentDto.getUserId());
      updateStudent.setInt(18, studentDto.getVersionNumber() + 1);
      updateStudent.setInt(19, studentDto.getVersionNumber());
      updateStudent.setInt(20, studentDto.getId());
      try {
        updateStudent.executeUpdate();
      } catch (SQLException sqle) {
        throw new OptimisticLockException("Erreur lors de la mise à jour de l'étudiant.", sqle);
      }
    } catch (SQLException sqle) {
      throw new FatalException("Erreur lors de la préparation de la mise à jour de l'étudiant.",
          sqle);
    }
  }



  @Override
  public int findStudentIdByUserId(int userId) {
    try {
      PreparedStatement findStudentIdByUserId = dalBackendServices
          .getPreparedStatement("SELECT student_id FROM projet_ae.students WHERE user_id=?");
      findStudentIdByUserId.setInt(1, userId);
      try (ResultSet rs = findStudentIdByUserId.executeQuery()) {
        if (rs.next()) {
          Logs.fine("Id étudiant associé à l'id d'utilisateur " + userId + ": " + rs.getInt(1));
          return rs.getInt(1);
        }
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
    return 0;
  }

  /**
   * trouve le numéro de version de la mobilité.
   * 
   * @param studentDto l'étudiant correspondant.
   * @return le numéro de version correspond, 0 sinon.
   */
  public int findVersionNumberFrom(StudentDto studentDto) {
    try {
      PreparedStatement findVersionNumberFrom = dalBackendServices
          .getPreparedStatement("SELECT version_number FROM projet_ae.students WHERE student_id=?");
      findVersionNumberFrom.setInt(1, studentDto.getId());
      try (ResultSet rs = findVersionNumberFrom.executeQuery()) {
        if (rs.next()) {
          Logs.fine(
              "Numéro de version de l'utilisateur " + studentDto.getId() + ": " + rs.getInt(1));
          return rs.getInt(1);
        }
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
    return 0;
  }


  @Override
  public StudentDto findStudentById(int studentId) {
    try {
      PreparedStatement findStudentById = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.students WHERE student_id=?");
      findStudentById.setInt(1, studentId);
      return getPreparedStatementResult(findStudentById);
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
  }

  private StudentDto getPreparedStatementResult(PreparedStatement statement) throws SQLException {
    StudentDto studentToComplete = bizFactory.getStudentDto();
    try (ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        Logs.fine("Récupération de l'instance StudentDto associée à l'étudiant " + rs.getInt(1)
            + " dans la base de données");
        studentToComplete.setId(rs.getInt(1));
        studentToComplete.setTitle(rs.getString(2));
        studentToComplete.setBirthDate(rs.getDate(3) == null ? null : rs.getDate(3));
        studentToComplete.setNationalityId(rs.getInt(4));
        studentToComplete.setStreet(rs.getString(5));
        studentToComplete.setStreetNumber(rs.getInt(6));
        studentToComplete.setBox(rs.getString(7));
        studentToComplete.setZipCode(rs.getInt(8));
        studentToComplete.setCommune(rs.getString(9));
        studentToComplete.setCity(rs.getString(10));
        studentToComplete.setPhoneNumber(rs.getString(11));
        studentToComplete.setSex(rs.getString(12) == null ? ' ' : rs.getString(12).charAt(0));
        studentToComplete.setNumberHighSchollYearsSucceded(rs.getInt(13));
        studentToComplete.setBankCardNumber(rs.getString(14));
        studentToComplete.setAccountOwner(rs.getString(15));
        studentToComplete.setBankName(rs.getString(16));
        studentToComplete.setBicCode(rs.getString(17));
        studentToComplete.setUser(rs.getInt(18));
        studentToComplete.setVersionNumber(rs.getInt(19));
        return studentToComplete;
      }
    }
    return null;
  }


  @Override
  public List<StudentDto> findStudents() {
    try {
      PreparedStatement findStudents =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.students");
      try (ResultSet rs = findStudents.executeQuery()) {
        List<StudentDto> students = new ArrayList<StudentDto>();
        while (rs.next()) {
          Logs.fine("Récupération de l'instance StudentDto associée à l'étudiant " + rs.getInt(1)
              + " dans la base de données");
          StudentDto student = bizFactory.getStudentDto();
          student.setId(rs.getInt(1));
          student.setTitle(rs.getString(2));
          student.setBirthDate(rs.getDate(3) == null ? null : rs.getDate(3));
          student.setNationalityId(rs.getInt(4));
          student.setStreet(rs.getString(5));
          student.setStreetNumber(rs.getInt(6));
          student.setBox(rs.getString(7));
          student.setZipCode(rs.getInt(8));
          student.setCommune(rs.getString(9));
          student.setCity(rs.getString(10));
          student.setPhoneNumber(rs.getString(11));
          student.setSex(rs.getString(12) == null ? ' ' : rs.getString(12).charAt(0));
          student.setNumberHighSchollYearsSucceded(rs.getInt(13));
          student.setBankCardNumber(rs.getString(14));
          student.setAccountOwner(rs.getString(15));
          student.setBankName(rs.getString(16));
          student.setBicCode(rs.getString(17));
          student.setUser(rs.getInt(18));
          student.setVersionNumber(rs.getInt(19));
          students.add(student);
        }
        return Collections.unmodifiableList(students);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findStudents", sqle);
    }
  }

}

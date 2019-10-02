package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.UserDao;
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

public class UserDaoImpl implements UserDao {

  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;

  /**
   * Cree un objet UserDaoImpl.
   * 
   * @param dalBackendServices un objet DalBackendServices.
   * @param bizFactory un objet BizFactory
   */
  public UserDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    super();
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }

  @Override
  public void insertNewUser(UserDto userDto) {
    Logs.fine("Insertion de l'utilisateur dans la base de données");
    try {
      PreparedStatement insertNewUser = dalBackendServices
          .getPreparedStatement("INSERT INTO projet_ae.users VALUES(DEFAULT,?,?,?,?,?,?,?,1)");
      insertNewUser.setString(1, userDto.getPseudo());
      insertNewUser.setString(2, userDto.getLastName());
      insertNewUser.setString(3, userDto.getFirstName());
      insertNewUser.setString(4, userDto.getEmail());
      insertNewUser.setString(5, userDto.getPassword());
      insertNewUser.setString(6, userDto.getRole());
      insertNewUser.setObject(7, userDto.getRecordDate(), java.sql.Types.TIMESTAMP);
      try {
        insertNewUser.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de l'insertion de l'utilisateur.", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException(
          "Erreur lors de la préparation de la requête SQL d'insertion de l'utilisateur.", sqle);
    }
  }

  @Override
  public UserDto findUserByPseudo(String pseudo) {

    try {
      PreparedStatement findUserByPseudo =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.users WHERE pseudo=?");
      findUserByPseudo.setString(1, pseudo);
      try (ResultSet rs = findUserByPseudo.executeQuery()) {
        if (rs.next()) {
          UserDto user = populateUserDto(rs);
          return user;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findUserByPseudo", sqle);
    }
  }

  @Override
  public UserDto findUserByEmail(String email) {
    try {
      PreparedStatement findUserByEmail =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.users WHERE email=?");
      findUserByEmail.setString(1, email);
      try (ResultSet rs = findUserByEmail.executeQuery()) {
        if (rs.next()) {
          UserDto user = populateUserDto(rs);
          return user;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findUserByEmail", sqle);
    }
  }

  @Override
  public UserDto findUserById(int userId) {
    try {
      PreparedStatement findUserById =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.users WHERE user_id=?");
      findUserById.setInt(1, userId);
      try (ResultSet rs = findUserById.executeQuery()) {
        if (rs.next()) {
          UserDto user = populateUserDto(rs);
          return user;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findUserByPseudo", sqle);
    }
  }

  @Override
  public UserDto findRespTeacher() {
    try {
      PreparedStatement findRespTeacher = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.users WHERE role='resp_teacher'");
      try (ResultSet rs = findRespTeacher.executeQuery()) {
        if (rs.next()) {
          UserDto respTeacher = populateUserDto(rs);
          return respTeacher;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
  }

  private UserDto populateUserDto(ResultSet rs) throws SQLException {
    UserDto userToComplete = bizFactory.getUserDto();
    Logs.fine("Récupération de l'instance UserDto associée à l'utilisateur" + rs.getInt(1)
        + " dans la base de données");
    userToComplete.setId(rs.getInt(1));
    userToComplete.setPseudo(rs.getString(2));
    userToComplete.setLastName(rs.getString(3));
    userToComplete.setFirstName(rs.getString(4));
    userToComplete.setEmail(rs.getString(5));
    userToComplete.setPassword(rs.getString(6));
    userToComplete.setRole(rs.getString(7));
    userToComplete.setRecordDate(rs.getDate(8));
    userToComplete.setVersionNumber(rs.getInt(9));
    return userToComplete;
  }

  @Override
  public List<UserDto> findUsers() {
    try {
      PreparedStatement findUsers =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.users");
      try (ResultSet rs = findUsers.executeQuery()) {
        List<UserDto> users = new ArrayList<UserDto>();
        while (rs.next()) {
          UserDto userDto = populateUserDto(rs);
          users.add(userDto);
        }
        return Collections.unmodifiableList(users);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de selectCountriesOrderByProgram", sqle);
    }
  }

  @Override
  public List<UserDto> findStudents() {
    try {
      PreparedStatement findStudents = dalBackendServices.getPreparedStatement(
          "SELECT * FROM projet_ae.users WHERE role='student' ORDER BY first_name");
      try (ResultSet rs = findStudents.executeQuery()) {
        List<UserDto> students = new ArrayList<UserDto>();
        while (rs.next()) {
          UserDto userDto = populateUserDto(rs);
          students.add(userDto);
        }

        return Collections.unmodifiableList(students);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
  }

  @Override
  public void updateUser(UserDto userDto) {
    Logs.fine("Mise à jour des informations de l'utilisateur (updateUser) " + userDto.getId()
        + " dans la base de données");
    try {
      PreparedStatement updateUser = dalBackendServices
          .getPreparedStatement("UPDATE projet_ae.users SET role=?, version_number=?"
              + "WHERE version_number=? AND user_id=? ");
      updateUser.setString(1, userDto.getRole());
      updateUser.setInt(2, userDto.getVersionNumber() + 1);
      updateUser.setInt(3, userDto.getVersionNumber());
      updateUser.setInt(4, userDto.getId());
      try {
        updateUser.executeUpdate();
      } catch (SQLException sqle) {
        throw new OptimisticLockException("Erreur lors de la mise à jour du user.", sqle);
      }

    } catch (SQLException sqle) {
      throw new FatalException("Erreur lors de la préparation de la mise a jour du user", sqle);
    }

  }

}

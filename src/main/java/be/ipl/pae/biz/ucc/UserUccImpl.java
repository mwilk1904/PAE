package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.User;
import be.ipl.pae.biz.interfaces.UserUcc;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserUccImpl implements UserUcc {
  private UserDao userDao;
  private BizFactory bizFactory;
  private boolean respTeacherExists = false;
  private DalServices dalServices;
  private StudentDao studentDao;

  /**
   * crée un objet UserUccImpl
   * 
   * @param bizFactory un objet BizFactory.
   * @param userDao un objet UserDao.
   * @param dalServices un objet DalServices.
   * @param studentDao un objet StudentDao.
   */
  public UserUccImpl(BizFactory bizFactory, UserDao userDao, DalServices dalServices,
      StudentDao studentDao) {
    super();
    this.userDao = userDao;
    this.bizFactory = bizFactory;
    this.dalServices = dalServices;
    this.studentDao = studentDao;
  }


  @Override
  public UserDto signUp(UserDto userDto) {
    User user = (User) userDto;
    Logs.fine("Vérification de la validité des données d'inscription");
    if (user.checkUser()) {
      if (!user.checkEmail()) {
        throw new BizException(
            "Format prenom.nom@vinci.be ou prenom.nom@student.vinci.be uniquement.", "email");
      }
      if (!user.encryptPassword()) {
        throw new FatalException("Erreur lors de l'encryption du mot de passe.");
      }
      dalServices.startTransaction();
      try {
        // Pseudo deja utilise
        if (userDao.findUserByPseudo(user.getPseudo()) != null) {
          dalServices.commit();
          throw new BizException("Pseudo déjà utilisé.", "pseudo");
        }
        // Email deja utilise
        if (userDao.findUserByEmail(user.getEmail()) != null) {
          dalServices.commit();
          throw new BizException("Adresse e-mail déjà utilisée.", "email");
        }
        // Si aucun professeur responsable n'existe, cet utilisateur
        // devient le professeur responsable
        respTeacherExists = respTeacherExists || userDao.findRespTeacher() != null;
        Logs.fine("Un professeur responsable "
            + (respTeacherExists ? "existe déjà" : "n'existe pas encore")
            + " dans la base de données");
        user.setRole(respTeacherExists ? "student" : "resp_teacher");
        user.setRecordDate(new Date());
        userDao.insertNewUser(user);
        UserDto insertedUser = userDao.findUserByPseudo(user.getPseudo());
        if (respTeacherExists) {
          StudentDto studentData = bizFactory.getStudentDto();
          studentData.setUser(insertedUser.getId());
          studentDao.insertDataStudent(studentData);
        }
        // On garde en mémoire qu'un professeur responsable existe afin
        // de ne pas faire de requête inutile à la base de données
        respTeacherExists = true;
        dalServices.commit();
        return insertedUser;
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
    } else {
      throw new BizException("Certaines données n'ont pas été spécifiées.");
    }
  }

  @Override
  public UserDto authentification(int id) {
    Logs.fine("Authentification de l'utilisateur " + id);
    UserDto userDb;
    if (id >= 1) {
      try {
        dalServices.startTransaction();
        userDb = userDao.findUserById(id);
        if (userDb == null) {
          dalServices.commit();
          throw new BizException("Aucun utilisateur ne correspond à l'id. " + id, "id");
        }
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
      dalServices.commit();
      return userDb;
    } else {
      throw new BizException("L'id d'utilisateur " + id + " n'est pas correct.", "id");
    }
  }

  @Override
  public UserDto login(String pseudo, String password) {
    Logs.fine("Connection de " + pseudo + " avec le mot de passe " + password);
    User user = (User) bizFactory.getUserDto();
    user.setPseudo(pseudo);
    user.setPassword(password);
    Logs.fine("Vérification de la validité des données de connection");
    if (user.checkUser()) {
      UserDto userDb;
      try {
        dalServices.startTransaction();
        userDb = userDao.findUserByPseudo(user.getPseudo());
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
      dalServices.commit();
      if (userDb == null) {
        throw new BizException("Pseudo incorrect", "pseudo");
      }
      if (!user.checkPassword(userDb.getPassword())) {
        throw new BizException("Mot de passe incorrect.", "password");
      }
      userDb.setPassword(null);
      return userDb;
    } else {
      throw new BizException("Certaines données n'ont pas été spécifiées.");
    }
  }

  @Override
  public List<UserDto> getUsers() {
    Logs.fine("Récupération des utilisateurs");
    List<UserDto> users;
    try {
      dalServices.startTransaction();
      users = userDao.findUsers();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(users);
  }

  @Override
  public void setTeacher(int userId) {
    Logs.fine("Changement de rôle d'un étudiant vers professeur");
    try {
      dalServices.startTransaction();
      UserDto user = userDao.findUserById(userId);
      user.setRole("teacher");
      userDao.updateUser(user);
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
  }


  @Override
  public List<UserDto> getUsersWithStudentRole() {
    Logs.fine("Récupération des utilisateurs étant des étudiants");
    List<UserDto> users;
    try {
      dalServices.startTransaction();
      users = userDao.findStudents();
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(users);
  }


}

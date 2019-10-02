package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.exceptions.DalException;

import java.util.ArrayList;
import java.util.List;

public class UserDaoMock implements UserDao {

  private boolean findUserByPseudoNull;
  private boolean findUserByPseudoPseudoNull;
  private boolean findUserByEmailNull;
  private boolean findUserByRoleNull;
  private boolean findUserByIdNull;
  private boolean findUsersNull;
  private boolean findStudentsNull;
  private boolean testDalException;
  private BizFactory bizFactory;

  /**
   * Cree un objet UserDaoMock.
   * 
   * @param findUserByPseudoNull verifie si findUserByPseudo est nul
   * @param findUserByEmailNull verifie si findUserByEmail est nul
   * @param findUserByRoleNull verifie si findUserByRole est nul
   * @param findUserByPseudoPseudoNull verifie si findUserByPseudoPseudo est nul
   * @param findUserByIdNull verifie si findUserById est nul
   */
  public UserDaoMock(boolean findUserByPseudoNull, boolean findUserByEmailNull,
      boolean findUserByRoleNull, boolean findUserByPseudoPseudoNull, boolean findUserByIdNull,
      boolean findUsersNull, boolean findStudentsNull, boolean testDalException) {
    this.findUserByPseudoNull = findUserByPseudoNull;
    this.findUserByEmailNull = findUserByEmailNull;
    this.findUserByRoleNull = findUserByRoleNull;
    this.findUserByPseudoPseudoNull = findUserByPseudoPseudoNull;
    this.findUserByIdNull = findUserByIdNull;
    this.findUsersNull = findUsersNull;
    this.findStudentsNull = findStudentsNull;
    this.testDalException = testDalException;
    this.bizFactory = new BizFactoryStub();
  }


  @Override
  public void insertNewUser(UserDto userDto) {
    // return 0;
  }

  @Override
  public UserDto findUserByPseudo(String pseudo) {
    testDalException();
    if (findUserByPseudoNull) {
      return null;
    } else {
      if (findUserByPseudoPseudoNull) {
        UserDto user = bizFactory.getUserDto();
        user.setPseudo(null);
        return user;
      } else {
        UserDto user = bizFactory.getUserDto();
        /*
         * String salt = BCrypt.gensalt(); String hashPassword = BCrypt.hashpw(user.getPassword(),
         * salt); user.setPassword(hashPassword);
         */
        return user;
      }

    }
  }

  @Override
  public UserDto findUserById(int userId) {
    testDalException();
    if (findUserByIdNull) {
      return null;
    } else {
      return bizFactory.getUserDto();
    }
  }

  @Override
  public UserDto findUserByEmail(String email) {
    testDalException();
    if (findUserByEmailNull) {
      return null;
    } else {
      return bizFactory.getUserDto();
    }
  }

  @Override
  public UserDto findRespTeacher() {
    testDalException();
    if (findUserByRoleNull) {
      return null;
    } else {
      return bizFactory.getUserDto();
    }
  }

  @Override
  public List<UserDto> findUsers() {
    testDalException();
    List<UserDto> users = new ArrayList<UserDto>();
    users.add(bizFactory.getUserDto());
    if (findUsersNull) {
      return null;
    }
    return users;
  }


  @Override
  public void updateUser(UserDto user) {

  }


  @Override
  public List<UserDto> findStudents() {
    testDalException();
    List<UserDto> users = new ArrayList<UserDto>();
    users.add(bizFactory.getUserDto());
    if (findStudentsNull) {
      return null;
    }
    return users;
  }

  private void testDalException() {
    if (testDalException) {
      throw new DalException();
    }
  }

}

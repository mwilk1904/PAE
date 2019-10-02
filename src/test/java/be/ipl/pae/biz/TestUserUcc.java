package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.UserUcc;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.bcrypt.BCrypt;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestUserUcc {
  UserDao userDao;
  Constructor<?> userDaoConstruct;
  UserUcc userUcc;
  Constructor<?> userUccConstruct;
  StudentDao studentDao;
  BizFactory bizFactory;
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
    userDto = bizFactory.getUserDto();
    String salt = BCrypt.gensalt();
    String hashPassword = BCrypt.hashpw(userDto.getPassword(), salt);
    userDto.setPassword(hashPassword);

    studentDao = (StudentDao) Class.forName(Config.getValueOfKey(StudentDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class)
        .newInstance(false, false, false);

    userDaoConstruct = Class.forName(Config.getValueOfKey(UserDao.class.getName())).getConstructor(
        boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class,
        boolean.class, boolean.class);
    userUccConstruct = Class.forName(Config.getValueOfKey(UserUcc.class.getName()))
        .getConstructor(BizFactory.class, UserDao.class, DalServices.class, StudentDao.class);

  }

  @Test
  @DisplayName("Pseudo incorrect")
  final void testLogin1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(true, false, false, false, false, false, false,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(BizException.class,
        () -> userUcc.login(userDto.getPseudo(), userDto.getPassword()));
  }

  @Test
  @DisplayName("Mot de passe null")
  final void testLogin2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto.setPassword(null);
    assertThrows(BizException.class,
        () -> userUcc.login(userDto.getPseudo(), userDto.getPassword()));
  }

  @Test
  @DisplayName("user null")
  final void testLogin3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto = null;
    assertThrows(BizException.class, () -> userUcc.login(null, null));
  }

  @Test
  @DisplayName("")
  final void testLogin4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(FatalException.class,
        () -> userUcc.login(userDto.getPseudo(), userDto.getPassword()));
  }

  @Test
  @DisplayName("Pseudo deja existant")
  final void testSignUp1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(BizException.class, () -> userUcc.signUp(userDto));
  }

  @Test
  @DisplayName("Email deja existant")
  final void testSignUp2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(true, false, false, false, false, false, false,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(BizException.class, () -> userUcc.signUp(userDto));
  }

  @Test
  @DisplayName("Format de l'email incorrect")
  final void testSignUp3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(true, true, false, false, false, false, false,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto.setEmail("admin@ipl.be");
    assertThrows(BizException.class, () -> userUcc.signUp(userDto));
  }

  @Test
  @DisplayName("")
  final void testSignUp4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao =
        (UserDao) userDaoConstruct.newInstance(true, true, true, false, false, false, false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto.setEmail("hedi.mzoughi@student.vinci.be");

    userUcc.signUp(userDto);

  }

  @Test
  @DisplayName("")
  final void testSignUp5() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao =
        (UserDao) userDaoConstruct.newInstance(true, true, true, false, false, false, false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto.setEmail("hedi.mzoughi@student.vinci.be");
    assertThrows(FatalException.class, () -> userUcc.signUp(userDto));
  }

  @Test
  @DisplayName("Id = 0")
  final void testAuthentification1() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userDto.setId(0);
    assertThrows(BizException.class, () -> userUcc.authentification(userDto.getId()));
  }

  @Test
  @DisplayName("Id = 0")
  final void testAuthentification2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, true, false, false,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(BizException.class, () -> userUcc.authentification(userDto.getId()));
  }

  @Test
  @DisplayName("testOk")
  final void testAuthentification3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertEquals(userUcc.authentification(userDto.getId()).getId(), userDto.getId());
  }

  @Test
  @DisplayName("")
  final void testAuthentification4() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(FatalException.class, () -> userUcc.authentification(userDto.getId()));
  }

  @Test
  @DisplayName("users null")
  final void testGetUsers() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, true, false,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(NullPointerException.class, () -> userUcc.getUsers());
  }

  @Test
  @DisplayName("users ok")
  final void testGetUsers2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertEquals(userUcc.getUsers().get(0).getPseudo(), userDto.getPseudo());
  }

  @Test
  @DisplayName("")
  final void testGetUsers3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(FatalException.class, () -> userUcc.getUsers());
  }

  @Test
  @DisplayName("users null")
  final void testGetUsersWithStudentRole() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false, true,
        false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(NullPointerException.class, () -> userUcc.getUsersWithStudentRole());
  }

  @Test
  @DisplayName("students ok")
  final void testGetUsersWithStudentRole2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertEquals(userUcc.getUsersWithStudentRole().get(0).getPseudo(), userDto.getPseudo());
  }

  @Test
  @DisplayName("")
  final void testGetUsersWithStudentRole3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(FatalException.class, () -> userUcc.getUsersWithStudentRole());
  }

  @Test
  @DisplayName("test Teacher")
  final void testSetTeacher() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, false);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    userUcc.setTeacher(userDto.getId());
  }

  @Test
  @DisplayName("")
  final void testSetTeacher2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    userDao = (UserDao) userDaoConstruct.newInstance(false, false, false, false, false, false,
        false, true);
    userUcc = (UserUcc) userUccConstruct.newInstance(bizFactory, userDao, dalServices, studentDao);
    assertThrows(FatalException.class, () -> userUcc.setTeacher(userDto.getId()));
  }


}

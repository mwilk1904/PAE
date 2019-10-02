package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.impl.UserImpl;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.bcrypt.BCrypt;

import utils.Config;

import java.io.IOException;

class TestUser {
  BizFactory bizFactory;
  User user;

  {
    try {
      Config.init("tests.properties");
    } catch (IOException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  @BeforeEach
  void setUp() throws Exception {
    bizFactory = (BizFactory) Class.forName(Config.getValueOfKey(BizFactory.class.getName()))
        .getConstructor().newInstance();
    user = (UserImpl) bizFactory.getUserDto();

  }

  @Test
  final void testGetRole1() {
    user.setRole(null);
    assertNull(user.getRole());
  }

  @Test
  final void testGetRole2() {
    assertNull(user.getRole());
  }

  @Test
  final void testGetRole3() {
    user.setRole("student");
    assertTrue(user.getRole().equals("student"));
  }

  @Test
  final void testCheckUser1() {
    user.setPseudo(null);
    assertFalse(user.checkUser());
  }

  @Test
  final void testCheckUser2() {
    user.setPassword(null);
    assertFalse(user.checkUser());
  }

  @Test
  final void testCheckEmail1() {
    assertFalse(user.checkEmail());
  }

  @Test
  final void testCheckEmail2() {
    user.setEmail("user.test@student.vinci.be");
    assertTrue(user.checkEmail());
  }

  @Test
  final void testCheckEmail3() {
    user.setEmail("user.test@vinci.be");
    assertTrue(user.checkEmail());
  }

  @Test
  final void testCheckPassword() {
    String salt = BCrypt.gensalt();
    String hashPswrd = BCrypt.hashpw(user.getPassword(), salt);
    assertTrue(user.checkPassword(hashPswrd));
  }

  @Test
  final void testGetters() {
    assertAll(() -> assertEquals(1, user.getId(), "la methode getId" + " ne renvoit pas le bon id"),
        () -> assertEquals("TestPseudo", user.getPseudo(),
            "la methode getPseudo " + "ne renvoit pas le bon pseudo"),
        () -> assertEquals("TestPassword", user.getPassword(),
            "la methode getPassword ne " + "renvoit pas le bon mot de passe"),

        () -> assertEquals("TestLastName", user.getLastName(),
            "la methode " + "getLastName ne renvoit pas le bon nom"),

        () -> assertEquals("TestFirstName", user.getFirstName(),
            "la methode getFirstName" + "ne renvoit pas le bon prenom "),
        () -> assertEquals("TestEmail", user.getEmail(),
            "la methode getEmail ne renvoit pas le bon email"));
  }

}

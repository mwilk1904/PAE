package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.impl.StudentImpl;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.sql.Date;

class TestStudent {
  BizFactory bizFactory;
  Student student;

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
    student = (StudentImpl) bizFactory.getStudentDto();

  }

  @Test
  final void testCheckTitle() {
    student.setTitle(null);
    assertFalse(student.checkTitle());
  }

  @Test
  final void testCheckTitle2() {
    student.setTitle("M.");
    assertTrue(student.checkTitle());
  }

  @Test
  final void testCheckTitle3() {
    student.setTitle("Mlle");
    assertTrue(student.checkTitle());
  }

  @Test
  final void testCheckTitle4() {
    student.setTitle("Mme");
    assertTrue(student.checkTitle());
  }

  @Test
  final void testCheckTitle5() {
    student.setTitle("");
    assertFalse(student.checkTitle());
  }

  @Test
  final void testCheckTitle6() {
    student.setTitle("Mr.");
    assertFalse(student.checkTitle());
  }

  @Test
  final void testCheckSex() {
    student.setSex((char) 0);
    assertFalse(student.checkSex());
  }

  @Test
  final void testCheckSex2() {
    student.setSex('N');
    assertFalse(student.checkSex());
  }

  @Test
  final void testCheckSex3() {
    student.setSex('M');
    assertTrue(student.checkSex());
  }

  @Test
  final void testCheckSex4() {
    student.setSex('F');
    assertTrue(student.checkSex());
  }

  @Test
  final void testCheckBirthDate() {
    student.setBirthDate(null);
    assertFalse(student.checkBirthDate());
  }

  @Test
  final void testCheckBirthDate2() {
    Date date = Date.valueOf("2004-06-11");
    student.setBirthDate(date);
    assertFalse(student.checkBirthDate());
  }

  @Test
  final void testCheckBirthDate3() {
    Date date = Date.valueOf("1850-06-11");
    student.setBirthDate(date);
    assertFalse(student.checkBirthDate());
  }

  @Test
  final void testCheckBirthDate4() {
    Date date = Date.valueOf("1997-06-11");
    student.setBirthDate(date);
    assertTrue(student.checkBirthDate());
  }

}

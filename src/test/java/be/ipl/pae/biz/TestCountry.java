package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.impl.CountryImpl;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.Country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;

public class TestCountry {
  BizFactory bizFactory;
  Country country;

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
    country = (CountryImpl) bizFactory.getCountryDto();

  }

  @Test
  final void testCheckProgram() {
    country.setProgramId(2);
    assertTrue(country.checkProgram());
  }

  @Test
  final void testCheckProgram2() {
    country.setProgramId(3);
    assertTrue(country.checkProgram());
  }

  @Test
  final void testCheckProgram3() {
    country.setProgramId(1);
    assertTrue(country.checkProgram());
  }

  // @Test
  // final void testCheckProgram4() {
  // country.setProgramId(null);
  // assertFalse(country.checkProgram());
  // }
  //
  // @Test
  // final void testCheckProgram5() {
  // country.setProgramId("");
  // assertFalse(country.checkProgram());
  // }
  //
  // @Test
  // final void testCheckProgram6() {
  // country.setProgramId("eramu");
  // assertFalse(country.checkProgram());
  // }

}

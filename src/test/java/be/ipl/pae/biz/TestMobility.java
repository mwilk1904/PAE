package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.ipl.pae.biz.impl.MobilityImpl;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.Mobility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.util.Date;

public class TestMobility {
  BizFactory bizFactory;
  Mobility mobility;

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
    mobility = (MobilityImpl) bizFactory.getMobilityDto();

  }

  @Test
  final void testCheckMobility() {
    mobility.setMobilityCode(null);
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility2() {
    mobility.setApplicationNumber(-1);;
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility3() {
    mobility.setPreferenceOrderNumber(0);
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility4() {
    mobility.setSemester(0);
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility5() {
    mobility.setPartnerId(0);
    mobility.setCountryId(0);
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility6() {
    mobility.setIntroductionDate(null);
    assertFalse(mobility.checkMobility());
  }

  @Test
  final void testCheckMobility8() {
    mobility.setApplicationNumber(1);
    mobility.setPreferenceOrderNumber(1);
    mobility.setMobilityCode("SMS");
    mobility.setSemester(1);
    mobility.setPartnerId(1);
    mobility.setCountryId(1);
    mobility.setIntroductionDate(new Date());
    assertTrue(mobility.checkMobility());
  }

  @Test
  final void testGetMobilityCode() {
    mobility.setMobilityCode(null);
    assertNull(mobility.getMobilityCode());
  }

  @Test
  final void testGetMobilityCode2() {
    mobility.setMobilityCode("sms");
    assertNull(mobility.getMobilityCode());
  }

  @Test
  final void testGetMobilityCode3() {
    mobility.setMobilityCode("SMS");
    assertTrue(mobility.getMobilityCode().equals("SMS"));
  }

  @Test
  final void testGetMobilityCode4() {
    mobility.setMobilityCode("SMP");
    assertTrue(mobility.getMobilityCode().equals("SMP"));
  }

}

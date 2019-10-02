package be.ipl.pae.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.PartnerUcc;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Config;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class TestPartnerUcc {
  PartnerDao partnerDao;
  Constructor<?> partnerDaoConstruct;
  PartnerUcc partnerUcc;
  Constructor<?> partnerUccConstruct;
  BizFactory bizFactory;
  PartnerDto partnerDto;
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
    partnerDto = bizFactory.getPartnerDto();
    partnerDaoConstruct = Class.forName(Config.getValueOfKey(PartnerDao.class.getName()))
        .getConstructor(boolean.class, boolean.class, boolean.class, boolean.class);
    partnerUccConstruct = Class.forName(Config.getValueOfKey(PartnerUcc.class.getName()))
        .getConstructor(PartnerDao.class, DalServices.class);
  }

  @Test
  final void testGetPartners() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, false);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    assertEquals(partnerUcc.getPartners().get(0).getId(), partnerDto.getId());
    assertEquals(partnerUcc.searchPartners("").get(0).getId(), partnerDto.getId());
  }

  @Test
  final void testGetPartners2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, true);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    assertThrows(FatalException.class, () -> partnerUcc.getPartners());
    assertThrows(FatalException.class, () -> partnerUcc.searchPartners(""));
  }

  @Test
  final void testInsertPartner() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, false);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    assertThrows(BizException.class, () -> partnerUcc.insertPartner(partnerDto));
  }

  @Test
  final void testInsertPartner2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, true, false, false);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    partnerUcc.insertPartner(partnerDto);
  }

  @Test
  final void testInsertPartner3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, true, false, true);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    assertThrows(FatalException.class, () -> partnerUcc.insertPartner(partnerDto));
  }

  @Test
  final void testGetPartnerData() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, false);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    partnerDto.setId(0);
    assertThrows(BizException.class, () -> partnerUcc.getPartnerData(partnerDto.getId()));
  }

  @Test
  final void testGetPartnerData2() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, false);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    partnerUcc.getPartnerData(partnerDto.getId());
  }

  @Test
  final void testGetPartnerData3() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    partnerDao = (PartnerDao) partnerDaoConstruct.newInstance(false, false, false, true);
    partnerUcc = (PartnerUcc) partnerUccConstruct.newInstance(partnerDao, dalServices);
    assertThrows(FatalException.class, () -> partnerUcc.getPartnerData(partnerDto.getId()));
  }
}

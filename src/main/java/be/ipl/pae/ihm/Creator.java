package be.ipl.pae.ihm;

import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.biz.interfaces.CountryUcc;
import be.ipl.pae.biz.interfaces.MobilityAllUcc;
import be.ipl.pae.biz.interfaces.MobilityPostulantUcc;
import be.ipl.pae.biz.interfaces.MobilityUcc;
import be.ipl.pae.biz.interfaces.PartnerUcc;
import be.ipl.pae.biz.interfaces.ProgramUcc;
import be.ipl.pae.biz.interfaces.StudentUcc;
import be.ipl.pae.biz.interfaces.UserUcc;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.dal.services.DalServices;

import utils.Config;
import utils.Logs;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

public class Creator {

  private BizFactory bizFactory;
  private UserUcc userUcc;
  private MobilityPostulantUcc mobilityPostulantUcc;
  private MobilityAllUcc mobilityAllUcc;
  private UserDao userDao;
  private StudentDao studentDao;
  private StudentUcc studentUcc;
  private MobilityDao mobilityDao;
  private MobilityUcc mobilityUcc;
  private PartnerDao partnerDao;
  private PartnerUcc partnerUcc;
  private CountryDao countryDao;
  private CountryUcc countryUcc;
  private ProgramDao programDao;
  private ProgramUcc programUcc;
  private DalServices dalServices;
  private DalBackendServices dalBackendServices;
  private Map<String, Object> map = new HashMap<String, Object>();
  private HttpServlet servlet;

  /**
   * Crée un objet Creator.
   * 
   * @throws InstantiationException si un problème est rencontré lors de l'instanciation d'un objet
   * @throws IllegalAccessException si la méthode n'a pas accès à une des définitions liées à
   *         l'instance à créer
   * @throws IllegalArgumentException si une des méthodes reçoit un argument illégal ou inapproprié
   * @throws InvocationTargetException si une exception a été rencontrée par une méthode invoquée ou
   *         par un constructeur
   * @throws NoSuchMethodException si une méthode est introuvable
   * @throws SecurityException si il y a une violation de sécurité
   * @throws ClassNotFoundException si aucune définition pour une class avec un nom spécifié n'a été
   *         trouvée
   */

  public Creator(Class<?> classServlet)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    bizFactory = newInstance(BizFactory.class);
    dalBackendServices = newInstance(DalBackendServices.class);
    userDao = newInstance(UserDao.class, bizFactory, dalBackendServices);
    studentDao = newInstance(StudentDao.class, bizFactory, dalBackendServices);
    mobilityDao = newInstance(MobilityDao.class, bizFactory, dalBackendServices);
    partnerDao = newInstance(PartnerDao.class, bizFactory, dalBackendServices);
    countryDao = newInstance(CountryDao.class, bizFactory, dalBackendServices);
    programDao = newInstance(ProgramDao.class, bizFactory, dalBackendServices);
    dalServices = newInstance(DalServices.class);

    userUcc = newInstance(UserUcc.class, bizFactory, userDao, dalServices, studentDao);
    studentUcc = newInstance(StudentUcc.class, studentDao, dalServices);
    countryUcc = newInstance(CountryUcc.class, countryDao, dalServices);
    partnerUcc = newInstance(PartnerUcc.class, partnerDao, dalServices);
    mobilityUcc = newInstance(MobilityUcc.class, mobilityDao, studentDao, programDao,
        dalServices);
    mobilityPostulantUcc = newInstance(MobilityPostulantUcc.class, dalServices,
        studentDao, mobilityDao, userDao);
    mobilityAllUcc = newInstance(MobilityAllUcc.class, dalServices, studentDao,
        mobilityDao, partnerDao, countryDao, userDao, programDao);
    programUcc = newInstance(ProgramUcc.class, programDao, dalServices);

    Logs.info("Injection de la servlet");
    servlet = (HttpServlet) Class.forName(classServlet.getName())
        .getConstructor(UserUcc.class, StudentUcc.class, MobilityUcc.class, PartnerUcc.class,
            CountryUcc.class, MobilityPostulantUcc.class, MobilityAllUcc.class, ProgramUcc.class)
        .newInstance(userUcc, studentUcc, mobilityUcc, partnerUcc, countryUcc, mobilityPostulantUcc,
            mobilityAllUcc, programUcc);

  }

  @SuppressWarnings("unchecked")
  private <T> T newInstance(Class<T> nameClass, Object... args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    String classeName = Class.forName(Config.getValueOfKey(nameClass.getName())).getName();
    Object nouvelleInstance;
    if (args.length == 0) {
      if (map.containsKey(classeName)) {
        return (T) map.get(classeName); // return l'instance
      }
      nouvelleInstance =
          Class.forName(Config.getValueOfKey(nameClass.getName())).getConstructor().newInstance();
      map.put(classeName, nouvelleInstance);
      return (T) nouvelleInstance;

    } else {
      return (T) Class.forName(Config.getValueOfKey(nameClass.getName()))
          .getConstructor(getParamClass(args)).newInstance(args);
    }
  }

  @SuppressWarnings("rawtypes")
  private Class[] getParamClass(Object... args) {
    Class[] parametersClass = new Class[args.length];
    for (int i = 0; i < parametersClass.length; i++) {
      Class[] interfaces = args[i].getClass().getInterfaces();
      if (interfaces.length == 0) {
        parametersClass[i] = args[i].getClass();
      } else {
        if (args[i].equals(dalServices)) {
          parametersClass[i] = args[i].getClass().getInterfaces()[1];
        } else {
          parametersClass[i] = args[i].getClass().getInterfaces()[0];
        }
      }
    }
    return parametersClass;
  }

  public HttpServlet getServlet() {
    return servlet;
  }


}


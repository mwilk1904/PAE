package be.ipl.pae.biz.factory;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.CountryImpl;
import be.ipl.pae.biz.impl.MobilityAllImpl;
import be.ipl.pae.biz.impl.MobilityImpl;
import be.ipl.pae.biz.impl.MobilityPostulantImpl;
import be.ipl.pae.biz.impl.PartnerImpl;
import be.ipl.pae.biz.impl.ProgramImpl;
import be.ipl.pae.biz.impl.StudentImpl;
import be.ipl.pae.biz.impl.UserImpl;
import be.ipl.pae.biz.interfaces.BizFactory;

import java.util.Date;

public class BizFactoryStub implements BizFactory {


  public BizFactoryStub() {

  }

  @Override
  public UserDto getUserDto() {
    String password = "TestPassword";
    return new UserImpl(1, "TestPseudo", password, "TestLastName", "TestFirstName", "TestEmail",
        "TestPassword");
  }

  @Override
  public PartnerDto getPartnerDto() {

    return new PartnerImpl(1, "TestLegalname", "TestBusinessName", "TestFullName",
        "TestDepartement", "TestOrganisation", 1, "TestAdress", 1, "TestArea", "TestZipCode",
        "TestCity", "TestEmail", "TestWebsites", "TestPhoneNumber", "TestMobilityCode");
  }

  @Override
  public CountryDto getCountryDto() {

    return new CountryImpl(1, "Tunisie", "SMP", 2);
  }

  public MobilityDto getMobilityDto() {

    return new MobilityImpl(1, 1, 1, "SMS", 1, 1, 1);
  }

  @Override
  public StudentDto getStudentDto() {

    return new StudentImpl(1, "M.", new Date(), 1, "rue de heembeek", 122, "b3", 1120, "neder",
        "bruxelles", "+32483659044", 'M', 2, "BE", "Belfius", "BCBBD", 1, 1);
  }

  @Override
  public ProgramDto getProgramDto() {
    return new ProgramImpl(1, "TestProgramme");
  }


  @Override
  public MobilityAllDto getMobilityAllDto() {
    Date date = new Date();
    return new MobilityAllImpl("SMP", 1, 1, "Hedi", "Mzoughi", "created", 1, 1, "dd", 1, 1, true,
        true, true, date);
  }

  @Override
  public MobilityPostulantDto getMobilityPostulantDto() {

    return new MobilityPostulantImpl(1, "Hedi", "Mzoughi", 1, 1);
  }


}

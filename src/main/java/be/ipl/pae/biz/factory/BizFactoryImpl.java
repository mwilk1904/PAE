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

public class BizFactoryImpl implements BizFactory {
  public BizFactoryImpl() {

  }

  @Override
  public UserDto getUserDto() {
    return new UserImpl();
  }

  @Override
  public PartnerDto getPartnerDto() {
    return new PartnerImpl();
  }

  @Override
  public MobilityDto getMobilityDto() {
    return new MobilityImpl();
  }

  public StudentDto getStudentDto() {
    return new StudentImpl();
  }

  @Override
  public CountryDto getCountryDto() {
    return new CountryImpl();
  }

  @Override
  public ProgramDto getProgramDto() {
    return new ProgramImpl();
  }

  @Override
  public MobilityAllDto getMobilityAllDto() {

    return new MobilityAllImpl();
  }

  @Override
  public MobilityPostulantDto getMobilityPostulantDto() {

    return new MobilityPostulantImpl();
  }
}

package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.factory.BizFactoryStub;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.StudentDao;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoMock implements StudentDao {

  private boolean findStudentIdByUserIdWrong;
  private boolean findVersionNumberFromWrong;
  private boolean findStudentByIdNull;


  private BizFactory bizFactory;

  /**
   * Crée un object StudentDaoMock.
   * 
   * @param findStudentIdByUserIdWrong vérifie si findStudentIdByUserId renvoie 0
   * @param findVersionNumberFromWrong vérifie si findVersionNumberFrom renvoie 0
   * @param findStudentByIdNull vérifie si findStudentById renvoie null
   * 
   * 
   */


  public StudentDaoMock(boolean findStudentIdByUserIdWrong, boolean findVersionNumberFromWrong,
      boolean findStudentByIdNull) {
    super();
    this.findStudentIdByUserIdWrong = findStudentIdByUserIdWrong;
    this.findVersionNumberFromWrong = findVersionNumberFromWrong;
    this.findStudentByIdNull = findStudentByIdNull;
    this.bizFactory = new BizFactoryStub();
  }

  @Override
  public void insertDataStudent(StudentDto studentDto) {
    // TODO Auto-generated method stub

  }

  @Override
  public int findStudentIdByUserId(int userId) {
    if (findStudentIdByUserIdWrong) {
      return 0;
    }
    return userId;
  }

  @Override
  public int findVersionNumberFrom(StudentDto studentDto) {
    if (findVersionNumberFromWrong) {
      return 0;
    }
    return studentDto.getVersionNumber() + 1;
  }

  @Override
  public StudentDto findStudentById(int studentId) {
    if (findStudentByIdNull) {
      return null;
    }
    return bizFactory.getStudentDto();
  }

  @Override
  public List<StudentDto> findStudents() {
    List<StudentDto> students = new ArrayList<>();
    students.add(bizFactory.getStudentDto());
    return students;
  }

  @Override
  public void updateDataStudent(StudentDto studentDto) {


  }

}

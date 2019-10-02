package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.User;

import com.owlike.genson.annotation.JsonIgnore;

import org.mindrot.bcrypt.BCrypt;

import utils.Logs;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserImpl implements User {
  private int id;
  private String pseudo;
  private String password;
  private String lastName;
  private String firstName;
  private String email;
  private String role;
  private Date recordDate;
  private int versionNumber;

  /**
   * Cree un objet UserImpl.
   * 
   * @param id l'id du User
   * @param pseudo le pseudo du User
   * @param password le mot de passe du User
   * @param lastName le nom du User
   * @param firstName le prenom du User
   * @param email l'email du User
   * @param role le role du User
   */
  public UserImpl(int id, String pseudo, String password, String lastName, String firstName,
      String email, String role) {
    this.id = id;
    this.pseudo = pseudo;
    this.password = password;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.role = role;
  }

  public UserImpl() {
    super();
  }



  @Override
  public String getPseudo() {
    return pseudo;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getRole() {
    if (checkRole(this.role)) {
      return this.role;
    }
    return null;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;

  }

  @Override
  public void setPassword(String password) {
    this.password = password;

  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;

  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;

  }

  @Override
  public void setEmail(String email) {
    this.email = email;

  }

  @Override
  public void setRole(String role) {
    this.role = role;

  }


  private boolean checkRole(String role) {
    Logs.fine("Vérification de la validité du rôle de l'utilisateur");
    if (role != null
        && (role.equals("student") || role.equals("teacher") || role.equals("resp_teacher"))) {
      return true;
    }
    Logs.fine("Le rôle " + role + " n'est pas valide pour l'utilisateur " + id + " (pseudo="
        + pseudo + ")");
    return false;
  }

  @Override
  public boolean checkUser() {
    Logs.fine("Vérification de la validité des données de l'utilisateur");
    if (this == null || this.getPseudo() == null || this.getPassword() == null) {
      Logs.fine("une des données n'est pas valide: pseudo=" + pseudo + " password=" + password);
      return false;
    }
    return true;
  }

  @Override
  public boolean checkEmail() {
    Logs.fine("Vérification de la validité de l'email de l'utilisateur");
    Pattern pattern = Pattern.compile("^[a-z]{2,}\\.[a-z]+@(student\\.)?vinci\\.be$");
    Matcher matcher = pattern.matcher(email);
    if (matcher.matches()) {
      return true;
    }
    Logs.fine("L'email " + email + " n'est pas valide pour l'utilisateur " + id + " (pseudo="
        + pseudo + ")");
    return false;
  }

  @Override
  public boolean checkPassword(String passwordDb) {
    Logs.fine("Vérification de la validité du mot de passe de l'utilisateur");
    if (BCrypt.checkpw(this.getPassword(), passwordDb)) {
      return true;
    }
    Logs.fine(
        "Le mot de passe " + password + " n'est pas le même que celui en db pour l'utilisateur "
            + id + " (pseudo=" + pseudo + ")");
    return false;
  }

  @Override
  public boolean encryptPassword() {
    Logs.fine("Encyption du mot de passe " + password);
    String salt = BCrypt.gensalt();
    String hashPassword = BCrypt.hashpw(password, salt);
    if (hashPassword != null) {
      Logs.fine("Mot de passe encrypté: " + hashPassword);
      setPassword(hashPassword);
      return true;
    }
    Logs.fine("Le mot de passe " + password + " n'a pas su être encrypté");
    return false;
  }

  public int getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  @Override
  public Date getRecordDate() {
    return recordDate;
  }

  @Override
  public void setRecordDate(Date recordDate) {
    this.recordDate = recordDate;
  }

}

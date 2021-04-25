package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Administrator;

public class AccountDTO {
    
    private Long id;
  
    @NotNull(message="{constraint.notnull}")
    @Size(min=6,max=32,message="{constraint.string.length.notinrange}")
    @Pattern(regexp="^[_a-zA-Z0-9-]*$",message="{constraint.string.incorrectlogin}")
    private String login;
    
    @NotNull(message="{constraint.notnull}")
    @Pattern(regexp="(?=.*\\d)(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*[\\W]).{8,30}" ,message="{constraint.string.incotectpassword}") 
    private String password;
    
    private boolean active;
    
    private String type;
    
    @NotNull(message="{constraint.notnull}")
    @Size(min=2,max=32,message="{constraint.string.length.notinrange}")
    private String name;
   
    @NotNull(message="{constraint.notnull}")
    @Size(min=2,max=32,message="{constraint.string.length.notinrange}")
    private String surname;
    
    @NotNull(message="{constraint.notnull}")
    @Pattern(regexp="^[_a-zA-Z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$",
            message="{constraint.string.incorrectemail}")
    private String email;
     
    @Size(max=12,message="{constraint.string.length.toolong}")
    private String phone;
    
    @NotNull(message="{constraint.notnull}")
    private String question;
    
    @NotNull(message="{constraint.notnull}")
    private String answer;
    
    private Administrator modifedBy;
    private String oldPassword;
    
    public AccountDTO(){    
    }

//    public AccountDTO(Long id, String login, String password, boolean active, String type, String name, String surname, String email, String phone, String question, String answer) {
//        this.id = id;
//        this.login = login;
//        this.password = password;
//        this.active = active;
//        this.type = type;
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//        this.phone = phone;
//        this.question = question;
//        this.answer = answer;
//    }

    public AccountDTO(Long id, String login, boolean active, String type, String name, String surname, String email, String phone, String question, String answer) {
        this.id = id;
        this.login = login;
        this.active = active;
        this.type = type;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
    }

    public AccountDTO(String login, String password) {
        this.login = login;
         this.password = password;
    }
  
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    } 

    public Administrator getModifedBy() {
        return modifedBy;
    }

    public void setModifedBy(Administrator modifedBy) {
        this.modifedBy = modifedBy;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
  
    
}

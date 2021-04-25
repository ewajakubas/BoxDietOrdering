package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name = "Account")
@SecondaryTable(name = "PersonalData")
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
    @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
    @NamedQuery(name = "Account.findByActive", query = "SELECT a FROM Account a WHERE a.active = :active"),
    })
       
@TableGenerator(name = "AccountIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE",
        pkColumnValue = "Account", initialValue=100)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("ACCOUNT")
public class Account extends AbstractEntity implements Serializable {
     
    public Account() {
    }
    @Override
    protected Object getBusinessKey() {
        return login;
    }
    @Id
    @Column(name = "id", updatable = false) 
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccountIdGen")
    private Long id;
    
    @NotNull
    @Size(min=6,max=64)
    @Column(name = "LOGIN", nullable = false, /*unique = true,*- ogr w initDB*/ updatable = false)
    private String login;
    
    @NotNull
    @Size(min=8,max=64)
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private boolean active; 
    
    @Column(name= "TYPE", updatable=false)
    private String type;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "NAME", table="PersonalData", length = 100, nullable = false, updatable=true)
    private String name;
    
    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "SURNAME", table= "PersonalData",length = 255, nullable = false, updatable=true)
    private String surname;
    
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")   @NotNull
    @Size(min = 6, max = 255)
    @Column(name = "EMAIL", table= "PersonalData", length = 255, /*unique = true, w piliku initDB ograniczenie*/ nullable = false, updatable= true)
    private String email;
   
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")
    @Size(max = 12)
    @Column(name = "PHONE", table= "PersonalData",length = 12, nullable = true, updatable= true)
    private String phone;
    
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "QUESTION",table= "PersonalData", nullable = false)
    private String question;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ANSWER",table= "PersonalData", nullable = false)
    private String answer;
    
    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Administrator modifiedBy;
    
    @Override
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
    public String getType() {
        return type;
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

    public Administrator getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Administrator modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    

    

}
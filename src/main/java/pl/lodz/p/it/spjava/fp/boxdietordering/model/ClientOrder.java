package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@TableGenerator(name = "ClientOrderIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "ClientOrder", initialValue=100)
@Entity
@Table(name = "CLIENT_ORDER")
@NamedQueries({
    @NamedQuery(name = "ClientOrder.findNotAccepted", query = "SELECT o FROM ClientOrder o WHERE o.accepted=false"),
    @NamedQuery(name = "ClientOrder.findAccepted", query = "SELECT o FROM ClientOrder o WHERE o.accepted=true"),
    @NamedQuery(name = "ClientOrder.findForClient", query = "SELECT o FROM ClientOrder o WHERE o.whoOrdered.login=:login"),   
    @NamedQuery(name = "ClientOrder.findNotAcceptedForClient", query = "SELECT o FROM ClientOrder o WHERE o.accepted=false AND o.whoOrdered.login=:login"),
    @NamedQuery(name = "ClientOrder.findAll", query = "SELECT o FROM ClientOrder o"),
    @NamedQuery(name = "ClientOrder.findById", query = "SELECT o FROM ClientOrder o WHERE o.id = :id"),
})

public class ClientOrder extends AbstractEntity implements Serializable {
    
    @Override
    protected Object getBusinessKey() {
        return id;
    }
    @Id
    @NotNull
    @Column(name = "ID",  updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator="ClientOrderIdGen")
    private Long id;  

    @NotNull
    @Column(name = "ACCEPTED_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptedTmp;
    
    @OneToMany(cascade =  {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientOrder_id", nullable = false, updatable = false)
    private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
    
    @Column(name = "ACCEPTED", nullable = false)
    private boolean accepted = false;
    
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    @ManyToOne
    private Client whoOrdered;
    
    @JoinColumn(name = "ACCEPTED_EMPLOYEE_ID", nullable=true)
    @ManyToOne
    private Employee whoAccepted;

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
    
    @Override
    public Long getId() {
        return id;
    }
    @PreUpdate
    private void acceptedTmp() {
        acceptedTmp = new Date();
    }

    public Date getAcceptedTmp() {
        return acceptedTmp;
    }


    public Client getWhoOrdered() {
        return whoOrdered;
    }

    public void setWhoOrdered(Client whoOrdered) {
        this.whoOrdered = whoOrdered;
    }

    public Employee getWhoAccepted() {
        return whoAccepted;
    }

    public void setWhoAccepted(Employee whoAccepted) {
        this.whoAccepted = whoAccepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }



}

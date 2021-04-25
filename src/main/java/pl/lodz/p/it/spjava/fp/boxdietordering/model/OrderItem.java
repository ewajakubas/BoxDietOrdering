package pl.lodz.p.it.spjava.fp.boxdietordering.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@TableGenerator(name = "OrdItemIdGen", table = "GENERATOR", pkColumnName = "ENTITY_NAME", valueColumnName = "ID_RANGE", pkColumnValue = "OrdItem", initialValue=100)
@Entity
@Table(name = "ORDER_ITEM")
@NamedQueries({
    @NamedQuery(name = "OrderItem.findAll", query = "SELECT o FROM OrderItem o"),
    @NamedQuery(name = "OrderItem.findById", query = "SELECT o FROM OrderItem o WHERE o.id = :id"),
    @NamedQuery(name = "OrderItem.findByDateFrom", query = "SELECT o FROM OrderItem o WHERE o.dateFrom = :dateFrom"),
    @NamedQuery(name = "OrderItem.findByDaysNb", query = "SELECT o FROM OrderItem o WHERE o.daysNb = :daysNb"),
    @NamedQuery(name = "OrderItem.findByPrice", query = "SELECT o FROM OrderItem o WHERE o.price = :price")})

public class OrderItem extends AbstractEntity implements Serializable {

    @Override
    protected Object getBusinessKey() {
        if (null != id) return id; //do porównywania pozycji juz zapisanych w bazie
        else return diet; //do porównywania pozycji nie zapisanych w bazie
    }
    
    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrdItemIdGen")
    private Long id;
       
    @Digits(integer = 6, fraction = 2)
    @DecimalMin(value="0.01")
    @Column(name = "PRICE", precision=6, scale=2, nullable=false)
    private BigDecimal price;
    
    @NotNull
    @Column(name = "DATE_FROM", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date dateFrom;
    
    @Min(value=1)
    @NotNull
    @Column(name = "DAYS_NB", nullable=false)
    private int daysNb;
    
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "DIET_ID", nullable=false, updatable=false)
    private Diet diet;

    public OrderItem() {
    }

    public OrderItem(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public int getDaysNb() {
        return daysNb;
    }

    public void setDaysNb(int daysNb) {
        this.daysNb = daysNb;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }
}

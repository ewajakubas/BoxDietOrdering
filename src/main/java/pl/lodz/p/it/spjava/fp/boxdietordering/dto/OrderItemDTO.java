package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;

public class OrderItemDTO {

    private Long id;

    @NotNull(message = "{constraint.notnull}")
    private BigDecimal price;

    @NotNull(message = "{constraint.notnull}")
    private Date dateFrom;

    @NotNull(message = "{constraint.notnull}")
    private int daysNb;

    private DietDTO diet;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long id, BigDecimal price, Date dateFrom, int daysNb, DietDTO diet) {
        this.id = id;
        this.price = price;
        this.dateFrom = dateFrom;
        this.daysNb = daysNb;
        this.diet = diet;
    }

    public DietDTO getDiet() {
        return diet;
    }

    public void setDiet(DietDTO diet) {
        this.diet = diet;
    }

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

}

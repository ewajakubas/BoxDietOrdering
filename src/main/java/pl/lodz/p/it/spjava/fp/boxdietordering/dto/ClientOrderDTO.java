package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientOrderDTO {

    private Date lastModification;

    private Long id;

    private List<OrderItemDTO> orderItemList = new ArrayList<>();

    private boolean accepted = false;

    private Date acceptedTmp;

    private ClientDTO whoOrdered;

    private EmployeeDTO whoAccepted;

    public ClientOrderDTO() {
    }

    public ClientOrderDTO(Long id, boolean accepted, Date lastModification, Date acceptedTmp, ClientDTO whoOrdered, EmployeeDTO whoAccepted, List<OrderItemDTO> orderItemList) {
        this.id = id;
        this.accepted = accepted;
        this.lastModification = lastModification;
        this.acceptedTmp = acceptedTmp;
        this.whoOrdered = whoOrdered;
        this.whoAccepted = whoAccepted;
        this.orderItemList = orderItemList;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemDTO> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemDTO> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getAcceptedTmp() {
        return acceptedTmp;
    }

    public ClientDTO getWhoOrdered() {
        return whoOrdered;
    }

    public void setWhoOrdered(ClientDTO whoOrdered) {
        this.whoOrdered = whoOrdered;
    }

    public EmployeeDTO getWhoAccepted() {
        return whoAccepted;
    }

    public void setWhoAccepted(EmployeeDTO whoAccepted) {
        this.whoAccepted = whoAccepted;
    }

    public String getLoginWhoOrdered() {
        if (null == whoOrdered) {
            return "";
        } else {
            return whoOrdered.getLogin();
        }
    }

    public String getLoginWhoAccepted() {
        if (null == whoAccepted) {
            return "";
        } else {
            return whoAccepted.getLogin();
        }
    }

    public int getNumberOfOrderItem() {
        return this.orderItemList.size();
    }

    public BigDecimal getTotalOrder() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO, 2);
        for (OrderItemDTO oi : this.orderItemList) {
            total = total.add(oi.getPrice().multiply(new BigDecimal(oi.getDaysNb())));
        }
        return total;
    }

}

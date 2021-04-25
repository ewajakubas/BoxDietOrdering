package pl.lodz.p.it.spjava.fp.boxdietordering.web.clientOrder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.OrderItemDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;

@Named(value = "createNewClientOrderPageBean")
@RequestScoped
public class CreateNewClientOrderPageBean implements Serializable {

    @EJB
    private DietEndpoint dietEndpoint;
    @Inject
    private ClientOrderController clientOrderController;

    private String selectedDiet;

    public String getSelectedDiet() {
        return selectedDiet;
    }

    public void setSelectedDiet(String selectedDiet) {
        this.selectedDiet = selectedDiet;
    }

    private ClientOrderDTO clientOrderDTO = new ClientOrderDTO();

    private OrderItemDTO orderItemDTO = new OrderItemDTO();

    private Date selectedDate;

    public Date getSelectedDate() {
        return selectedDate;
    }

    public ClientOrderDTO getClientOrderDTO() {
        return clientOrderDTO;
    }

    public void setClientOrderDTO(ClientOrderDTO clientOrderDTO) {
        this.clientOrderDTO = clientOrderDTO;
    }

    public OrderItemDTO getOrderItemDTO() {
        return orderItemDTO;
    }

    public void setOrderItemDTO(OrderItemDTO orderItemDTO) {
        this.orderItemDTO = orderItemDTO;
    }

    public CreateNewClientOrderPageBean() {
    }

    @PostConstruct
    public void init() {
        try {
            dietList = dietEndpoint.listDiets();
        } catch (AppBaseException ex) {
            Logger.getLogger(CreateNewClientOrderPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<DietDTO> dietList;

    public List<DietDTO> getDietCategoryList() {
        return dietList;
    }

    public List<DietDTO> getDietList() {
        return dietList;
    }

    public void setDietList(List<DietDTO> dietList) {
        this.dietList = dietList;
    }

    public String createNewClientOrder() throws AppBaseException {
        if (orderItemDTO.getDaysNb() < 3 || orderItemDTO.getDaysNb() > 30) {
            ContextUtils.emitI18NMessage("OrderForm:daysNb", "error.new.order.days.number.constraint");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Date choosenDate = orderItemDTO.getDateFrom();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        LocalDate minDate = currentDate.plusDays(3);
        LocalDate maxDate = currentDate.plusDays(30);

        Date minDate1 = Date.from(minDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date maxDate1 = Date.from(maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (choosenDate.compareTo(minDate1) < 0) {
            ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.toshort.date");
            ContextUtils.emitI18NMessage("OrderForm:orderDate", sdf.format(minDate1));
            return null;
        }
        if (choosenDate.compareTo(maxDate1) > 0) {
            ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.tolong.date");
            ContextUtils.emitI18NMessage("OrderForm:orderDate", sdf.format(maxDate1));
            return null;
        }

        DietDTO dietDTO = new DietDTO();
        dietDTO.setName(selectedDiet);
        orderItemDTO.setDiet(dietDTO);
        clientOrderDTO.getOrderItemList().add(orderItemDTO);
        return clientOrderController.createNewClientOrder(clientOrderDTO);
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

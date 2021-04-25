package pl.lodz.p.it.spjava.fp.boxdietordering.web.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AccountDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.AdministratorDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.ClientOrderDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietCategoryDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.EmployeeDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.OrderItemDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Account;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Administrator;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Client;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.DietCategory;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Employee;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.OrderItem;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.ClientOrder;

public class DTOConverter {

    public static AccountDTO createAccountDTOFromEntity(Account account) {

        if (account instanceof Client) {
            return createClientDTOFromEntity((Client) account);
        }

        if (account instanceof Employee) {
            return createEmployeeDTOFromEntity((Employee) account);
        }

        if (account instanceof Administrator) {
            return createAdministratorDTOFromEntity((Administrator) account);
        }

        return null;
    }

    private static ClientDTO createClientDTOFromEntity(Client client) {
        return null == client ? null : new ClientDTO(client.getCity(), client.getZipCode(), client.getStreet(),
                client.getHouseNumber(), client.getApartmentNumber(), client.getId(), client.getLogin(), client.isActive(),
                client.getType(), client.getName(), client.getSurname(), client.getEmail(), client.getPhone(),
                client.getQuestion(), client.getAnswer());
    }

    private static EmployeeDTO createEmployeeDTOFromEntity(Employee employee) {
        return null == employee ? null : new EmployeeDTO(employee.getOfficeNumber(), employee.getId(), employee.getLogin(),
                employee.isActive(), employee.getType(), employee.getName(), employee.getSurname(), employee.getEmail(),
                employee.getPhone(), employee.getQuestion(), employee.getAnswer());
    }

    private static AdministratorDTO createAdministratorDTOFromEntity(Administrator administrator) {
        return null == administrator ? null : new AdministratorDTO(administrator.getAlarmNumber(), administrator.getId(),
                administrator.getLogin(), administrator.isActive(), administrator.getType(), administrator.getName(),
                administrator.getSurname(), administrator.getEmail(), administrator.getPhone(),
                administrator.getQuestion(), administrator.getAnswer());
    }

    public static List<AccountDTO> createAccountsListDTOFromEntity(List<Account> accountList) {
        return null == accountList ? null : accountList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createAccountDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

    public static List<ClientDTO> createAccountsClientsListDTOFromEntity(List<Client> accountClientList) {
        return null == accountClientList ? null : accountClientList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createClientDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

    public static DietDTO createDietDTOFromEntity(Diet diet) {
        return null == diet ? null : new DietDTO(diet.getId(), diet.getName(), diet.getPrice(), createDietCategoryDTOFromEntity(diet.getDietCategory()));

    }

    public static List<DietDTO> createListDietDTOFromEntity(List<Diet> dietList) {
        return null == dietList ? null : dietList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createDietDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

    public static ClientOrderDTO createClientOrderDTOFromEntity(ClientOrder clientOrder) {
        return null == clientOrder ? null : new ClientOrderDTO(clientOrder.getId(), clientOrder.isAccepted(), clientOrder.getModificationTimestamp(),
                clientOrder.getAcceptedTmp(), createClientDTOFromEntity(clientOrder.getWhoOrdered()),
                createEmployeeDTOFromEntity(clientOrder.getWhoAccepted()),
                createListOrderItemDTOFromEntity(clientOrder.getOrderItemList()));
    }

    public static List<ClientOrderDTO> createClientOrderListDTOfromEntity(List<ClientOrder> clientOrderList) {
        return null == clientOrderList ? null : clientOrderList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createClientOrderDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

    public static DietCategoryDTO createDietCategoryDTOFromEntity(DietCategory dietCategory) {
        return null == dietCategory ? null : new DietCategoryDTO(dietCategory.getId(), dietCategory.getName());
    }

    public static List<DietCategoryDTO> createListDietCategotyDTOFromEntity(List<DietCategory> dietCategoryList) {
        return null == dietCategoryList ? null : dietCategoryList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createDietCategoryDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

    public static OrderItemDTO createOrderItemDTOFromEntity(OrderItem item) {
        return null == item ? null : new OrderItemDTO(item.getId(), item.getPrice(), item.getDateFrom(), item.getDaysNb(), createDietDTOFromEntity(item.getDiet()));

    }

    public static List<OrderItemDTO> createListOrderItemDTOFromEntity(List<OrderItem> orderItemList) {
        return null == orderItemList ? null : orderItemList.stream()
                .filter(Objects::nonNull)
                .map(elt -> DTOConverter.createOrderItemDTOFromEntity(elt))
                .collect(Collectors.toList());
    }

}

package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietCategoryDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.DietCategoryFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.DietFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.managers.DietManager;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AccountException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.DietException;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.Diet;
import pl.lodz.p.it.spjava.fp.boxdietordering.model.DietCategory;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.DTOConverter;

@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(LoggingInterceptor.class)
@Stateful
public class DietEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @Inject
    private DietManager dietManager;

    @Inject
    private DietCategoryFacade dietCategoryFacade;

    @Inject
    private DietFacade dietFacade;

    @Resource(name = "txRetryLimit")
    private int txRetryLimit;

    private Diet dietState;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed({"Employee"})
    public DietDTO rememberSelectedDietInState(Long id) throws AppBaseException {
        dietState = dietFacade.findById(id);
        if (null == dietState){
            throw DietException.createExceptionNoDietFound();
            } 
        DietCategoryDTO dietCategoryDTO = new DietCategoryDTO(dietState.getDietCategory().getName());
        return new DietDTO(
                dietState.getId(),
                dietState.getName(),
                dietState.getPrice(),
                dietCategoryDTO);
    }

    @RolesAllowed({"Employee"})
    public void createNewDiet(DietDTO dietDTO) throws AppBaseException {
        List<DietCategory> dietCategoryList = dietCategoryFacade.findAll();
        DietCategory selectedDietCategoryName = null;
        for (DietCategory dietCategory : dietCategoryList) {
            if (dietCategory.getName().equals(dietDTO.getDietCategory().getName())) {
                selectedDietCategoryName = dietCategory;
                break;
            }
        }
        Diet diet = new Diet();
        diet.setName(dietDTO.getName());
        diet.setPrice(dietDTO.getPrice());
        diet.setDietCategory(selectedDietCategoryName);

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                dietManager.createDiet(diet);
                rollbackTX = dietManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej createNewDiet zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw DietException.createDietExceptionWithTxRetryRollback();
        }
    }

    @PermitAll
    public List<DietDTO> listDiets() throws AppBaseException {
        return DTOConverter.createListDietDTOFromEntity(dietFacade.findAll());
    }

    @RolesAllowed({"Employee"})
    public void saveEditedDiet(DietDTO dietDTO) throws AppBaseException {
        if (null == dietState) {
            throw DietException.createExceptionWrongState(dietState);
        }

        dietState.setName(dietDTO.getName());
        dietState.setPrice(dietDTO.getPrice());

        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                dietManager.editDiet(dietState);
                rollbackTX = dietManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej saveEditedDiet zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw DietException.createDietExceptionWithTxRetryRollback();
        }
    }

    @RolesAllowed({"Employee"})
    public void deleteDiet(DietDTO dietDTO) throws AppBaseException {
        if (null == dietState) {
            throw DietException.createExceptionWrongState(dietState);
        }
        boolean rollbackTX;
        int retryTXCounter = txRetryLimit;

        do {
            try {
                dietManager.deleteDiet(dietState);
                rollbackTX = dietManager.isLastTransactionRollback();
            } catch (AppBaseException | EJBTransactionRolledbackException ex) {
                Logger.getGlobal().log(Level.SEVERE, "Próba " + retryTXCounter
                        + " wykonania metody biznesowej deleteDiet zakończona wyjątkiem klasy:"
                        + ex.getClass().getName());
                rollbackTX = true;
            }

        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AccountException.createAccountExceptionWithTxRetryRollback();
        }
    }

}

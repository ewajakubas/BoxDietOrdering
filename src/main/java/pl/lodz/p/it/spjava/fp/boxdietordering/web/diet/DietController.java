package pl.lodz.p.it.spjava.fp.boxdietordering.web.diet;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietCategoryEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints.DietEndpoint;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.DietException;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.ContextUtils;

@SessionScoped
public class DietController implements Serializable {

    @EJB
    private DietCategoryEndpoint dietCategoryEndpoint;

    @EJB
    private DietEndpoint dietEndpoint;

    private DietDTO selectedDietDTO;

    private DietDTO dietCreate;

    public DietController() {
    }

    public DietDTO getSelectedDietDTO() {
        return selectedDietDTO;
    }

    public void setSelectedDietDTO(DietDTO selectedDietDTO) {
        this.selectedDietDTO = selectedDietDTO;
    }

    public void selectDietForChange(DietDTO dietDTO) throws AppBaseException {
        selectedDietDTO = dietEndpoint.rememberSelectedDietInState(dietDTO.getId());

    }

    public String createNewDiet(DietDTO diet) {
        try {
            dietCreate = diet;
            dietEndpoint.createNewDiet(dietCreate);
            dietCreate = null;
            return "success";
        } catch (DietException de) {
            if (DietException.KEY_DIET_NAME_EXISTS.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage("createNewDietForm:name", DietException.KEY_DIET_NAME_EXISTS);
            } else {
                Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji createNewDiet wyjatku: ", de);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji createNewDiet wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String deleteDiet(DietDTO dietDTO) throws AppBaseException {
        try {
            dietEndpoint.deleteDiet(dietDTO);
            return "success";
        } catch (DietException de) {
            if (DietException.KEY_DIET_ALREADY_CHANGED.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage(null, DietException.KEY_DIET_ALREADY_CHANGED);
            } else if (DietException.KEY_DIET_NOT_FOUND.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage(null, DietException.KEY_DIET_NOT_FOUND);
            } else if (DietException.KEY_ORDERED_DIET_IS.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage(null, DietException.KEY_ORDERED_DIET_IS);
            } else if (DietException.KEY_DIET_OPTIMISTIC_LOCK.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage(null, DietException.KEY_DIET_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji deleteDiet wyjatku: ", de);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji deleteDiet wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

    public String saveEditedDiet(DietDTO dietDTO) throws AppBaseException {
        try {
            dietEndpoint.saveEditedDiet(dietDTO);
            return "success";
        } catch (DietException de) {
            if (DietException.KEY_DIET_NAME_EXISTS.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage("editDietForm:name", DietException.KEY_DIET_NAME_EXISTS);
            } else if (DietException.KEY_DIET_OPTIMISTIC_LOCK.equals(de.getMessage())) {
                ContextUtils.emitI18NMessage(null, DietException.KEY_DIET_OPTIMISTIC_LOCK);
            } else {
                Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                        "Zgłoszenie w metodzie akcji saveEditedDiet wyjatku: ", de);
            }
            return null;
        } catch (AppBaseException abe) {
            Logger.getLogger(DietController.class.getName()).log(Level.SEVERE,
                    "Zgłoszenie w metodzie akcji saveEditedDiet wyjatku typu: ", abe.getClass());
            if (ContextUtils.isI18NKeyExist(abe.getMessage())) {
                ContextUtils.emitI18NMessage(null, abe.getMessage());
            }
            return null;
        }
    }

}

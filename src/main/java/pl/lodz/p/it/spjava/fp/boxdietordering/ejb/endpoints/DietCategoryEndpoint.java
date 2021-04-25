package pl.lodz.p.it.spjava.fp.boxdietordering.ejb.endpoints;

import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.fp.boxdietordering.dto.DietCategoryDTO;
import pl.lodz.p.it.spjava.fp.boxdietordering.ejb.facades.DietCategoryFacade;
import pl.lodz.p.it.spjava.fp.boxdietordering.web.utils.DTOConverter;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
public class DietCategoryEndpoint {

    @Inject
    private DietCategoryFacade dietCategoryFacade;

    public List<DietCategoryDTO> dietCategoryList() {
        return DTOConverter.createListDietCategotyDTOFromEntity(dietCategoryFacade.findAll());
    }
}

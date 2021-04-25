package pl.lodz.p.it.spjava.fp.boxdietordering.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmployeeDTO extends AccountDTO {

    @NotNull(message = "{constraint.notnull}")
    @Size(max = 12, message = "{constraint.string.length.toolong}")
    @Pattern(regexp = "\\d{9,13}",
            message = "{constraint.string.incorrecttel}")
    private String officeNumber;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String officeNumber, Long id, String login, boolean active, String type, String name, String surname, String email, String phone, String question, String answer) {
        super(id, login, active, type, name, surname, email, phone, question, answer);
        this.officeNumber = officeNumber;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

}

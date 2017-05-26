/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mallikharjuna Rao Dande
 */
public class Equipment {
    private String projectorId;
    private String companyName;
    private String type;
    private String details;

    public Equipment() {
    }

    public Equipment(String projectorId, String companyName, String type, String details) {
        this.projectorId = projectorId;
        this.companyName = companyName;
        this.type = type;
        this.details = details;
    }

    public String getProjectorId() {
        return projectorId;
    }

    public void setProjectorId(String projectorId) {
        this.projectorId = projectorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Equipment{" + "projectorId=" + projectorId + ", companyName=" + companyName + ", type=" + type + ", details=" + details + '}';
    }
    
    
    
    
    
}

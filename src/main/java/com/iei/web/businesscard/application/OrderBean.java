package com.iei.web.businesscard.application;

import com.iei.web.businesscard.business.businesscard.boundary.BusinessCardService;
import com.iei.web.businesscard.business.businesscard.boundary.EmailService;
import com.iei.web.businesscard.business.businesscard.entity.BusinessCard;
import com.iei.web.businesscard.business.businesscard.entity.CompanyEnum;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    BusinessCardService service;
    @Inject
    EmailService emailService;

    private BusinessCard businessCard = new BusinessCard();

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public String approve() throws Exception {
        return "/approveOrder?faces-redirect=true";
    }

    public String cancel() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/order?faces-redirect=true";
    }

    public String order() throws Exception {
        service.order(businessCard);
        emailService.sendEmail(businessCard);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/order?faces-redirect=true";
    }

    public CompanyEnum[] getCompanies() {
        return CompanyEnum.values();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iei.web.businesscard.business.businesscard.entity;

/**
 *
 * @author rabhala
 */
public enum CompanyEnum {

    IE("Infinite Energy", "(352) 313-1654", "7001 SW 24th Ave| Gainesville,FL 32607-3704", "www.InfiniteEnergy.com"),
    VE("Veteran Energy", "(800) 578-7070", "10575 Katy Fwy |Houston, TX 77024", "www.VeteranEnergy.com");

    private final String label;
    private final String phone;
    private final String address;
    private final String companyURL;

    CompanyEnum(String label, String phone, String address, String companyURL) {
        this.label = label;
        this.phone = phone;
        this.address = address;
        this.companyURL = companyURL;

    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyURL() {
        return companyURL;
    }

    public String getLabel() {
        return label;
    }

}

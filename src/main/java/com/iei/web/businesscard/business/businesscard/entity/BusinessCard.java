package com.iei.web.businesscard.business.businesscard.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BusinessCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String designation;
    private String extension;
    private String cell;
    private String email;
    @Enumerated(EnumType.STRING)
    private CompanyEnum company;

    public CompanyEnum getCompany() {
        return company;
    }

    public void setCompany(CompanyEnum company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public String getExtension() {
        return extension;
    }

    public String getCell() {
        return cell;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

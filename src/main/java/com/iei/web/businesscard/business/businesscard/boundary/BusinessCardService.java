package com.iei.web.businesscard.business.businesscard.boundary;

import com.iei.web.businesscard.business.businesscard.entity.BusinessCard;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BusinessCardService {

    @PersistenceContext
    EntityManager em;

    public void order(BusinessCard businessCard) {
        em.persist(businessCard);
    }

}

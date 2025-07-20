package com.company.cas.dao;

import com.company.cas.model.Attachment;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Attachment findById(Long id) {
        return sessionFactory.getCurrentSession().get(Attachment.class, id);
    }
}
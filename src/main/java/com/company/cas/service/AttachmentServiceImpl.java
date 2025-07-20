package com.company.cas.service;

import com.company.cas.dao.AttachmentDAO;
import com.company.cas.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDAO attachmentDAO;

    @Override
    @Transactional(readOnly = true)
    public Attachment findById(Long id) {
        return attachmentDAO.findById(id);
    }
}
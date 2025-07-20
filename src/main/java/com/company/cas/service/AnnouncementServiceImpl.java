package com.company.cas.service;

import com.company.cas.dao.AnnouncementDAO;
import com.company.cas.model.Announcement;
import java.util.List;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementDAO announcementDAO;

    @Override
    @Transactional
    public void saveOrUpdate(Announcement announcement) {
        if (announcement == null) { return; }
        if (announcement.getContent() != null) {
            String safeContent = Jsoup.clean(announcement.getContent(), Safelist.basicWithImages());
            announcement.setContent(safeContent);
        }
        announcementDAO.saveOrUpdate(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Announcement> findByIdWithAttachments(Long id) {
        return Optional.ofNullable(announcementDAO.findByIdWithAttachments(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Announcement> findPaginatedWithAttachments(int pageNumber, int pageSize) {
        int pageIndex = pageNumber - 1 < 0 ? 0 : pageNumber - 1;
        List<Announcement> announcements = announcementDAO.findPaginatedWithAttachments(pageIndex, pageSize);
        long totalItems = announcementDAO.countAllActive();
        return new PageImpl<>(announcements, PageRequest.of(pageIndex, pageSize), totalItems);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        announcementDAO.softDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public Announcement findById(Long id) {
        return announcementDAO.findById(id);
    }
}
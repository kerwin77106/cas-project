package com.company.cas.service;

import com.company.cas.dao.AnnouncementDAO;
import com.company.cas.model.Announcement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service 註解告訴 Spring，這是一個業務邏輯層的元件(Bean)
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    // 自動注入我們寫好的 DAO，讓 Service 可以呼叫它
    @Autowired
    private AnnouncementDAO announcementDAO;

    // @Transactional 註解是 Service 層的精髓！
    // Spring 會自動為這個方法加上資料庫交易控制。
    // 方法開始時開啟交易，方法成功結束時提交(commit)，若中途發生錯誤則回滾(rollback)。
    @Transactional
    @Override
    public void saveOrUpdate(Announcement announcement) {
        announcementDAO.saveOrUpdate(announcement);
    }

    // 對於「只讀取」的操作，可以加上 readOnly = true，能優化效能
    @Transactional(readOnly = true)
    @Override
    public Announcement findById(Integer id) {
        return announcementDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Announcement> findAllActive() {
        return announcementDAO.findAllActive();
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        announcementDAO.softDelete(id);
    }
}
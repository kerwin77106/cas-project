package com.company.cas.dao;

import com.company.cas.model.Announcement;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnnouncementDAOImpl implements AnnouncementDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(Announcement announcement) {
        getCurrentSession().saveOrUpdate(announcement);
    }

    @Override
    @Deprecated
    public Announcement findById(Long id) {
        return getCurrentSession().get(Announcement.class, id);
    }

    /**
     * 【新方法的實現】
     * 使用 HQL 和 LEFT JOIN FETCH 來一次性查詢公告及其關聯的附件。
     * 這將從根本上解決 LazyInitializationException。
     */
    @Override
    public Announcement findByIdWithAttachments(Long id) {
        String hql = "SELECT a FROM Announcement a LEFT JOIN FETCH a.attachments WHERE a.id = :id AND a.isDeleted = 0";
        return getCurrentSession()
                .createQuery(hql, Announcement.class)
                .setParameter("id", id)
                .uniqueResult(); // 使用 uniqueResult 是因為我們預期只會有一筆或零筆結果
    }

    @Override
    public List<Announcement> findAllActive() {
        // 您的這個查詢已經很棒了，它正確地使用了 JOIN FETCH
        String hql = "SELECT DISTINCT a FROM Announcement a LEFT JOIN FETCH a.attachments WHERE a.isDeleted = 0 ORDER BY a.publishDate DESC";
        return getCurrentSession().createQuery(hql, Announcement.class).list();
    }

    @Override
    public void softDelete(Long id) {
        // 注意：這裡呼叫的是舊的 findById，這沒問題，因為我們只是要取得 ID 來更新狀態。
        Announcement announcement = findById(id);
        if (announcement != null) {
            announcement.setIsDeleted(1);
            getCurrentSession().update(announcement);
        }
    }

    /**
     * 【新方法的實現】
     * 這個方法將取代舊的 findPaginated。參數 pageIndex 是從 0 開始的頁碼。
     * DISTINCT 關鍵字是為了防止因為 JOIN 導致回傳重複的 Announcement 物件。
     */
    @Override
    public List<Announcement> findPaginatedWithAttachments(int pageIndex, int pageSize) {
        String hql = "SELECT DISTINCT a FROM Announcement a LEFT JOIN FETCH a.attachments WHERE a.isDeleted = 0 ORDER BY a.publishDate DESC";
        return getCurrentSession()
                .createQuery(hql, Announcement.class)
                .setFirstResult(pageIndex * pageSize) // 計算起始位置
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public long countAllActive() {
        String hql = "SELECT COUNT(a.id) FROM Announcement a WHERE a.isDeleted = 0";
        return getCurrentSession()
                .createQuery(hql, Long.class)
                .getSingleResult();
    }
}
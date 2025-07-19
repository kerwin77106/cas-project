package com.company.cas.dao;

import com.company.cas.model.Announcement;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*@Repository: 我們用這個註解告訴 Spring：「嗨，這個類別是負責跟資料庫打交道的，請幫我管理它！」

@Autowired: 我們用這個註解來取得 SessionFactory，這是操作 Hibernate 的核心工廠，我們之前已在 applicationContext.xml 中設定好它。

軟刪除的實現: 注意看 softDelete 方法，它完美地利用了您設計的 is_deleted 欄位。它先找出資料，然後將狀態改為 1 再更新回去，而不是真的從資料庫中 DELETE。 */

// @Repository 是 Spring 的一個註解，專門用於標示資料存取層的元件(Bean)
@Repository
public class AnnouncementDAOImpl implements AnnouncementDAO {

    // @Autowired 會讓 Spring 自動將我們在 applicationContext.xml 中設定好的 SessionFactory 注入進來
    @Autowired
    private SessionFactory sessionFactory;

    // 取得當前的 Hibernate Session
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(Announcement announcement) {
        getCurrentSession().saveOrUpdate(announcement);
    }

    @Override
    public Announcement findById(Integer id) {
        return getCurrentSession().get(Announcement.class, id);
    }

    @Override
    public List<Announcement> findAllActive() {
        // 使用 HQL (Hibernate Query Language) 來查詢
        // 我們只查詢 isDeleted = 0 的資料，並根據發布日期降冪排序
        return getCurrentSession()
                .createQuery("FROM Announcement WHERE isDeleted = 0 ORDER BY publishDate DESC", Announcement.class)
                .list();
    }

    @Override
    public void softDelete(Integer id) {
        Announcement announcement = findById(id);
        if (announcement != null) {
            // 這就是軟刪除！我們不是真的刪除資料，而是更新 isDeleted 欄位的狀態
            announcement.setIsDeleted(1);
            getCurrentSession().update(announcement);
        }
    }
}
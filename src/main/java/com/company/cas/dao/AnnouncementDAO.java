package com.company.cas.dao;

import com.company.cas.model.Announcement;
import java.util.List;

public interface AnnouncementDAO {

    /**
     * 新增或更新一筆公告
     * @param announcement 要儲存的公告物件
     */
    void saveOrUpdate(Announcement announcement);

    /**
     * 根據 ID 尋找一筆公告
     * @param id 公告的 ID
     * @return 找到的公告物件，若無則回傳 null
     */
    Announcement findById(Integer id);

    /**
     * 尋找所有未被軟刪除的公告
     * @return 公告列表
     */
    List<Announcement> findAllActive();
    
    /**
     * 根據 ID 軟刪除一筆公告
     * @param id 要刪除的公告 ID
     */
    void softDelete(Integer id);

    // (未來擴充用) 加上分頁查詢的功能
    // List<Announcement> findPaginated(int page, int size);
    // long countAllActive();
}
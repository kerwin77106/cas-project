package com.company.cas.service;

import com.company.cas.model.Announcement;
import java.util.List;

public interface AnnouncementService {

    /**
     * 儲存或更新公告
     * @param announcement 要處理的公告物件
     */
    void saveOrUpdate(Announcement announcement);

    /**
     * 透過 ID 取得公告
     * @param id 公告 ID
     * @return 找到的公告物件
     */
    Announcement findById(Integer id);

    /**
     * 取得所有有效的公告
     * @return 公告列表
     */
    List<Announcement> findAllActive();

    /**
     * 刪除公告 (軟刪除)
     * @param id 要刪除的公告 ID
     */
    void deleteById(Integer id);
}
package com.company.cas.dao;

import com.company.cas.model.Announcement;
import java.util.List;

public interface AnnouncementDAO {

    void saveOrUpdate(Announcement announcement);

    @Deprecated // 標記為棄用，提醒自己不要再用它
    Announcement findById(Long id);

    // 【新方法】這個方法將解決詳情頁的崩潰問題
    Announcement findByIdWithAttachments(Long id);

    void softDelete(Long id);
    
    // 【新方法】取代舊的 findPaginated，解決列表頁的潛在效能問題
    List<Announcement> findPaginatedWithAttachments(int pageIndex, int pageSize);

    long countAllActive();

    // 為了清晰，可以移除 findAllActive()，因為分頁查詢已包含其功能，但暫時保留也無妨
    List<Announcement> findAllActive(); 
}
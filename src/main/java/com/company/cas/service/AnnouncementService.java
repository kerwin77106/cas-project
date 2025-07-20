package com.company.cas.service;

import com.company.cas.model.Announcement;
import org.springframework.data.domain.Page; // 引入 Page
import java.util.Optional;

public interface AnnouncementService {

    void saveOrUpdate(Announcement announcement);

    // 【新方法】根據 ID 查找公告，並預先抓取附件
    Optional<Announcement> findByIdWithAttachments(Long id);

    // 【新方法】分頁查詢，並預先抓取附件
    Page<Announcement> findPaginatedWithAttachments(int pageNumber, int pageSize);

    void deleteById(Long id);

    // 為了讓舊程式碼暫時不報錯，我們先保留這個，但之後應移除
    @Deprecated
    Announcement findById(Long id);
}
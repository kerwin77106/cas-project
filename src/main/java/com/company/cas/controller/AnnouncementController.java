package com.company.cas.controller;

import com.company.cas.model.Announcement;
import com.company.cas.service.AnnouncementService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// @Controller 註解，標示這個類別是 Spring MVC 的控制器
@Controller
// @RequestMapping 將這個控制器下的所有請求都映射到 /announcements 路徑下
@RequestMapping("/announcements")
public class AnnouncementController {

    // 自動注入我們寫好的 Service
    @Autowired
    private AnnouncementService announcementService;

    /**
     * 處理顯示所有公告列表的請求
     * 
     * @param model 用來將資料傳遞給 View (JSP)
     * @return 視圖的名稱
     */
    @GetMapping("/list")
    public String listAnnouncements(Model model) {
        // 1. 呼叫 Service 取得所有有效的公告
        List<Announcement> announcements = announcementService.findAllActive();
        // 2. 將公告列表加入到 model 中，JSP 頁面就可以取用
        model.addAttribute("announcements", announcements);
        // 3. 回傳視圖名稱 "announcement-list"，Spring 會去尋找
        // /WEB-INF/views/announcement-list.jsp
        return "announcement-list";
    }

    /**
     * 顯示「新增公告」的表單頁面
     * 
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // 建立一個空的 Announcement 物件，給表單進行資料綁定
        model.addAttribute("announcement", new Announcement());
        return "announcement-form";
    }

    /**
     * 處理「新增」或「修改」的表單提交
     * 
     * @param announcement Spring MVC 會自動將表單的欄位值填充到這個物件中
     * @return
     */
    @PostMapping("/save")
    public String saveAnnouncement(@ModelAttribute("announcement") Announcement formAnnouncement) {

        Announcement dbAnnouncement;

        // 判斷是「更新」還是「新增」
        if (formAnnouncement.getId() != null) {
            // === 這是更新的流程 ===
            // 1. 先從資料庫根據 ID 取得最原始、最完整的資料
            dbAnnouncement = announcementService.findById(formAnnouncement.getId());

            // 2. 將表單送過來的新資料，手動更新到從資料庫撈出來的物件上
            dbAnnouncement.setTitle(formAnnouncement.getTitle());
            dbAnnouncement.setPublisherName(formAnnouncement.getPublisherName());
            dbAnnouncement.setPublishDate(formAnnouncement.getPublishDate());
            dbAnnouncement.setEndDate(formAnnouncement.getEndDate());
            dbAnnouncement.setContent(formAnnouncement.getContent());
            // 注意：我們沒有動 isDeleted 和 status，所以它們會保留資料庫中的原始值 (0 和 1)

        } else {
            // === 這是新增的流程 ===
            // 直接使用從表單綁定過來的物件
            dbAnnouncement = formAnnouncement;
            dbAnnouncement.setStatus(1);
            dbAnnouncement.setIsDeleted(0);
        }

        // 3. 儲存我們處理好的完整物件
        announcementService.saveOrUpdate(dbAnnouncement);

        return "redirect:/announcements/list";
    }

    /**
     * 顯示「修改公告」的表單頁面
     * 
     * @param id    從 URL 路徑中取得的公告 ID
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Announcement announcement = announcementService.findById(id);
        model.addAttribute("announcement", announcement);
        return "announcement-form";
    }

    /**
     * 處理「刪除公告」的請求
     * 
     * @param id 從 URL 路徑中取得的公告 ID
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Integer id) {
        announcementService.deleteById(id);
        return "redirect:/announcements/list";
    }
}
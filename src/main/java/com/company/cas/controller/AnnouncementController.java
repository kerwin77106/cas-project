package com.company.cas.controller;

import com.company.cas.model.Announcement;
import com.company.cas.model.Attachment;
import com.company.cas.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    public String listAnnouncements(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model model) {
        final int pageSize = 5;

        // 改為呼叫回傳 Page 物件的新方法
        Page<Announcement> announcementPage = announcementService.findPaginatedWithAttachments(currentPage, pageSize);

        // 直接從 Page 物件取用所有資訊，程式碼更簡潔！
        model.addAttribute("announcements", announcementPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", announcementPage.getTotalPages());
        model.addAttribute("totalItems", announcementPage.getTotalElements());

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
    public String saveAnnouncement(@ModelAttribute("announcement") Announcement formAnnouncement,
            @RequestParam("files") MultipartFile[] files) { // 新增 files 參數來接收檔案

        final String uploadDirectory = "/opt/uploads/cas-files/"; // 檔案儲存路徑

        Announcement dbAnnouncement;

        // 更新的流程 (先讀取，再更新)
        if (formAnnouncement.getId() != null) {
            dbAnnouncement = announcementService.findByIdWithAttachments(formAnnouncement.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Announcement not found for saving: " + formAnnouncement.getId()));
            dbAnnouncement.setTitle(formAnnouncement.getTitle());
            dbAnnouncement.setPublisherName(formAnnouncement.getPublisherName());
            dbAnnouncement.setPublishDate(formAnnouncement.getPublishDate());
            dbAnnouncement.setEndDate(formAnnouncement.getEndDate());
            dbAnnouncement.setContent(formAnnouncement.getContent());
        } else {
            // 新增的流程
            dbAnnouncement = formAnnouncement;
            dbAnnouncement.setStatus(1);
            dbAnnouncement.setIsDeleted(0);
        }

        // 處理檔案上傳
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 為了避免檔名衝突，產生一個唯一的檔名
                    String originalFilename = file.getOriginalFilename();
                    String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                    File destinationFile = new File(uploadDirectory + uniqueFilename);

                    // 將上傳的檔案儲存到伺服器
                    file.transferTo(destinationFile);

                    // 建立 Attachment 物件並存入資料庫
                    Attachment attachment = new Attachment();
                    attachment.setOriginalFilename(originalFilename);
                    attachment.setStoredPath(destinationFile.getPath());
                    attachment.setFileType(file.getContentType());
                    attachment.setFileSize(file.getSize());
                    attachment.setAnnouncement(dbAnnouncement); // 建立關聯

                    // 將附件加入到公告的附件列表中
                    dbAnnouncement.getAttachments().add(attachment);

                } catch (IOException e) {
                    e.printStackTrace();
                    // 可以在這裡加入錯誤處理，例如回傳一個錯誤頁面
                }
            }
        }

        // 儲存公告以及其關聯的附件 (因為有 CascadeType.ALL, Hibernate會自動處理)
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
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Announcement announcement = announcementService.findByIdWithAttachments(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found for editing: " + id));
        model.addAttribute("announcement", announcement);
        return "announcement-form";
    }

    /**
     * 處理「刪除公告」的請求
     * 
     * @param id 從 URL 路徑中取得的公告 ID
     * @return
     */
    @PostMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteById(id);
        return "redirect:/announcements/list";
    }

    /**
     * 顯示單一公告的詳細內容頁面
     * 
     * @param id    從 URL 路徑中取得的公告 ID
     * @param model
     * @return
     */
    @GetMapping("/view/{id}")
    public String viewAnnouncement(@PathVariable("id") Long id, Model model) {
        // 改為呼叫 Service 的新方法，並處理 Optional
        Announcement announcement = announcementService.findByIdWithAttachments(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found: " + id)); // 或導向 404 頁面
        model.addAttribute("announcement", announcement);
        return "announcement-details";
    }
}
package com.company.cas.controller;

import com.company.cas.model.Attachment;
import com.company.cas.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable("id") Long id) {
        // 1. 從資料庫讀取附件資訊
        Attachment attachment = attachmentService.findById(id);

        if (attachment != null) {
            try {
                // 2. 根據儲存的路徑，讀取實體檔案
                Path filePath = Paths.get(attachment.getStoredPath());
                byte[] fileContent = Files.readAllBytes(filePath);

                // 3. 設定 HTTP Headers，這是讓瀏覽器觸發「下載」的關鍵
                HttpHeaders headers = new HttpHeaders();

                // 設定檔案的 MIME Type
                headers.setContentType(MediaType.parseMediaType(attachment.getFileType()));

                // 設定 Content-Disposition，並對中文檔名進行編碼
                String encodedFilename = new String(attachment.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1");
                headers.setContentDispositionFormData("attachment", encodedFilename);

                // 4. 回傳包含檔案內容和 Headers 的 ResponseEntity
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

            } catch (IOException e) {
                e.printStackTrace();
                // 實際專案中應該回傳一個更友善的錯誤頁面
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // 如果找不到檔案，回傳 404
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
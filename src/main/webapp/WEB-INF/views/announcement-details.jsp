<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>公告內容 - <c:out value="${announcement.title}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header">
                <%-- 顯示標題 --%>
                <h1><c:out value="${announcement.title}" /></h1>
                <%-- 顯示發布者和日期等資訊 --%>
                <p class="text-muted mb-0">
                    由 <strong><c:out value="${announcement.publisherName}" /></strong> 發布於 
                    <fmt:formatDate value="${announcement.publishDate}" pattern="yyyy-MM-dd HH:mm" />
                </p>
            </div>
            <div class="card-body">
                <%-- 顯示公告內容 --%>
                <%-- 重要：escapeXml="false" 才能讓 CKEditor 儲存的 HTML 格式正確顯示 --%>
                <div class="mt-4">
                    <c:out value="${announcement.content}" escapeXml="false" />
                </div>

                <%-- 顯示附件列表 --%>
                <c:if test="${not empty announcement.attachments}">
                    <hr/>
                    <h5 class="mt-4">附件下載</h5>
                    <ul class="list-group">
                        <c:forEach var="att" items="${announcement.attachments}">
                            <li class="list-group-item">
                                <a href="<c:url value='/attachments/download/${att.id}' />">
                                    <c:out value="${att.originalFilename}" />
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>
            <div class="card-footer text-center">
                <a href="<c:url value='/announcements/list' />" class="btn btn-primary">返回列表</a>
            </div>
        </div>
    </div>
</body>
</html>
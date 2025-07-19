<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>公司公告系統</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2 class="mb-3">公告列表</h2>
        
        <div class="mb-3">
            <a href="<c:url value='/announcements/add' />" class="btn btn-primary">新增公告</a>
        </div>
        
        <table class="table table-hover table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>標題</th>
                    <th>發布者</th>
                    <th>發布日期</th>
                    <th>截止日期</th>
                    <th style="width: 15%;">操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="ann" items="${announcements}">
                    <tr>
                        <td><c:out value="${ann.title}" /></td>
                        <td><c:out value="${ann.publisherName}" /></td>
                        <td><fmt:formatDate value="${ann.publishDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td><fmt:formatDate value="${ann.endDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td>
                            <a href="<c:url value='/announcements/edit/${ann.id}' />" class="btn btn-sm btn-warning">修改</a>
                            <a href="<c:url value='/announcements/delete/${ann.id}' />" class="btn btn-sm btn-danger" 
                               onclick="return confirm('您確定要刪除這則公告嗎？')">刪除</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty announcements}">
                    <tr>
                        <td colspan="5" class="text-center">目前沒有任何公告。</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                                    <td>
                                        <%-- 將標題變成一個連結，指向我們剛剛建立的 view 頁面 --%>
                                            <a href="<c:url value='/announcements/view/${ann.id}' />">
                                                <c:out value="${ann.title}" />
                                            </a>
                                            <c:if test="${not empty ann.attachments}">
                                                <div class="mt-2">
                                                    <small>
                                                        <strong>附件:</strong>
                                                        <c:forEach var="att" items="${ann.attachments}">
                                                            <a href="<c:url value='/attachments/download/${att.id}' />"
                                                                class="badge bg-info text-dark me-1"
                                                                style="text-decoration: none;">
                                                                <c:out value="${att.originalFilename}" />
                                                            </a>
                                                        </c:forEach>
                                                    </small>
                                                </div>
                                            </c:if>
                                    </td>
                                    <td>
                                        <c:out value="${ann.publisherName}" />
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${ann.publishDate}" pattern="yyyy-MM-dd HH:mm" />
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${ann.endDate}" pattern="yyyy-MM-dd HH:mm" />
                                    </td>
                                    <td>
                                        <a href="<c:url value='/announcements/edit/${ann.id}' />"
                                            class="btn btn-sm btn-warning">修改</a>
                                        <form action="<c:url value='/announcements/delete/${ann.id}' />" method="post"
                                            style="display:inline;" onsubmit="return confirm('您確定要刪除這則公告嗎？');">
                                            <button type="submit" class="btn btn-sm btn-danger">刪除</button>
                                            <%-- 之後若啟用 Spring Security，CSRF token 會加在這裡 --%>
                                        </form>
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
                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">

                            <%-- 上一頁按鈕 --%>
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link"
                                        href="<c:url value='/announcements/list?page=${currentPage - 1}' />">上一頁</a>
                                </li>

                                <%-- 產生頁碼連結 --%>
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link"
                                                href="<c:url value='/announcements/list?page=${i}' />">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <%-- 下一頁按鈕 --%>
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link"
                                                href="<c:url value='/announcements/list?page=${currentPage + 1}' />">下一頁</a>
                                        </li>

                        </ul>
                    </nav>

                </div>
            </body>

            </html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>${empty announcement.id ? '新增' : '修改'}公告</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

                <script src="https://cdn.ckeditor.com/4.22.1/standard/ckeditor.js"></script>

            </head>

            <body>
                <div class="container mt-4">
                    <h3>${empty announcement.id ? '新增公佈事項' : '編輯公佈事項'}</h3>

                    <form:form modelAttribute="announcement"
                        action="${pageContext.request.contextPath}/announcements/save" method="post"
                        enctype="multipart/form-data">

                        <form:hidden path="id" />

                        <table class="table table-bordered">
                            <tbody>
                                <tr>
                                    <td class="bg-light w-25">標題:</td>
                                    <td>
                                        <form:input path="title" cssClass="form-control" required="true" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="bg-light">發佈日期:</td>
                                    <td>
                                        <form:input type="datetime-local" path="publishDate" cssClass="form-control"
                                            required="true" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="bg-light">截止日期:</td>
                                    <td>
                                        <form:input type="datetime-local" path="endDate" cssClass="form-control" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="bg-light">公佈者:</td>
                                    <td>
                                        <form:input path="publisherName" cssClass="form-control" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="bg-light">公佈內容:</td>
                                    <td>
                                        <%-- ▼▼▼ 我們將原本的 form:textarea 換成這個 ▼▼▼ --%>
                                            <textarea name="content"
                                                id="contentEditor">${announcement.content}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="bg-light">上傳附件:</td>
                                    <td>
                                        <%-- `multiple="true" ` 允許一次選擇多個檔案 --%>
                                            <input type="file" name="files" class="form-control" multiple="true" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <div class="mt-3 text-center">
                            <button type="submit" class="btn btn-primary">儲存</button>
                            <a href="<c:url value='/announcements/list' />" class="btn btn-secondary">取消</a>
                        </div>

                    </form:form>
                </div>

                <%-- ▼▼▼ 在 body 結尾加入這段 JavaScript 來啟用編輯器 ▼▼▼ --%>
                    <script>
                        CKEDITOR.replace('contentEditor');
                    </script>
            </body>

            </html>
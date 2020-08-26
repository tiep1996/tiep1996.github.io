<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Thông báo!</title>
</head>

<body>
<p>Dear All,</p>
<p>Nhân sự quản lý dự án ${codeProject}- ${nameProject} vừa có sự thay đổi.</p>
<#if pmChange>
    <p>${pmNew} được bổ nhiệm vào vị trí PM Manager thay cho ${pmOld}</p>
</#if>
<#if baChange>
    <p>${baNew} được bổ nhiệm vào vị trí BA Manager thay cho ${baOld}</p>
</#if>
<#if tlChange>
    <p>${tlNew} được bổ nhiệm vào vị trí Test Lead thay cho ${tlOld}</p>
</#if>
<#if qaChange>
    <p>${qaNew} được bổ nhiệm vào vị trí QA Manager thay cho ${qaOld}</p>
</#if>

<p>Vai trò mới quản lý dự án:</p>
<table border="1px solid black" cellspacing="0" style="text-align: center">
    <tr style="background-color: #a0c3ff">
        <td>Vai trò</td>
        <td>PM/Team lead</td>
        <td>BA Manager</td>
        <td>Test Leader</td>
        <td>QA Manager</td>
    </tr>
    <tr >
        <td>Họ và tên</td>
        <td>${namePM}</td>
        <td>${nameBA}</td>
        <td>${nameTestLead}</td>
        <td>${nameQA}</td>
    </tr>
</table>
<p>Trân trọng!</p>
</body>
</html>

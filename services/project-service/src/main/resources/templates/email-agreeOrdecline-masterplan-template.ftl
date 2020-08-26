<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Thông báo!</title>
</head>

<body>
<p>Dear All,</p>
<p>Master Plan của dự án ${codeProject} - ${nameProject} vừa được cập nhật.</p>
<#if pmShow>
    <#if pmChange>
        <p>PM Manager vừa đồng ý với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
    <#else>
        <p>PM Manager vừa từ chối với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
        <p>Lý do từ chối: ${pmReason}</p>
    </#if>
</#if>
<#if baShow>
    <#if baChange>
        <p>BA Manager vừa đồng ý với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
    <#else>
        <p>BA Manager vừa từ chối với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
        <p>Lý do từ chối: ${baReason}</p>
    </#if>
</#if>
<#if tlShow>
    <#if tlChange>
        <p>Test Lead vừa đồng ý với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
    <#else>
        <p>Test Lead vừa từ chối với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
        <p>Lý do từ chối: ${tlReason}</p>
    </#if>
</#if>
<#if qaShow>
    <#if qaChange>
        <p>QA Manager vừa đồng ý với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
    <#else>
        <p>QA Manager vừa từ chối với Master Plan của dự án: ${codeProject} - ${nameProject}</p>
        <p>Lý do từ chối: ${qaReason}</p>
    </#if>
</#if>
<p>Anh,chị vui lòng truy cập vào trang chủ của dự án để xác nhận.</p>
<p>Trân trọng!</p>
</body>
</html>

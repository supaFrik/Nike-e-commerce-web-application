<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<div
        id="appRuntime"
        hidden
        data-app-ctx="${env}"
        data-csrf-token="${_csrf.token}"
        data-csrf-header="${_csrf.headerName}">
</div>
<script src="${env}/js/common/runtime.js"></script>
<script src="${env}/js/common/mobile-nav.js"></script>
<script src="${env}/js/customer/components/header.js"></script>
<script src="${env}/js/customer/components/footer.js"></script>
<script src="${env}/js/customer/components/overlay-panels.js"></script>

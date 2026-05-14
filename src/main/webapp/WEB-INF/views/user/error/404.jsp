<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found - Nike</title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <link rel="stylesheet" href="${env}/css/customer/error/not-found.css">
</head>
<body>

<main class="not-found-page" role="main" aria-labelledby="not-found-title">
    <section class="not-found-hero">
        <div class="container">
            <div class="not-found-shell">
                <div>
                    <span class="not-found-kicker">404</span>
                    <h1 class="not-found-title" id="not-found-title">Page not found</h1>
                    <p class="not-found-copy">
                        The page you are looking for may have moved, expired, or never existed.
                        Head back home or continue exploring the latest products.
                    </p>
                    <div class="not-found-actions">
                        <a class="not-found-btn primary" href="${env}/">Go back home</a>
                        <a class="not-found-btn" href="${env}/products/list">Browse products</a>
                    </div>
                </div>
                <div class="not-found-marker" aria-hidden="true">404</div>
            </div>
        </div>
    </section>
</main>

</body>
</html>

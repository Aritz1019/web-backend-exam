<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose>
	<c:when test="${empty requestScope.advertisement}">
		<c:set var="pageTitle" scope="request" value="createAdvertisement"/>
		<c:set var="action" scope="page" value="/advertisement/create"/>
		<c:set var="title" scope="page" value=""/>
		<c:set var="src" scope="page" value=""/>
        <c:set var="url" scope="page" value=""/>
        <c:set var="addLang" scope="page" value="${requestScope.lang.language}"/>
        <c:set var="sessionLang" scope="page" value="${requestScope.langTag}"/>
	</c:when>
	<c:otherwise>
		<c:set var="pageTitle" scope="request" value="editAdvertisement"/>
		<c:set var="action" scope="page" value="/advertisement/${requestScope.advertisement.advertismentId}/edit"/>
		<c:set var="title" scope="page" value="${requestScope.advertisement.title}"/>
		<c:set var="src" scope="page" value="${requestScope.advertisement.src}"/>
        <c:set var="url" scope="page" value="${requestScope.advertisement.url}"/>
        <c:set var="addLang" scope="page" value="${requestScope.advertisement.locale.language}"/>
        <c:set var="sessionLang" scope="page" value="${requestScope.langTag}"/>
	</c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/includes/header.jsp"/>
<jsp:include page="/WEB-INF/includes/advertisements.jsp"/>
<main class="centered-content">
	<fmt:bundle basename="resources.Labels">
	<form class="card" action="${action}" method="post">
		<h2 class="card-title"><fmt:message key="${requestScope.pageTitle}"/></h2>
		<div class="card-body">
			<label>
				<fmt:message key="title"/>:
				<input	type="text"
						name="title"
						value="${title}"
						placeholder="<fmt:message key='title'/>" />
			</label>
			<label>
				<fmt:message key="src"/>:
				<input	type="text"
						name="src"
						value="${src}"
						placeholder="<fmt:message key='title'/>" />
			</label>
            <label>
				<fmt:message key="url"/>:
				<input	type="text"
						name="url"
						value="${url}"
						placeholder="<fmt:message key='title'/>" />
			</label>
            <p><fmt:message key="advertLang"/>: ${addLang}</p>
            <p><fmt:message key="sessionLang"/>: ${sessionLang}</p>
			<button type="submit"><fmt:message key="save"/></button>
		</div>
	</form>
	</fmt:bundle>
</main>
<jsp:include page="/WEB-INF/includes/footer.jsp"/>
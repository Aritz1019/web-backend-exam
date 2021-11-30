<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<link rel="stylesheet" href="/css/be-exam-2021-2022.css">
<fmt:bundle basename="resources.Labels">
<aside id="advertisements">
    <c:if test="${not empty sessionScope.user}">
        <a class="button" href="/advertisement/create"><fmt:message key="createAdvertisement"/></a>
    </c:if>

		<c:forEach items="${applicationScope.advertisements}" var="advertisement">
            <c:if test="${advertisement.locale.language eq sessionScope.locale.language}">
                <div class="advertisement">
                    <a href="${advertisement.url}" class="advertisement__link">${advertisement.title}</a>
                    <img class="advertisement__image" src="${advertisement.src}"
                    alt="Mondragon University">
    
                    <c:if test="${not empty sessionScope.user}">
                        <div class="advertisement__actions">
                            <a href="/advertisement/${advertisement.advertismentId}/edit" class="button"><fmt:message key="editAdvertisement"/></a>
                            <a href="/advertisement/${advertisement.advertismentId}/delete" class="button"><fmt:message key="deleteAdvertisement"/></a>
                        </div><!-- only if permitted -->
                    </c:if>
                </div>
            </c:if>
        </c:forEach>
   </fmt:bundle>
</aside>
   
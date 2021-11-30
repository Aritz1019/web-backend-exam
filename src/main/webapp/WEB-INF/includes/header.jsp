<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<c:set var="browserLanguageLong" scope="page" value="${ fn:split(header['Accept-Language'], ',')[0] }" />
<c:set var="browserLanguage" scope="page" value="${ fn:split(browserLanguageLong, ';')[0] }" />
<c:set var="fmtLanguage" scope="page" value="${sessionScope['javax.servlet.jsp.jstl.fmt.locale.session']}" />

<!DOCTYPE html>
<c:choose>
  <c:when test="${not empty fmtLanguage}">
    <html lang="${fmtLanguage}">
  </c:when>
  <c:otherwise>
    <html lang="${browserLanguage}">
  </c:otherwise>
</c:choose>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="/css/be-exam-2021-2022.css">
  <link rel="stylesheet" href="/css/style.css" />
  <link rel="icon" href="/images/mu_light.svg" type="image/svg+xml" />
  <fmt:bundle basename="resources.Labels">
    <title>MVC Exercise 3 -
      <fmt:message key="${requestScope.pageTitle}" />
    </title>
  </fmt:bundle>
</head>

<body>
  <header>
    <fmt:bundle basename="resources.Labels">
      <h1>MVC Exercise 3 -
        <fmt:message key="${requestScope.pageTitle}" />
      </h1>
      <nav>
        <div id="left-nav">
          <a href="/" class="${requestScope.pageTitle=='home' || requestScope.pageTitle=='User' ? 'current' : ''}"
            id="home-nav">
            <fmt:message key="home" /></a>

          <c:if test="${not empty sessionScope.user}">
            <a href="/user/list"
              class="${requestScope.pageTitle=='userList' ? 'current' : ''}"
              id="user-list-nav">
              <fmt:message key="userList" /></a>
          </c:if>
          <a href="/user/create"
            class="${requestScope.pageTitle=='createUser' ? 'current' : ''}"
            id="create-user-nav">
            <fmt:message key="createUser" /></a>

          <a href="/news/list"
            class="${requestScope.pageTitle=='newsList' ? 'current' : ''}"
            id="news-list-nav">
            <fmt:message key="newsList" /></a>
          <c:if test="${not empty sessionScope.user}">
            <a href="/news/create"
              class="${requestScope.pageTitle=='createNewsItem' ? 'current' : ''}"
              id="create-news-item-nav">
              <fmt:message key="createNewsItem" /></a>
          </c:if>
        </div>
        <div id="right-nav">
          <a href="/lang?language=eu&country=ES"
            class="${fn:startsWith(sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'],'eu') ? 'current' : '' }"
            id="eu-nav">
            <fmt:message key="language.eu" />
          </a>
          <a href="/lang?language=es&country=ES"
            class="${fn:startsWith(sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'],'es') ? 'current' : '' }"
            id="es-nav">
            <fmt:message key="language.es" />
          </a>
          <a href="/lang?language=en&country=UK"
            class="${fn:startsWith(sessionScope['javax.servlet.jsp.jstl.fmt.locale.session'],'en') ? 'current' : '' }"
            id="en-nav">
            <fmt:message key="language.en" />
          </a>
        </div>
      </nav>
    </fmt:bundle>
    <fmt:bundle basename="resources.Notifications">
      <div id="notifications">
        <c:if test="${not empty sessionScope.error}">
          <p class="error">
            <fmt:message key="${sessionScope.error}" />
          </p>
          <c:remove var="error" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.message}">
          <p class="message">
            <fmt:message key="${sessionScope.message}" />
          </p>
          <c:remove var="message" scope="session" />
        </c:if>
      </div>
    </fmt:bundle>
  </header>
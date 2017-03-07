<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>
<jsp:useBean id="lessons" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Lesson>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Lessons - ${group.id}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <h3>Lessons in group <a href="${pageContext.request.contextPath}/group?id=${group.id}">${group.title}</a></h3>
            <c:choose>
                <c:when test="${empty lessons}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong>No lessons!</strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover table-sm">
                        <thead>
                        <tr>
                            <th>Discipline</th>
                            <th>Teacher</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Start time</th>
                            <th>End time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="lesson" items="${lessons}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/discipline?id=${lesson.course.discipline.id}">${lesson.course.discipline.title}</a>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/teacher?id=${lesson.course.teacher.id}">${lesson.course.teacher.firstName} ${lesson.course.teacher.lastName}</a>
                                </td>
                                <td>${lesson.type}</td>
                                <td>${lesson.date}</td>
                                <td>${lesson.order.startTime}</td>
                                <td>${lesson.order.endTime}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
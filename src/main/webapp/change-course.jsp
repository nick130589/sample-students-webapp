<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="course" scope="request" type="com.sergeev.studapp.model.Course"/>
<jsp:useBean id="disciplines" scope="request" type="java.util.List<com.sergeev.studapp.model.Discipline>"/>
<jsp:useBean id="groups" scope="request" type="java.util.List<com.sergeev.studapp.model.Group>"/>
<jsp:useBean id="teachers" scope="request" type="java.util.List<com.sergeev.studapp.model.User>"/>

<c:set var="title" scope="request" value="Change course in group ${course.group}"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="${pageContext.request.contextPath}/course" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" class="form-control" value="${course.id}">
                </div>
                <div class="form-group">
                    <label>Discipline
                        <select class="form-control" name="discipline">
                            <c:forEach items="${disciplines}" var="discipline">
                                <option value="${discipline.id}" ${discipline.id == course.discipline.id ? 'selected="selected"' : ''}>${discipline.title}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <label>Group
                        <select class="form-control" name="group">
                            <c:forEach items="${groups}" var="group">
                                <option value="${group.id}" ${group.id == course.group.id ? 'selected="selected"' : ''}>${group.title}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <label>Teacher
                        <select class="form-control" name="teacher">
                            <c:forEach items="${teachers}" var="teacher">
                                <option value="${teacher.id}" ${teacher.id == course.teacher.id ? 'selected="selected"' : ''}>${teacher.firstName} ${teacher.lastName}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request" value="Add a new teacher"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-3">
            <form action="${pageContext.request.contextPath}/teacher" method="POST">
                <div class="form-group">
                    <input type="hidden" name="action" value="save">
                    <br>
                    <label>Add a new teacher</label>
                    <input type="text" name="first-name" class="form-control" placeholder="First name...">
                </div>
                <div class="form-group">
                    <input type="text" name="last-name" class="form-control" placeholder="Last name...">
                </div>
                <div class="form-group">
                    <input type="hidden" name="type" class="form-control" value="${2}">
                </div>
                <button type="submit" class="btn btn-block btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>
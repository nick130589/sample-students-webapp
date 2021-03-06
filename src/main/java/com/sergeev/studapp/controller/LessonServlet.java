package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;
import com.sergeev.studapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@WebServlet(name = "LessonServlet", urlPatterns = {"/lesson", "/lesson/*"})
public class LessonServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LessonServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        final String action = request.getParameter("action");

        Integer id;
        LocalDate date;
        Integer order;
        Integer disciplineId;
        Integer groupId;
        String type;

        if (path.matches("^/lesson/?")) {

            if ("save".equals(action)) {
                groupId = Integer.valueOf(request.getParameter("group"));
                disciplineId = Integer.valueOf(request.getParameter("discipline"));
                type = request.getParameter("type");
                order = Integer.valueOf(request.getParameter("number"));
                date = LocalDate.parse(request.getParameter("date"));
                try {
                    LessonService.save(groupId, disciplineId, type, order, date);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be created.", e);
                    e.printStackTrace();
                    response.sendRedirect("/lesson/new/group/"+groupId);
                    return;
                }
                LOG.info("Lesson successfully created.");
                response.sendRedirect("lesson/group/"+groupId);
                return;

            } else if ("update".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                groupId = Integer.valueOf(request.getParameter("group"));
                disciplineId = Integer.valueOf(request.getParameter("discipline"));
                type = request.getParameter("type");
                order = Integer.valueOf(request.getParameter("number"));
                date = LocalDate.parse(request.getParameter("date"));
                try {
                    LessonService.update(groupId, disciplineId, type, order, date, id);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be updated.", e);
                    response.sendRedirect("/lesson/" + id + "/change");
                    return;
                }
                LOG.info("Lesson successfully updated.");
                response.sendRedirect("lesson/group/"+groupId);
                return;

            } else if ("remove".equals(action)) {
                id = Integer.valueOf(request.getParameter("id"));
                try {
                    groupId = LessonService.get(id).getCourse().getGroup().getId();
                    LessonService.remove(id);
                } catch (ApplicationException e) {
                    LOG.info("Lesson cannot be deleted, because lesson doesn't exist.", e);
                    response.sendRedirect("/");
                    return;
                }
                LOG.info("Lesson successfully deleted.");
                response.sendRedirect("lesson/group/"+groupId);
                return;
            }
        }
        response.sendRedirect("/");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getRequestURI().substring(request.getContextPath().length());

        Integer groupId;
        Group group;
        Lesson lesson;
        List<Course> courses;
        List<Lesson> lessons;
        LocalDate dateNow = LocalDate.now(ZoneOffset.ofHours(2));
        Lesson.Type[] types;
        Lesson.Order[] orders;
        List<Mark> marks;

        if (path.matches("^/lesson/group/[^/]+/?")) {
            groupId= Integer.valueOf(path.split("/")[3]);
            try {
                group = GroupService.get(groupId);
                lessons = LessonService.getAll(groupId);
            } catch (ApplicationException e) {
                LOG.info("Lessons in group not found, because group doesn't exist.", e);
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("group", group);
            request.setAttribute("dateNow", dateNow);
            request.setAttribute("lessons", lessons);
            request.getRequestDispatcher("/lessons.jsp").forward(request, response);
            return;
        }

        if (path.matches("^/lesson/new/group/[^/]+/?")) {
            groupId= Integer.valueOf(path.split("/")[4]);
            types = Lesson.Type.values();
            orders = Lesson.Order.values();
            try {
                group = GroupService.get(groupId);
                courses = CourseService.getByGroup(groupId);
            } catch (ApplicationException e) {
                LOG.info("Lessons cannot be created, because group doesn't exist.", e);
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("types", types);
            request.setAttribute("orders", orders);
            request.setAttribute("group", group);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/add-lesson.jsp").forward(request, response);
            return;
        }
        if (path.matches("^/lesson/[^/]+/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                lesson = LessonService.get(id);
                marks = MarkService.getByLesson(id);
            } catch (ApplicationException e) {
                LOG.info("Lesson not found.");
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("lesson", lesson);
            request.setAttribute("marks", marks);
            request.setAttribute("dateNow", dateNow);
            request.getRequestDispatcher("/lesson.jsp").forward(request, response);
            return;
        }


        if (path.matches("^/lesson/[^/]+/change/?")) {
            Integer id = Integer.valueOf(path.split("/")[2]);
            try {
                lesson = LessonService.get(id);
            } catch (ApplicationException e) {
                LOG.info("Lesson cannot be updated, because lesson doesn't exist.", e);
                response.sendRedirect("/lesson");
                return;
            }
            types = Lesson.Type.values();
            orders = Lesson.Order.values();
            request.setAttribute("lesson", lesson);
            request.setAttribute("types", types);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/change-lesson.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }
}
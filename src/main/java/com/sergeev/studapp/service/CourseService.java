package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.*;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

public class CourseService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static CourseDao courseDao = daoFactory.getCourseDao();
    private static DisciplineDao disciplineDao = daoFactory.getDisciplineDao();
    private static GroupDao groupDao = daoFactory.getGroupDao();
    private static UserDao userDao = daoFactory.getUserDao();

    public static Course create(String disciplineId, String groupId, String teacherId) {
        Course course = new Course();

        try {
            Discipline discipline = disciplineDao.getById(disciplineId);
            Group group = groupDao.getById(groupId);
            User teacher = userDao.getById(teacherId);

            course.setDiscipline(discipline);
            course.setGroup(group);
            course.setTeacher(teacher);

            course = courseDao.persist(course);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static Course update(String disciplineId, String groupId, String teacherId, String courseId) {
        Course course = new Course();
        course.setId(courseId);

        try {
            Discipline discipline = disciplineDao.getById(disciplineId);
            Group group = groupDao.getById(groupId);
            User teacher = userDao.getById(teacherId);

            course.setDiscipline(discipline);
            course.setGroup(group);
            course.setTeacher(teacher);

            courseDao.update(course);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return course;
    }

    public static void delete(String courseId){
        try {
            courseDao.delete(courseId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }
}

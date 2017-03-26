package com.sergeev.studapp.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDaoFactory extends DaoFactory {

    private static final String DATABASE;
    private static final String ADDRESS;
    private static final int PORT;
    private static final String USER;
    private static final String PASSWORD;

    private static final String PROPERTIES_FILE = "db_mongo.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = classLoader.getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DATABASE = PROPERTIES.getProperty("database");
        ADDRESS = PROPERTIES.getProperty("address");
        PORT = Integer.parseInt(PROPERTIES.getProperty("port"));
        USER = PROPERTIES.getProperty("username");
        PASSWORD = PROPERTIES.getProperty("password");
    }

    static MongoDatabase getConnection() {
        MongoClient mongoClient = new MongoClient(ADDRESS, PORT);
        return mongoClient.getDatabase(DATABASE);
    }

    @Override
    public AccountDao getAccountDao() {
        return new MongoAccountDao();
    }

    @Override
    public CourseDao getCourseDao() {
        return null;
    }

    @Override
    public DisciplineDao getDisciplineDao() {
        return null;
    }

    @Override
    public GroupDao getGroupDao() {
        return new MongoGroupDao();
    }

    @Override
    public LessonDao getLessonDao() {
        return null;
    }

    @Override
    public MarkDao getMarkDao() {
        return null;
    }

    @Override
    public UserDao getUserDao() {
        return new MongoUserDao();
    }
}

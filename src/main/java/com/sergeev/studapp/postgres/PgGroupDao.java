package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.GROUP_ID;
import static com.sergeev.studapp.model.Constants.TITLE;

public class PgGroupDao extends PgGenericDao<Group> implements GroupDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgGroupDao.class);

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM groups Where group_id = ?";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM groups";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO groups (title) VALUES (?)";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE groups SET title= ? WHERE group_id = ?";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM groups WHERE group_id = ?";
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs, Connection con) {
        List<Group> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Group group = new Group()
                        .setId(rs.getInt(GROUP_ID))
                        .setTitle(rs.getString(TITLE));
                list.add(group);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list.isEmpty()) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, Group group) {
        try {
            st.setString(1, group.getTitle());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Group group) {
        try {
            st.setString(1, group.getTitle());
            st.setInt(2, group.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

}

package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgAccountDao extends PgGenericDao<Account> implements AccountDao {

    protected static final String ACCOUNT_ID = "account_id";
    protected static final String LOGIN = "login";
    protected static final String PASSWORD = "password";
    protected static final String TOKEN = "token";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM accounts WHERE account_id= ?;";
    }

    public String getSelectAllQuery() {
        return "SELECT * FROM accounts";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO accounts (login, password) VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE accounts SET login= ?, password= ?, token= ? WHERE account_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM accounts WHERE account_id= ?;";
    }

    @Override
    protected List<Account> parseResultSet(ResultSet rs) throws PersistentException {
        List<Account> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getString(ACCOUNT_ID));
                account.setLogin(rs.getString(LOGIN));
                account.setPassword(rs.getString(PASSWORD));
                account.setToken(rs.getString(TOKEN));
                result.add(account);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Account object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getToken());
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public Account getByToken(String token) throws PersistentException{
        List<Account> list;
        String sql = "SELECT * FROM accounts WHERE token=?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }
}

package com.sergeev.studapp.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class MongoAccountDao extends MongoGenericDao<Account> implements AccountDao {

    protected static final String LOGIN = "login";
    protected static final String PASSWORD = "password";
    protected static final String TOKEN = "token";

    private Document doc;
    private Account account;
    private MongoCollection<Document> collection;

    @Override
    protected MongoCollection<Document> getCollection(MongoDatabase db) {
        return collection = db.getCollection("accounts");
    }

    @Override
    protected Document getDocument(Account object) {
        return doc = new Document(LOGIN, object.getLogin()).append(PASSWORD, object.getPassword()).append(TOKEN, object.getToken());
    }

    @Override
    protected Account parseDocument(Document doc) throws PersistentException{
        ObjectId oid = (ObjectId) doc.get(ID);
        account.setId(oid.toString());
        account.setLogin(String.valueOf(doc.get(LOGIN)));
        account.setPassword(String.valueOf(doc.get(PASSWORD)));
        account.setToken(String.valueOf(doc.get(TOKEN)));
        return account;
    }

    protected Account getByToken(String token) throws PersistentException {
        doc = collection.find(eq(TOKEN, token)).first();
        if (doc == null) {
            throw new PersistentException();
        }
        return parseDocument(doc);
    }

    protected Account getByLogin(String login, String password) throws PersistentException {
        List<Account> list = new ArrayList<>();
        Pattern patternLogin = Pattern.compile(login, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        Pattern patternPassword = Pattern.compile(password, Pattern.UNICODE_CHARACTER_CLASS);
        DBObject query1 = QueryBuilder.start(LOGIN).regex(patternLogin).and(PASSWORD).regex(patternPassword).get();
        BasicDBList or = new BasicDBList();
        or.add(query1);
        BasicDBObject query = new BasicDBObject("$or", or);

        Block<Document> documents = doc -> {
            Account item = null;
            try {
                item = parseDocument(doc);
            } catch (PersistentException e) {
                e.printStackTrace();
            }
            list.add(item);
        };
        collection.find(query).forEach(documents);
        return list.listIterator().next();
    }
}
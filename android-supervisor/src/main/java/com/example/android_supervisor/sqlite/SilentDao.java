package com.example.android_supervisor.sqlite;//package com.example.android_supervisor.sqlite;
//
//import com.j256.ormlite.dao.CloseableIterator;
//import com.j256.ormlite.dao.CloseableWrappedIterable;
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.dao.DatabaseResultsMapper;
//import com.j256.ormlite.dao.ForeignCollection;
//import com.j256.ormlite.dao.GenericRawResults;
//import com.j256.ormlite.dao.ObjectCache;
//import com.j256.ormlite.dao.RawRowMapper;
//import com.j256.ormlite.dao.RawRowObjectMapper;
//import com.j256.ormlite.field.DataType;
//import com.j256.ormlite.field.FieldType;
//import com.j256.ormlite.logger.Log;
//import com.j256.ormlite.logger.Logger;
//import com.j256.ormlite.stmt.DeleteBuilder;
//import com.j256.ormlite.stmt.GenericRowMapper;
//import com.j256.ormlite.stmt.PreparedDelete;
//import com.j256.ormlite.stmt.PreparedQuery;
//import com.j256.ormlite.stmt.PreparedUpdate;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.stmt.UpdateBuilder;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.support.DatabaseConnection;
//import com.j256.ormlite.support.DatabaseResults;
//import com.j256.ormlite.table.ObjectFactory;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Callable;
//
///**
// * @author wujie
// */
//public class SilentDao<T, ID> implements Dao<T, ID> {
//    private Dao<T, ID> dao;
//
//    public SilentDao(Dao<T, ID> dao) {
//        this.dao = dao;
//    }
//
//    public T queryForId(ID id) {
//        try {
//            return this.dao.queryForId(id);
//        } catch (SQLException e) {
//
//            this.logMessage(var3, "queryForId threw exception on: " + id);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public T queryForFirst(PreparedQuery<T> preparedQuery) {
//        try {
//            return this.dao.queryForFirst(preparedQuery);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForFirst threw exception on: " + preparedQuery);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public List<T> queryForAll() {
//        try {
//            return this.dao.queryForAll();
//        } catch (SQLException var2) {
//            this.logMessage(var2, "queryForAll threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public List<T> queryForEq(String fieldName, Object value) {
//        try {
//            return this.dao.queryForEq(fieldName, value);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "queryForEq threw exception on: " + fieldName);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public List<T> queryForMatching(T matchObj) {
//        try {
//            return this.dao.queryForMatching(matchObj);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForMatching threw exception on: " + matchObj);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public List<T> queryForMatchingArgs(T matchObj) {
//        try {
//            return this.dao.queryForMatchingArgs(matchObj);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForMatchingArgs threw exception on: " + matchObj);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public List<T> queryForFieldValues(Map<String, Object> fieldValues) {
//        try {
//            return this.dao.queryForFieldValues(fieldValues);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForFieldValues threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public List<T> queryForFieldValuesArgs(Map<String, Object> fieldValues) {
//        try {
//            return this.dao.queryForFieldValuesArgs(fieldValues);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForFieldValuesArgs threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public T queryForSameId(T data) {
//        try {
//            return this.dao.queryForSameId(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "queryForSameId threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public QueryBuilder<T, ID> queryBuilder() {
//        return this.dao.queryBuilder();
//    }
//
//    public UpdateBuilder<T, ID> updateBuilder() {
//        return this.dao.updateBuilder();
//    }
//
//    public DeleteBuilder<T, ID> deleteBuilder() {
//        return this.dao.deleteBuilder();
//    }
//
//    public List<T> query(PreparedQuery<T> preparedQuery) {
//        try {
//            return this.dao.query(preparedQuery);
//        } catch (SQLException e) {
//            this.logMessage(var3, "query threw exception on: " + preparedQuery);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int create(T data) {
//        try {
//            return this.dao.create(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "create threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int create(Collection<T> datas) {
//        try {
//            return this.dao.create(datas);
//        } catch (SQLException e) {
//            this.logMessage(var3, "create threw exception on: " + datas);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public T createIfNotExists(T data) {
//        try {
//            return this.dao.createIfNotExists(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "createIfNotExists threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public CreateOrUpdateStatus createOrUpdate(T data) {
//        try {
//            return this.dao.createOrUpdate(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "createOrUpdate threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int update(T data) {
//        try {
//            return this.dao.update(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "update threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int updateId(T data, ID newId) {
//        try {
//            return this.dao.updateId(data, newId);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "updateId threw exception on: " + data);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public int update(PreparedUpdate<T> preparedUpdate) {
//        try {
//            return this.dao.update(preparedUpdate);
//        } catch (SQLException e) {
//            this.logMessage(var3, "update threw exception on: " + preparedUpdate);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int refresh(T data) {
//        try {
//            return this.dao.refresh(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "refresh threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int delete(T data) {
//        try {
//            return this.dao.delete(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "delete threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int deleteById(ID id) {
//        try {
//            return this.dao.deleteById(id);
//        } catch (SQLException e) {
//            this.logMessage(var3, "deleteById threw exception on: " + id);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int delete(Collection<T> datas) {
//        try {
//            return this.dao.delete(datas);
//        } catch (SQLException e) {
//            this.logMessage(var3, "delete threw exception on: " + datas);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int deleteIds(Collection<ID> ids) {
//        try {
//            return this.dao.deleteIds(ids);
//        } catch (SQLException e) {
//            this.logMessage(var3, "deleteIds threw exception on: " + ids);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int delete(PreparedDelete<T> preparedDelete) {
//        try {
//            return this.dao.delete(preparedDelete);
//        } catch (SQLException e) {
//            this.logMessage(var3, "delete threw exception on: " + preparedDelete);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public CloseableIterator<T> iterator() {
//        return this.dao.iterator();
//    }
//
//    public CloseableIterator<T> closeableIterator() {
//        return this.dao.closeableIterator();
//    }
//
//    public CloseableIterator<T> iterator(int resultFlags) {
//        return this.dao.iterator(resultFlags);
//    }
//
//    public CloseableWrappedIterable<T> getWrappedIterable() {
//        return this.dao.getWrappedIterable();
//    }
//
//    public CloseableWrappedIterable<T> getWrappedIterable(PreparedQuery<T> preparedQuery) {
//        return this.dao.getWrappedIterable(preparedQuery);
//    }
//
//    public void closeLastIterator() {
//        try {
//            this.dao.closeLastIterator();
//        } catch (IOException var2) {
//            this.logMessage(var2, "closeLastIterator threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery) {
//        try {
//            return this.dao.iterator(preparedQuery);
//        } catch (SQLException e) {
//            this.logMessage(var3, "iterator threw exception on: " + preparedQuery);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery, int resultFlags) {
//        try {
//            return this.dao.iterator(preparedQuery, resultFlags);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "iterator threw exception on: " + preparedQuery);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public GenericRawResults<String[]> queryRaw(String query, String... arguments) {
//        try {
//            return this.dao.queryRaw(query, arguments);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "queryRaw threw exception on: " + query);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public long queryRawValue(String query, String... arguments) {
//        try {
//            return this.dao.queryRawValue(query, arguments);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "queryRawValue threw exception on: " + query);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public <UO> GenericRawResults<UO> queryRaw(String query, RawRowMapper<UO> mapper, String... arguments) {
//        try {
//            return this.dao.queryRaw(query, mapper, arguments);
//        } catch (SQLException var5) {
//            this.logMessage(var5, "queryRaw threw exception on: " + query);
//            throw new RuntimeException(var5);
//        }
//    }
//
//    public <UO> GenericRawResults<UO> queryRaw(String query, DataType[] columnTypes, RawRowObjectMapper<UO> mapper, String... arguments) {
//        try {
//            return this.dao.queryRaw(query, columnTypes, mapper, arguments);
//        } catch (SQLException var6) {
//            this.logMessage(var6, "queryRaw threw exception on: " + query);
//            throw new RuntimeException(var6);
//        }
//    }
//
//    public GenericRawResults<Object[]> queryRaw(String query, DataType[] columnTypes, String... arguments) {
//        try {
//            return this.dao.queryRaw(query, columnTypes, arguments);
//        } catch (SQLException var5) {
//            this.logMessage(var5, "queryRaw threw exception on: " + query);
//            throw new RuntimeException(var5);
//        }
//    }
//
//    public <UO> GenericRawResults<UO> queryRaw(String query, DatabaseResultsMapper<UO> mapper, String... arguments) {
//        try {
//            return this.dao.queryRaw(query, mapper, arguments);
//        } catch (SQLException var5) {
//            this.logMessage(var5, "queryRaw threw exception on: " + query);
//            throw new RuntimeException(var5);
//        }
//    }
//
//    public int executeRaw(String statement, String... arguments) {
//        try {
//            return this.dao.executeRaw(statement, arguments);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "executeRaw threw exception on: " + statement);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public int executeRawNoArgs(String statement) {
//        try {
//            return this.dao.executeRawNoArgs(statement);
//        } catch (SQLException e) {
//            this.logMessage(var3, "executeRawNoArgs threw exception on: " + statement);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public int updateRaw(String statement, String... arguments) {
//        try {
//            return this.dao.updateRaw(statement, arguments);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "updateRaw threw exception on: " + statement);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public <CT> CT callBatchTasks(Callable<CT> callable) {
//        try {
//            return this.dao.callBatchTasks(callable);
//        } catch (Exception var3) {
//            this.logMessage(var3, "callBatchTasks threw exception on: " + callable);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public String objectToString(T data) {
//        return this.dao.objectToString(data);
//    }
//
//    public boolean objectsEqual(T data1, T data2) {
//        try {
//            return this.dao.objectsEqual(data1, data2);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "objectsEqual threw exception on: " + data1 + " and " + data2);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public ID extractId(T data) {
//        try {
//            return this.dao.extractId(data);
//        } catch (SQLException e) {
//            this.logMessage(var3, "extractId threw exception on: " + data);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public Class<T> getDataClass() {
//        return this.dao.getDataClass();
//    }
//
//    public FieldType findForeignFieldType(Class<?> clazz) {
//        return this.dao.findForeignFieldType(clazz);
//    }
//
//    public boolean isUpdatable() {
//        return this.dao.isUpdatable();
//    }
//
//    public boolean isTableExists() {
//        try {
//            return this.dao.isTableExists();
//        } catch (SQLException var2) {
//            this.logMessage(var2, "isTableExists threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public long countOf() {
//        try {
//            return this.dao.countOf();
//        } catch (SQLException var2) {
//            this.logMessage(var2, "countOf threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public long countOf(PreparedQuery<T> preparedQuery) {
//        try {
//            return this.dao.countOf(preparedQuery);
//        } catch (SQLException e) {
//            this.logMessage(var3, "countOf threw exception on " + preparedQuery);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void assignEmptyForeignCollection(T parent, String fieldName) {
//        try {
//            this.dao.assignEmptyForeignCollection(parent, fieldName);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "assignEmptyForeignCollection threw exception on " + fieldName);
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public <FT> ForeignCollection<FT> getEmptyForeignCollection(String fieldName) {
//        try {
//            return this.dao.getEmptyForeignCollection(fieldName);
//        } catch (SQLException e) {
//            this.logMessage(var3, "getEmptyForeignCollection threw exception on " + fieldName);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void setObjectCache(boolean enabled) {
//        try {
//            this.dao.setObjectCache(enabled);
//        } catch (SQLException e) {
//            this.logMessage(var3, "setObjectCache(" + enabled + ") threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public ObjectCache getObjectCache() {
//        return this.dao.getObjectCache();
//    }
//
//    public void setObjectCache(ObjectCache objectCache) {
//        try {
//            this.dao.setObjectCache(objectCache);
//        } catch (SQLException e) {
//            this.logMessage(var3, "setObjectCache threw exception on " + objectCache);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void clearObjectCache() {
//        this.dao.clearObjectCache();
//    }
//
//    public T mapSelectStarRow(DatabaseResults results) {
//        try {
//            return this.dao.mapSelectStarRow(results);
//        } catch (SQLException e) {
//            this.logMessage(var3, "mapSelectStarRow threw exception on results");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public GenericRowMapper<T> getSelectStarRowMapper() {
//        try {
//            return this.dao.getSelectStarRowMapper();
//        } catch (SQLException var2) {
//            this.logMessage(var2, "getSelectStarRowMapper threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public boolean idExists(ID id) {
//        try {
//            return this.dao.idExists(id);
//        } catch (SQLException e) {
//            this.logMessage(var3, "idExists threw exception on " + id);
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public DatabaseConnection startThreadConnection() {
//        try {
//            return this.dao.startThreadConnection();
//        } catch (SQLException var2) {
//            this.logMessage(var2, "startThreadConnection() threw exception");
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public void endThreadConnection(DatabaseConnection connection) {
//        try {
//            this.dao.endThreadConnection(connection);
//        } catch (SQLException e) {
//            this.logMessage(var3, "endThreadConnection(" + connection + ") threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void setAutoCommit(DatabaseConnection connection, boolean autoCommit) {
//        try {
//            this.dao.setAutoCommit(connection, autoCommit);
//        } catch (SQLException var4) {
//            this.logMessage(var4, "setAutoCommit(" + connection + "," + autoCommit + ") threw exception");
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public boolean isAutoCommit(DatabaseConnection connection) {
//        try {
//            return this.dao.isAutoCommit(connection);
//        } catch (SQLException e) {
//            this.logMessage(var3, "isAutoCommit(" + connection + ") threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void commit(DatabaseConnection connection) {
//        try {
//            this.dao.commit(connection);
//        } catch (SQLException e) {
//            this.logMessage(var3, "commit(" + connection + ") threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void rollBack(DatabaseConnection connection) {
//        try {
//            this.dao.rollBack(connection);
//        } catch (SQLException e) {
//            this.logMessage(var3, "rollBack(" + connection + ") threw exception");
//            throw new RuntimeException(var3);
//        }
//    }
//
//    public void setObjectFactory(ObjectFactory<T> objectFactory) {
//        this.dao.setObjectFactory(objectFactory);
//    }
//
//    public RawRowMapper<T> getRawRowMapper() {
//        return this.dao.getRawRowMapper();
//    }
//
//    public ConnectionSource getConnectionSource() {
//        return this.dao.getConnectionSource();
//    }
//
//    public void registerObserver(DaoObserver observer) {
//        this.dao.registerObserver(observer);
//    }
//
//    public void unregisterObserver(DaoObserver observer) {
//        this.dao.unregisterObserver(observer);
//    }
//
//    public void notifyChanges() {
//        this.dao.notifyChanges();
//    }
//
//    public String getTableName() {
//        return this.dao.getTableName();
//    }
//}

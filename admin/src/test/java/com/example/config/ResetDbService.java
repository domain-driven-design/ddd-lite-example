package com.example.config;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的目的是通过每次备份系统初始化的数据（这些初始化数据可能来自 flyway），
 * 来实现每次测试的数据一致的目的。
 */

@Service
public class ResetDbService {

    public static final String ROOT_URL = "build/resources/test/";
    private static IDatabaseConnection conn;

    @Autowired
    private DataSource dataSource;
    private File tempFile;

    public void backUp() throws Exception {
        this.getConnection();
        this.backupCustom(tables());
    }

    public void rollback() throws Exception {
        this.reset();
        this.closeConnection();
    }

    List<String> tables() throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        ArrayList<String> tableNames = new ArrayList<>();
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tableNames.add(tableName);
        }

        connection.close();
        return tableNames;
    }

    protected void backupCustom(List<String> tableName) {
        try {
            QueryDataSet qds = getQueryDataSet();
            for (String str : tableName) {
                qds.addTable(str);
            }

            conn.getConfig().setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN , "`?`");

            tempFile = new File(ROOT_URL + "temp.xml");
            FlatXmlDataSet.write(qds, new FileWriter(tempFile), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected QueryDataSet getQueryDataSet() {
        return new QueryDataSet(conn);
    }

    void getConnection() throws DatabaseUnitException {
        conn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));
    }

    protected void reset() throws FileNotFoundException, DatabaseUnitException, SQLException {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet ds = builder.build(new FileInputStream(tempFile));

        DatabaseOperation.CLEAN_INSERT.execute(conn, ds);
    }

    protected void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}

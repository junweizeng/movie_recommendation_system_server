package cn.zjw.mrs.utils;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.catalina.realm.DataSourceRealm;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;

/**
 * @author zjw
 * @Classname MyDataModel
 * @Date 2022/5/1 22:13
 * @Description
 */
public class MyDataModel {

    public static JDBCDataModel myDataModel() {
        MysqlDataSource dataSource = new MysqlDataSource();
        JDBCDataModel dataModel = null;
        try {
            dataSource.setServerName("rm-xxx.mysql.rds.aliyuncs.com");
            dataSource.setUser("xxx");
            dataSource.setPassword("xxx");
            dataSource.setDatabaseName("mrs");
            dataSource.setServerTimezone("GMT%2B8");
            dataSource.setCharacterEncoding("utf-8");

            System.out.println("1111");
//            ConnectionPoolDataSource connectionPool = new ConnectionPoolDataSource(dataSource);

            dataModel = new MySQLJDBCDataModel(dataSource,
                    "comment",
                    "uid",
                    "mid",
                    "score",
                    "time");

            System.out.println("2222");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataModel;
    }

}

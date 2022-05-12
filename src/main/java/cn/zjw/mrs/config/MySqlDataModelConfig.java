package cn.zjw.mrs.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


/**
 * @author zjw
 * @Classname MySqlDataModelConfig
 * @Date 2022/5/1 20:11
 * @Description
 */

public class MySqlDataModelConfig {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataModel getMySqlDataModel() {
        MysqlDataSource dataSource = new MysqlDataSource();
        JDBCDataModel dataModel = null;

        try {
            dataSource.setServerName("rm-bp11602preg16q6g4jo.mysql.rds.aliyuncs.com");
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setServerTimezone("GMT%2B8");
            dataSource.setUseSSL(false);
            dataSource.setCharacterEncoding("utf-8");

            System.out.println("hello hello hello 1111111");
            System.out.println(dataSource);
            System.out.println();

            dataModel = new MySQLJDBCDataModel(dataSource,
                    "comment",
                    "uid",
                    "mid",
                    "score",
                    "time");

            System.out.println("hello hello hello 1111111");
            System.out.println(dataModel);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataModel;
    }
}

package test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Mysql {

    Connection connection;

    DruidDataSource dataSource;

    @BeforeSuite
    public void init() throws SQLException {
        dataSource = new DruidDataSource();
//        dataSource.setUrl("jdbc:mysql://192.168.30.102:3306/gb32960data?useSSL=false");
        dataSource.setUrl("jdbc:mysql://localhost:3306/gb32960data?useSSL=false");
       // dataSource.setPassword("kevin115");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        connection = dataSource.getConnection();

            try (Statement stmt = connection.createStatement()) {
                String sql = "CREATE TABLE  if NOT exists t_gb32960CompletelyVehicle" + " (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `uid` varchar(17) COLLATE utf8_bin NOT NULL COMMENT 'VIN',\n" +
                        "  `unixtimestamp` int(32) NOT NULL,\n" +
                        "  `vehicleState` int(1) DEFAULT NULL COMMENT '车辆状态，1启动，2熄火，3其他状态，FE/FF表示无效',\n" +
                        "  `chargeState` int(1) DEFAULT NULL COMMENT '充电状态,1停车充电，2行驶充电，3未充电，4充电完成',\n" +
                        "  `runningMode` int(11) DEFAULT NULL COMMENT '运行模式，1纯电，2混动，3，燃油,254/255无效',\n" +
                        "  `speed` float DEFAULT NULL COMMENT '车速，0km/h - 220km/h',\n" +
                        "  `mile` float DEFAULT NULL COMMENT '里程 0km  999.9km\\n',\n" +
                        "  `totalV` float DEFAULT NULL COMMENT '总电雅，单位V',\n" +
                        "  `totalI` float DEFAULT NULL COMMENT '总电流单位A',\n" +
                        "  `soc` int(11) DEFAULT NULL COMMENT '有效范围0~100%\\n',\n" +
                        "  `dcdcState` int(11) DEFAULT NULL COMMENT '1工作，02断开',\n" +
                        "  `gear` int(11) DEFAULT NULL,\n" +
                        "  `insulationR` int(11) DEFAULT NULL,\n" +
                        "  `accelerationPedal` int(11) DEFAULT NULL,\n" +
                        "  `brakePedal` int(11) DEFAULT NULL,\n" +
                        "  `dataTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `uid_UNIQUE` (`uid`)\n" +
                        ") ENGINE=MEMORY AUTO_INCREMENT=9581307 DEFAULT CHARSET=utf8 COLLATE=utf8_bin";

                stmt.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }


//    @Test
//    public void testSelect() throws SQLException {
//
//        try (Statement stmt = connection.createStatement()) {
//            // Create table based on REPLICATED template
//            ResultSet resultSet = stmt.executeQuery("select count(*) from t_gb32960CompletelyVehicle");
//            System.out.println(resultSet);
//        }
//    }


    @Test(threadPoolSize = 5, invocationCount = 5)
    public void TestQps() {



            for (int i = 1; i <= 100; i++) {
                                                // Create table based on REPLICATED template
                try (Connection connection=dataSource.getConnection();Statement stmt = connection.createStatement()) {
                String head = "replace   into t_gb32960CompletelyVehicle" + "(uid,unixtimestamp,vehicleState,chargeState,runningMode,speed,mile,totalV,totalI,soc,dcdcState,gear,insulationR,accelerationPedal,brakePedal,dataTime) values";
                                                StringBuilder sb = new StringBuilder();
                                                for (int j = 0; j <= 10000; j++) {
                                                    if (j == 10000) {

                                                        sb.append(String.format("('spafka%d',123,1,1,1,1,1,1,1,1,1,1,1,1,1, CURRENT_TIMESTAMP()) ", j));
                                                    } else {
                                                        sb.append(String.format("('spafka%d',123,1,1,1,1,1,1,1,1,1,1,1,1,1, CURRENT_TIMESTAMP()), ", j));
                                                    }
                                                }
                                                stmt.execute(head + sb);
                                            } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        System.out.println(Thread.currentThread().getName() + " " + new Date());


    }


    public static void main(String[] args) throws ClassNotFoundException {

        String[] classAndMethod = "test.Mysql#TestQps".split("#");
        Request request = Request.method(Class.forName(classAndMethod[0]),
                classAndMethod[1]);

        Result result = new JUnitCore().run(request);
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
}


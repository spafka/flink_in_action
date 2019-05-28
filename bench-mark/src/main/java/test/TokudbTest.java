package test;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.spafka.spark.util.Utils;
import org.junit.Before;
import org.junit.Test;
import scala.Tuple2;
import scala.runtime.AbstractFunction0;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.stream.IntStream;

public class TokudbTest {


    Connection connection;

    DruidDataSource dataSource;

    @Before
    public void init() throws SQLException {
        dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:mysql://192.168.6.107:3306/tokudb?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        connection = dataSource.getConnection();

        try (Statement stmt = connection.createStatement()) {

            String s = "     CREATE TABLE if NOT exists `test` (\n" +
                    "                `_1` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "                `_2` int(11) DEFAULT NULL,\n" +
                    "        `_3` int(11) DEFAULT NULL,\n" +
                    "        `_4` int(11) DEFAULT NULL,\n" +
                    "        `_5` int(11) DEFAULT NULL,\n" +
                    "        `_6` int(11) DEFAULT NULL,\n" +
                    "        `_7` int(11) DEFAULT NULL,\n" +
                    "        `_8` int(11) DEFAULT NULL,\n" +
                    "        `_9` int(11) DEFAULT NULL,\n" +
                    "        `10` int(11) DEFAULT NULL,\n" +
                    "        PRIMARY KEY (`_1`)\n" +
                    ") ENGINE=TokuDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin";


            stmt.execute(s);

        }
    }

    @Test
    public void insert() {

        IntStream.rangeClosed(0, Integer.MAX_VALUE - 2).forEach(x -> {
            try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {

                Random random = new Random();
                String sql = "INSERT INTO `tokudb`.`test` (`_2`, `_3`, `_4`, `_5`, `_6`, `_7`, `_8`, `_9`, `10`) VALUES ";
                StringBuffer stringBuffer = new StringBuffer(sql);
                for (int j = 0; j <= 10000; j++) {
                    if (j == 10000) {

                        sql = String.format(" (%d, %d, %d, %d, %d,%d ,%d , %d, %d)",
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000));
                    }else {
                        sql = String.format(" (%d, %d, %d, %d, %d,%d ,%d , %d, %d),",
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000),
                                random.nextInt(1000));
                    }

                    stringBuffer.append(sql);
                }

                System.out.println(x);
                stmt.execute(stringBuffer.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void queryUseIndex(){


        Tuple2<Object, Object> tuple2 = Utils.timeTakenMs(new AbstractFunction0<Object>() {
            @Override
            public Object apply() {
                try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {

                    stmt.executeQuery("SELECT count(*) from test group by `_2`");

                } catch (Exception e) {

                }

                return null;
            }
        });

        System.out.println(tuple2._2);

    }

    @Test
    public void queryNotUseIndex(){


        Tuple2<Object, Object> tuple2 = Utils.timeTakenMs(new AbstractFunction0<Object>() {
            @Override
            public Object apply() {
                try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {

                    stmt.executeQuery("SELECT count(*) from test group by `_3`");

                } catch (Exception e) {

                }

                return null;
            }
        });

        System.out.println(tuple2._2);

    }

    @Test
    public void queryAlow(){


        Tuple2<Object, Object> tuple2 = Utils.timeTakenMs(new AbstractFunction0<Object>() {
            @Override
            public Object apply() {
                try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {

                    stmt.executeQuery("SELECT * from test where  `_1` =154351212");

                } catch (Exception e) {

                }

                return null;
            }
        });

        System.out.println(tuple2._2);

    }
}

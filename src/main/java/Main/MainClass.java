package Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainClass {

    private static Logger logger = LoggerFactory.getLogger(MainClass.class);

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:springContext.xml");
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        //test3测试失败
        test3(jdbcTemplate);
        logger.info("Success!");
    }

    /**
     * 测试成功
     * @param jdbcTemplate
     */
    public static void test1(JdbcTemplate jdbcTemplate) {
        String sql = "insert into carPosition_1(carNo,gpsTime,speed,lon,lat,address) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, "鲁AXC949", new Date(), 68, 129.345678, 34.876543, "胶州湾高速");
        logger.info("####insert Success!");
    }

    /**
     * 测试成功
     * @param jdbcTemplate
     */
    public static void test2(JdbcTemplate jdbcTemplate) {
        String qsql = "select b.company as company1 from carPosition_1 a left outer join " +
                "carInfo b on a.carNo=b.carNo where a.carNo=?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(qsql, "鲁AXC949");
        for(Map<String, Object> innerMap : list) {
            String company = (String) innerMap.get("company1");
            logger.info(company);
        }
    }

    /**
     * 测试失败
     * @param jdbcTemplate
     */
    public static void test3(JdbcTemplate jdbcTemplate) {
        String qsql = "select b.company as company1 from carPosition_1 a left outer join " +
                "carInfo b on a.carNo=b.carNo where a.carNo=?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(qsql, "吉AXC949");
        for(Map<String, Object> innerMap : list) {
            String company = (String) innerMap.get("company1");
            logger.info(company);
        }
    }

}


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import output.mappers.*;
import output.models.*;

import java.util.List;
import java.util.Map;

public class MainTest {
    private SqlSessionFactory factory;

    @Before
    public void setup() {
        factory = new SqlSessionFactoryBuilder().build(
                getClass().getClassLoader().getResourceAsStream("mapperConfig.xml"));
    }

    @Test
    public void testSelectByExample() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);
            for (int i = 0; i < 10; i++) {
                Test1 model = new Test1(null, String.format("user%d", i), "password", null);
                mapper.insertSelective(model);
            }

            Test1Example example = new Test1Example();
            List<Test1> results;

            example.setLimit(2);
            results = mapper.selectByExample(example);
            assert results.size() == 2;

            example.setLimit(null);
            example.setOffset(5);
            results = mapper.selectByExample(example);
            assert results.size() == 10;

            example.setLimit(1);
            results = mapper.selectByExample(example);
            assert results.get(0).username.equals("user5");

            session.rollback();
        }
    }

    @Test
    public void testInsertOrUpdateManually() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);
            Test1 model = new Test1(1, "username", "password", "TEST");

            mapper.insertOrUpdateManually(model, "description = 'test'");
            Test1 result = mapper.selectByPrimaryKey(1);
            assert result.description.equals("TEST");

            mapper.insertOrUpdateManually(model, "description = 'test'");
            result = mapper.selectByPrimaryKey(1);
            assert result.description.equals("test");

            session.rollback();
        }
    }

    @Test
    public void testInsertSelectiveOrUpdateManually() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);
            Test1 model = new Test1(null, "username", "password", null);
            Test1Example example = new Test1Example();
            example.createCriteria().andUsernameEqualTo("username");

            mapper.insertSelectiveOrUpdateManually(model, "description = 'test'");
            List<Test1> results = mapper.selectByExample(example);
            assert results.get(0).description.equals("null");

            mapper.insertSelectiveOrUpdateManually(model, "description = 'test'");
            results = mapper.selectByExample(example);
            assert results.get(0).description.equals("test");

            session.rollback();
        }
    }

    @Test
    public void testSelectManuallyByExample() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);
            for (int i = 0; i < 10; i++) {
                Test1 model;
                if (i % 2 == 0) {
                    model = new Test1(null, String.format("user%d", i), "0", null);
                } else {
                    model = new Test1(null, String.format("user%d", i), "1", null);
                }
                mapper.insertSelective(model);
            }

            Test1Example example = new Test1Example();
            example.createCriteria().andPasswordEqualTo("0");
            example.setLimit(1);
            example.setOffset(1);

            List<Map<String, Object>> results = mapper.selectManuallyByExample("password", example);
            assert results.get(0).get("id") == null;
            assert results.get(0).get("username") == null;
            assert results.get(0).get("password").equals("0");
            assert results.get(0).get("description") == null;

            session.rollback();
        }
    }
}

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import output.mappers.Test1Mapper;
import output.mappers.Test2Mapper;
import output.models.Test1;
import output.models.Test1Example;
import output.models.Test2;
import output.models.Test2Key;

import java.util.List;

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
                Test1 model = new Test1();
                model.username = String.format("user%d", i);
                model.password = "password";
                mapper.insert(model);
            }

            Test1Example example = new Test1Example();
            List<Test1> results;

            // with only limit
            example.setLimit(2);
            results = mapper.selectByExample(example);
            assert results.size() == 2;

            // with only offset; should not work
            example.setLimit(null);
            example.setOffset(5);
            results = mapper.selectByExample(example);
            assert results.size() == 10;

            // with both limit and offset
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

            Test1 model = new Test1();
            model.username = "username";
            model.password = "password";
            model.description = "TEST";

            // insert for the first time
            mapper.insertOrUpdateManually(model, "description = 'test'");
            Integer id = model.id;

            Test1 result = mapper.selectByPrimaryKey(model.id);
            assert result.description.equals("TEST");

            // insert another model to change the value of last_insert_id()
            Test1 other = new Test1();
            other.username = "user";
            other.password = "password";
            mapper.insert(other);

            // the inserted model should be updated and the id should not be changed
            mapper.insertOrUpdateManually(model, "description = 'test'");
            assert model.id.equals(id);
            result = mapper.selectByPrimaryKey(model.id);
            assert result.description.equals("test");

            session.rollback();
        }
    }

    @Test
    public void testInsertSelectiveOrUpdateManually() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);

            Test1 model = new Test1();
            model.username = "username";
            model.password = "password";

            // insert for the first time
            mapper.insertSelectiveOrUpdateManually(model, "description = 'test'");
            Integer id = model.id;

            // description should be the default value "null"
            Test1 result = mapper.selectByPrimaryKey(model.id);
            assert result.description.equals("null");

            // insert another model to change the value of last_insert_id()
            Test1 other = new Test1();
            other.username = "user";
            other.password = "password";
            mapper.insert(other);

            // the inserted model should be updated and the id should not be changed
            mapper.insertSelectiveOrUpdateManually(model, "description = 'test'");
            assert model.id.equals(id);
            result = mapper.selectByPrimaryKey(model.id);
            assert result.description.equals("test");

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
                    model = new Test1();
                    model.username = String.format("user%d", i);
                    model.password = "0";
                } else {
                    model = new Test1();
                    model.username = String.format("user%d", i);
                    model.password = "1";
                }
                mapper.insertSelective(model);
            }

            Test1Example example = new Test1Example();
            example.setOrderByClause("id asc");
            example.createCriteria().andPasswordEqualTo("0");
            example.setLimit(2);
            example.setOffset(1);

            // only username and password should be selected
            List<Test1> results = mapper.selectManuallyByExample("username, password", example);
            assert results.size() == 2;
            assert results.get(0).id == null;
            assert results.get(0).username.equals("user2");
            assert results.get(0).password.equals("0");
            assert results.get(0).description == null;

            session.rollback();
        }
    }

    @Test
    public void testSelectManuallyByPrimaryKey() {
        try (SqlSession session = factory.openSession()) {
            Test2Mapper mapper = session.getMapper(Test2Mapper.class);

            Test2 model = new Test2();
            model.provinceId = 1;
            model.cityId = 1;
            mapper.insertSelective(model);

            Test2Key key = new Test2Key();
            key.provinceId = 1;
            key.cityId = 1;

            // only description should be selected
            Test2 result = mapper.selectManuallyByPrimaryKey("description", key);
            assert result.provinceId == null;
            assert result.cityId == null;
            assert result.description.equals("null");

            session.rollback();
        }
    }

    @Test
    public void testUpdateManuallyByExample() {
        try (SqlSession session = factory.openSession()) {
            Test1Mapper mapper = session.getMapper(Test1Mapper.class);

            Test1 test = new Test1();
            test.username = "username";
            test.password = "password";
            test.description = "test";

            mapper.insert(test);

            Test1Example example = new Test1Example();
            example.createCriteria().andDescriptionEqualTo("test");
            mapper.updateManuallyByExample("description = upper(description)", example);

            Test1 result = mapper.selectByPrimaryKey(test.id);
            assert result.description.equals("TEST");

            session.rollback();
        }
    }

    @Test
    public void testUpdateManuallyByPrimaryKey() {
        try (SqlSession session = factory.openSession()) {
            Test2Mapper mapper = session.getMapper(Test2Mapper.class);

            Test2 test = new Test2();
            test.provinceId = 1;
            test.cityId = 1;
            test.description = "test";

            mapper.insert(test);

            Test2Key key = new Test2Key();
            key.provinceId = 1;
            key.cityId = 1;
            mapper.updateManuallyByPrimaryKey("description = upper(description)", key);

            Test2 result = mapper.selectByPrimaryKey(key);
            assert result.description.equals("TEST");

            session.rollback();
        }
    }
}

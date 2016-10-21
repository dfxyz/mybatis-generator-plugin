import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
    private SqlSessionFactory factory;

    @Before
    public void setup() {
        factory = new SqlSessionFactoryBuilder().build(
                getClass().getClassLoader().getResourceAsStream("mapperConfig.xml"));
    }

}

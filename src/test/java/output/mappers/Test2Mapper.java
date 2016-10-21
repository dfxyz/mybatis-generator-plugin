package output.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import output.models.Test2;
import output.models.Test2Example;
import output.models.Test2Key;

public interface Test2Mapper {
    long countByExample(Test2Example example);

    int deleteByExample(Test2Example example);

    int deleteByPrimaryKey(Test2Key key);

    int insert(Test2 record);

    int insertSelective(Test2 record);

    List<Test2> selectByExample(Test2Example example);

    Test2 selectByPrimaryKey(Test2Key key);

    int updateByExampleSelective(@Param("record") Test2 record, @Param("example") Test2Example example);

    int updateByExample(@Param("record") Test2 record, @Param("example") Test2Example example);

    int updateByPrimaryKeySelective(Test2 record);

    int updateByPrimaryKey(Test2 record);

    int insertOrUpdateManually(@Param("record") Test2 record, @Param("updateClause") String updateClause);

    int insertSelectiveOrUpdateManually(@Param("record") Test2 record, @Param("updateClause") String updateClause);

    List<Test2> selectManuallyByExample(@Param("selectClause") String selectClause, @Param("example") Test2Example example);

    Test2 selectManuallyByPrimaryKey(@Param("selectClause") String selectClause, @Param("key") Test2Key key);

    int updateManuallyByExample(@Param("updateClause") String updateClause, @Param("example") Test2Example example);

    int updateManuallyByPrimaryKey(@Param("updateClause") String updateClause, @Param("key") Test2Key key);
}
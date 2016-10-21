package output.mappers;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import output.models.Test1;
import output.models.Test1Example;

public interface Test1Mapper {
    long countByExample(Test1Example example);

    int deleteByExample(Test1Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(Test1 record);

    int insertSelective(Test1 record);

    List<Test1> selectByExample(Test1Example example);

    Test1 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Test1 record, @Param("example") Test1Example example);

    int updateByExample(@Param("record") Test1 record, @Param("example") Test1Example example);

    int updateByPrimaryKeySelective(Test1 record);

    int updateByPrimaryKey(Test1 record);

    int insertOrUpdateManually(@Param("record") Test1 record, @Param("updateClause") String updateClause);

    int insertSelectiveOrUpdateManually(@Param("record") Test1 record, @Param("updateClause") String updateClause);

    List<Map<String, Object>> selectManuallyByExample(@Param("selectClause") String selectClause, @Param("example") Test1Example example);

    Map<String, Object> selectManuallyByPrimaryKey(@Param("selectClause") String selectClause, @Param("id") Integer id);

    int updateManuallyByExample(@Param("updateClause") String updateClause, @Param("example") Test1Example example);

    int updateManuallyByPrimaryKey(@Param("updateClause") String updateClause, @Param("id") Integer id);
}
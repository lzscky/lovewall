package com.cc.conststar.wall.dao;

import com.cc.conststar.wall.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import java.util.List;

/**
 * (Banner)表数据库访问层
 *
 * @author cky
 * @since 2022-12-05 14:31:39
 */
@Mapper
public interface BannerDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Banner queryById(Integer id);

    /**
     * 查询指定行数据
     *
     */
    List<Banner> queryAllByLimit();

    /**
     * 统计总行数
     *
     * @param banner 查询条件
     * @return 总行数
     */
    long count(Banner banner);

    /**
     * 新增数据
     *
     * @param banner 实例对象
     * @return 影响行数
     */
    int insert(Banner banner);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Banner> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Banner> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Banner> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Banner> entities);

    /**
     * 修改数据
     *
     * @param banner 实例对象
     * @return 影响行数
     */
    int update(Banner banner);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}


package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.OwnerScore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OwnerScoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OwnerScore row);

    OwnerScore selectByPrimaryKey(Long id);

    List<OwnerScore> selectAll();

    int updateByPrimaryKey(OwnerScore row);

    int addScoreByOwnerId(int score,Long ownerId);

    int selectScoreByOwnerId(Long ownerId);
}
package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Logg;

@Mapper
public interface LogMapper extends BaseMapper<Logg> {
}

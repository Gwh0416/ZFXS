package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Budget;

@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {
}

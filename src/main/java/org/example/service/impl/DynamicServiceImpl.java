package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Dynamic;
import org.example.mapper.DynamicMapper;
import org.example.service.DynamicService;
import org.springframework.stereotype.Service;

@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {
}

package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Dynamicpic;
import org.example.mapper.DynamicPicMapper;
import org.example.service.DynamicPicService;
import org.springframework.stereotype.Service;

@Service
public class DynamicPicServiceImpl extends ServiceImpl<DynamicPicMapper, Dynamicpic> implements DynamicPicService {
}

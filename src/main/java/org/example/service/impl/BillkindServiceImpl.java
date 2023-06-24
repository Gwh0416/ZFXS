package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Kind;
import org.example.mapper.BillkindMapper;
import org.example.service.BillkindService;
import org.springframework.stereotype.Service;

@Service
public class BillkindServiceImpl extends ServiceImpl<BillkindMapper, Kind> implements BillkindService {
}

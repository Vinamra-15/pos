package com.increff.pos.service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.DaySalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesReportService {
    @Autowired
    private DaySalesDao daySalesDao;
    public void add(DaySalesPojo daySalesPojo){
        daySalesDao.insert(daySalesPojo);
    }
}

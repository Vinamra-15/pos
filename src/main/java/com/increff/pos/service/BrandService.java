package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo p) throws ApiException {

		if(dao.checkBrandCatDuplicateExists(p.getName(),p.getCategory())){
			throw new ApiException("Brand: " + p.getName() + " in the category: " + p.getCategory() + " already exists.");
		}

		dao.insert(p);
	}



	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo get(Integer id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, BrandPojo p) throws ApiException {
		if(dao.checkBrandCatDuplicateExists(p.getName(),p.getCategory())){
			throw new ApiException("Brand: " + p.getName() + " in the category: " + p.getCategory() + " already exists.");
		}
		BrandPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setName(p.getName());
		dao.update(ex);
	}

	@Transactional
	public BrandPojo getCheck(Integer id) throws ApiException {
		BrandPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exist, id: " + id);
		}
		return p;
	}

	@Transactional
	public BrandPojo getCheckWithBrandCategory(String name, String category) throws ApiException{
		BrandPojo p = dao.select(name,category);
		if (p == null) {
			throw new ApiException("Brand with name: " + name + " and category: " + category+" does not exist!");
		}
		return p;
	}

}

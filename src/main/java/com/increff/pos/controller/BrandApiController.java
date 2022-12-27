package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {

	@Autowired
	private BrandService service;

	@Autowired
	private BrandDto dto;

	@ApiOperation(value = "Adds an brand")
	@RequestMapping(path = "/api/brands", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm brandForm) throws ApiException {
		dto.add(brandForm);
	}


	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable Integer id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates a brand")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException {
		dto.update(id, brandForm);
	}
	



}

package com.example.demo.service.impl;

import com.example.demo.domain.City;
import com.example.demo.mapper.CityESMapper;
import com.example.demo.service.CityService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CityServiceImpl implements CityService {

    @Resource
    private CityESMapper cityESMapper;

    @Override
    public void saveCity(City city) {
        cityESMapper.save(city);
    }

    @Override
    public List<City> searchCity(String name) {
        List<City> cityList = new ArrayList<City>();
        MatchQueryBuilder queryCondition = new MatchQueryBuilder("cityname", name);
        Iterable<City> citys = cityESMapper.search(queryCondition);
        citys.forEach(city -> cityList.add(city));
        return cityList;
    }

    @Override
    public void delete(City city) {
        cityESMapper.delete(city);
    }

    @Override
    public Long count() {
        return cityESMapper.count();
    }

    @Override
    public Iterable<City> getAll() {
        return cityESMapper.findAll();
    }
}

package com.example.demo.service;

import com.example.demo.domain.City;

import java.util.List;

public interface CityService {

    /**
     * 新增城市信息
     *
     * @param city
     * @return
     */
    void saveCity(City city);

    /**
     * 根据关键词，function score query 权重查询
     *
     * @param name
     * @return
     */
    List<City> searchCity(String name);

    /**
     *
     * @param city
     */
    void delete(City city);

    Long count();

    Iterable<City> getAll();
}

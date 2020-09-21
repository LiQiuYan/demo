package com.example.demo.mapper;

import com.example.demo.domain.City;
import org.apache.ibatis.annotations.Insert;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CityESMapper extends ElasticsearchRepository<City, Integer> {

/*    @Insert("insert into city(provinceid, cityname, description) values(#{provinceid}, #{cityname}, #{description})")
    void insert(City city);

    List<City> searchCityByName(String name);*/

}

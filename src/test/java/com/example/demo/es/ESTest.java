package com.example.demo.es;

import com.example.demo.domain.City;
import com.example.demo.service.CityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ESTest {

    @Resource
    private CityService cityService;

    @Test
    public void contextLoads() {
        Iterable<City> all = cityService.getAll();
        int count = 0;
        for(City city : all){
            System.err.println(city);
            count++;
        }
        System.err.println(count);
    }

    @Test
    public void saveCity() {
        City city = new City();
        city.setId((long)2);
        city.setProvinceid((long)1);
        city.setCityname("北京大兴");
        city.setDescription("");
        cityService.saveCity(city);
    }

    @Test
    public void getCityByname() {
        List<City> cityList1 =  cityService.searchCity("大");
        System.err.println(cityList1.size());
        List<City> cityList2 = cityService.searchCity("北京");
        System.err.println(cityList2.size());
    }

}

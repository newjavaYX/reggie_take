package com.example.reggie.dto;

import com.example.reggie.domain.Dish;
import com.example.reggie.domain.Dish_flavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<Dish_flavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}

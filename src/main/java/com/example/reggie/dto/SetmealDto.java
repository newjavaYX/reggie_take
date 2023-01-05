package com.example.reggie.dto;

import com.example.reggie.domain.Setmeal;
import com.example.reggie.domain.Setmeal_dish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<Setmeal_dish> setmealDishes;

    private String categoryName;
}

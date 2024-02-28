package com.sm.project.converter.food;

import com.sm.project.domain.food.Food;
import com.sm.project.domain.member.Member;
import com.sm.project.web.dto.food.FoodRequestDTO;
import com.sm.project.web.dto.food.FoodResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class FoodConverter {

    public static Food toFoodDTO(FoodRequestDTO.UploadFoodDTO request, Member member, Integer refrigeratorId) {

        return Food.builder()
                .name(request.getName())
                .refrigeratorId(refrigeratorId)
                .count(request.getCount())
                .expire(request.getExpire())
                .foodType(request.getFoodType())
                .member(member)
                .build();
    }

    public static FoodResponseDTO.UploadFoodResultDTO toUploadFoodResultDTO(Food food){

        return FoodResponseDTO.UploadFoodResultDTO.builder()
                .name(food.getName())
                .count(food.getCount()).build();
    }

    public static FoodResponseDTO.FoodListDTO toGetFoodListResultDTO(List<Food> foodList){
        List<FoodResponseDTO.FoodDTO> foodListDTO = foodList.stream().map(food -> FoodResponseDTO.FoodDTO.builder()
                                                                                                    .name(food.getName())
                                                                                                    .count(food.getCount())
                                                                                                    .expire(food.getExpire())
                                                                                                    .foodType(food.getFoodType())
                                                                                                    .build()).collect(Collectors.toList());
        return FoodResponseDTO.FoodListDTO.builder()
                .foodList(foodListDTO)
                .build();
    }
}

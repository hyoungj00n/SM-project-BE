package com.sm.project.service.food;

import com.sm.project.converter.food.FoodConverter;
import com.sm.project.domain.food.Food;
import com.sm.project.domain.member.Member;
import com.sm.project.repository.food.FoodRepository;
import com.sm.project.web.dto.food.FoodRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;

    public Food uploadFood(FoodRequestDTO.UploadFoodDTO request, Member member, Integer refrigeratorId){


        Food newFood = FoodConverter.toFoodDTO(request, member, refrigeratorId);
        foodRepository.save(newFood);
        return newFood;
    }

    public List<Food> getFoodList(Member member, Integer refigeratorId){

        List<Food> foodList = foodRepository.findAllByMemberAndRefrigeratorId(member,refigeratorId);

        return foodList;
    }

}

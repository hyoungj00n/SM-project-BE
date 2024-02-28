package com.sm.project.repository.food;

import com.sm.project.domain.food.Food;
import com.sm.project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findAllByMemberAndRefrigeratorId(Member member, Integer refrigeratorId);
}

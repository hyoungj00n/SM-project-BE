package com.sm.project.converter.member;

import com.sm.project.web.dto.member.MemberResponseDTO;

public class MemberConverter {

    public static MemberResponseDTO.SocialJoinResultDTO toSocialJoinResultDTO(String phone, String email) {

        return MemberResponseDTO.SocialJoinResultDTO.builder()
                .phone(phone)
                .email(email)
                .build();
    }



}

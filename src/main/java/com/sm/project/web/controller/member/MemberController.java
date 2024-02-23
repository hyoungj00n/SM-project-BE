package com.sm.project.web.controller.member;

import com.sm.project.apiPayload.ResponseDTO;
import com.sm.project.service.member.MemberService;
import com.sm.project.web.dto.member.MemberRequestDTO;
import com.sm.project.web.dto.member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "Member 관련 API")
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().body("성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "request 파라미터 : 이메일, 비밀번호(String)")
    public ResponseDTO<MemberResponseDTO.LoginDTO> login(@RequestBody MemberRequestDTO.LoginDTO request) {


        return ResponseDTO.onSuccess(memberService.login(request));
    }

}

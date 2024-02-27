package com.sm.project.web.controller.member;

import com.sm.project.apiPayload.ApiResponse;
import com.sm.project.apiPayload.code.ErrorReasonDTO;
import com.sm.project.apiPayload.code.status.SuccessStatus;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.domain.member.Member;
import com.sm.project.service.member.MemberCommandService;
import com.sm.project.web.dto.member.MemberRequestDTO;
import com.sm.project.web.dto.member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "Member 관련 API")
@RequestMapping("/api/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("성공");
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "이메일을 통해 회원가입하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4002", description = "이미 가입된 회원입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDTO request) {
        Member newMember = memberCommandService.joinMember(request);
        return ApiResponse.of(SuccessStatus._OK, MemberConverter.toJoinResultDTO(newMember));
    }
}

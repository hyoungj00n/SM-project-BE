package com.sm.project.web.controller.member;

import com.sm.project.apiPayload.ApiResponse;
import com.sm.project.apiPayload.code.ErrorReasonDTO;
import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.code.status.SuccessStatus;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.domain.member.Member;
import com.sm.project.service.member.MemberCommandService;
import com.sm.project.service.member.MemberQueryService;
import com.sm.project.web.dto.member.MemberRequestDTO;
import com.sm.project.web.dto.member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
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
    private final MemberQueryService memberQueryService;

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

    @GetMapping("/email")
    @Operation(summary = "이메일 찾기 API", description = "닉네임과 전화번호로 이메일을 찾는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @Parameters({
            @Parameter(name = "nickname", description = "회원의 닉네임을 입력하세요."),
            @Parameter(name = "phone", description = "회원의 전화번호를 입력하세요.")
    })
    public ApiResponse<MemberResponseDTO.EmailResultDTO> findEmail(@RequestParam String nickname, @RequestParam String phone) {
        Member member = memberQueryService.findEmail(nickname, phone);
        return ApiResponse.of(SuccessStatus._OK, MemberConverter.toEmailResultDTO(member));
    }

    @PostMapping("/send")
    @Operation(summary = "본인인증 문자 전송 API", description = "본인인증을 위한 인증번호 문자를 보내는 API입니다.")
    public ApiResponse sendSMS(@RequestBody MemberRequestDTO.SmsDTO smsDTO) {
        memberCommandService.sendSms(smsDTO);
        return ApiResponse.onSuccess("인증문자 전송 성공");
    }


}

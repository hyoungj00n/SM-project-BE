package com.sm.project.web.controller.member;

import com.sm.project.apiPayload.ResponseDTO;
import com.sm.project.service.mail.MailService;
import com.sm.project.service.member.MemberService;
import com.sm.project.web.dto.member.MemberRequestDTO;
import com.sm.project.web.dto.member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import com.sm.project.apiPayload.code.ErrorReasonDTO;
import com.sm.project.apiPayload.code.status.SuccessStatus;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.domain.member.Member;
import com.sm.project.service.member.MemberCommandService;
import com.sm.project.service.member.MemberQueryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "Member", description = "Member 관련 API")
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final MailService mailService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "request 파라미터 : 이메일, 비밀번호(String)")
    public ResponseDTO<MemberResponseDTO.LoginDTO> login(@RequestBody MemberRequestDTO.LoginDTO request) {


        return ResponseDTO.onSuccess(memberService.login(request));
    }


    @GetMapping("/callback/kakao")
    public ResponseDTO<?> getKakaoAccount(@RequestParam("code") String code) {

        return memberService.getKakaoInfo(code);

    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "이메일을 통해 회원가입하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4004", description = "이미 가입된 회원입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4006", description = "인증 번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    public ResponseDTO<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDTO request) {
        Member newMember = memberCommandService.joinMember(request);
        return ResponseDTO.of(SuccessStatus._OK, MemberConverter.toJoinResultDTO(newMember));
    }

    @PostMapping("/email")
    @Operation(summary = "이메일 찾기 API", description = "닉네임과 전화번호로 이메일을 찾는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    public ResponseDTO<MemberResponseDTO.EmailResultDTO> findEmail(@RequestBody @Valid MemberRequestDTO.FindEmailDTO request) {
        Member member = memberQueryService.findEmail(request.getNickname(), request.getPhone());
        return ResponseDTO.of(SuccessStatus._OK, MemberConverter.toEmailResultDTO(member));
    }

    @PostMapping("/send")
    @Operation(summary = "본인인증 문자 전송 API", description = "본인인증을 위한 인증번호 문자를 보내는 API입니다.")
    public ResponseDTO sendSMS(@RequestBody MemberRequestDTO.SmsDTO request) {
        memberCommandService.sendSms(request);
        return ResponseDTO.onSuccess("인증문자 전송 성공");
    }

    @PostMapping("/password/send")
    @Operation(summary = "비빌번호 찾기 이메일 전송 API", description = "비밀번호를 찾기 위한 인증링크 이메일을 전송하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "해당 회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    public ResponseDTO sendEmail(@RequestBody @Valid MemberRequestDTO.FindPasswordDTO request) throws MessagingException, UnsupportedEncodingException {
        memberCommandService.sendEmail(request);
        return ResponseDTO.onSuccess("메일 전송 성공");
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 재설정 API", description = "비밀번호 찾기 페이지에서 비밀번호를 재설정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4005", description = "재설정한 비밀번호가 서로 다릅니다.",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4007", description = "재설정 토큰이 올바르지 않습니다.(해당 회원을 찾을 수 없습니다.)",
                    content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    public ResponseDTO<?> resetPassword(@RequestParam String resetToken, @RequestBody @Valid MemberRequestDTO.PasswordDTO request) {
        memberCommandService.resetPassword(resetToken, request);
        return ResponseDTO.onSuccess("비밀번호 재설정 성공");
    }

}

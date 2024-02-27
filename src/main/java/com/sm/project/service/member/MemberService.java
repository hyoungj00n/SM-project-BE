package com.sm.project.service.member;

import com.sm.project.apiPayload.ResponseDTO;
import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.config.springSecurity.TokenProvider;
import com.sm.project.converter.member.MemberConverter;
import com.sm.project.domain.member.Member;
import com.sm.project.domain.member.MemberPassword;
import com.sm.project.feignClient.dto.KakaoProfile;
import com.sm.project.feignClient.dto.KakaoTokenResponse;
import com.sm.project.feignClient.kakao.KakaoTokenFeignClient;
import com.sm.project.feignClient.service.KakaoOauthService;
import com.sm.project.redis.service.RedisService;
import com.sm.project.repository.member.MemberPasswordRepository;
import com.sm.project.repository.member.MemberRepository;
import com.sm.project.web.dto.member.MemberRequestDTO;
import com.sm.project.web.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final MemberPasswordRepository memberPasswordRepository;
    private final KakaoOauthService kakaoOauthService;
    private final KakaoTokenFeignClient tokenClient;


    @Value("${oauth2.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;


    public MemberResponseDTO.LoginDTO login(MemberRequestDTO.LoginDTO request) {

        //email 없음
        Member selectedMember = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_EMAIL_NOT_FOUND));

        MemberPassword memberPassword = memberPasswordRepository.findByMember(selectedMember);
        System.out.println(memberPassword.getPassword());
        //password 틀림
        if (!encoder.matches(request.getPassword(), memberPassword.getPassword())) {
            throw new MemberHandler(ErrorStatus.MEMBER_PASSWORD_ERROR);
        }


        return MemberResponseDTO.LoginDTO.builder()
                .accessToken(redisService.saveLoginStatus(selectedMember.getId(), tokenProvider.createAccessToken(selectedMember.getId(), selectedMember.getJoinType(), request.getEmail(), Arrays.asList(new SimpleGrantedAuthority("USER")))))
                .refreshToken(redisService.generateRefreshToken(request.getEmail()).getToken())
                .build();

    }

    @Transactional
    public ResponseDTO<?> getKakaoInfo(String code) {
        KakaoTokenResponse token = tokenClient.generateToken("authorization_code",kakaoClientId, kakaoRedirectUri, code);


        KakaoProfile kakaoProfile = kakaoOauthService.getKakaoUserInfo("Bearer "+ token.getAccess_token());

        String email = kakaoProfile.getKakao_account().getEmail();

        Optional<Member> member = memberRepository.findByEmail(email);

        String phone = kakaoProfile.kakao_account.phone_number.replaceAll("[^0-9]", "");
        if (phone.startsWith("82")) {
            phone = "0" + phone.substring(2);
        }

        if(member.isEmpty()){
            return ResponseDTO.onFailure("로그인 실패", "회원가입 필요", MemberConverter.toSocialJoinResultDTO(phone, email));
        }
        else{
            return ResponseDTO.onSuccess(MemberResponseDTO.LoginDTO.builder()
                    .accessToken(redisService.saveLoginStatus(member.get().getId(), tokenProvider.createAccessToken(member.get().getId(), member.get().getJoinType() ,email, Arrays.asList(new SimpleGrantedAuthority("USER")))))
                    .refreshToken(redisService.generateRefreshToken(email).getToken())
                    .build());
        }
    }



}

package com.sm.project.redis.service;

import com.sm.project.apiPayload.code.status.ErrorStatus;
import com.sm.project.apiPayload.exception.handler.JwtHandler;
import com.sm.project.apiPayload.exception.handler.MemberHandler;
import com.sm.project.config.springSecurity.TokenProvider;
import com.sm.project.domain.member.Member;
import com.sm.project.redis.domain.LoginStatus;
import com.sm.project.redis.domain.RefreshToken;
import com.sm.project.redis.repository.LoginStatusRepository;
import com.sm.project.redis.repository.RefreshTokenRepository;
import com.sm.project.repository.member.MemberRepository;
import com.sm.project.web.dto.member.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisServiceImpl implements RedisService {

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginStatusRepository loginStatusRepository;

    private final TokenProvider tokenProvider;

    Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Override
    @Transactional
    public RefreshToken generateRefreshToken(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String token = tokenProvider.createRefreshToken(Arrays.asList(new SimpleGrantedAuthority("USER")));
        Long memberId = member.getId();

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime expireTime = currentTime.plus(1000, ChronoUnit.MINUTES);

        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .expireTime(expireTime).build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    @Transactional
    public RefreshToken reGenerateRefreshToken(MemberRequestDTO.IssueTokenDTO request) {
        if (request.getRefreshToken() == null)
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        RefreshToken findRefreshToken = refreshTokenRepository.findById(request.getRefreshToken()).orElseThrow(() -> new JwtHandler(ErrorStatus.JWT_REFRESH_TOKEN_EXPIRED));
        LocalDateTime expireTime = findRefreshToken.getExpireTime();
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime expireDeadLine = current.plusSeconds(20);

        Member member = memberRepository.findById(findRefreshToken.getMemberId()).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (current.isAfter(expireTime)) {
            logger.error("이미 만료된 리프레시 토큰 발견");
            throw new JwtHandler(ErrorStatus.JWT_REFRESH_TOKEN_EXPIRED);
        }

        // 새로 발급할 accessToken보다 refreshToken이 먼저 만료 될 경우인가?
        if (expireTime.isAfter(expireDeadLine)) {
            logger.info("기존 리프레시 토큰 발급");
            return findRefreshToken;
        } else {
            logger.info("accessToken보다 먼저 만료될 예정인 리프레시 토큰 발견");
            deleteRefreshToken(request.getRefreshToken());
            return generateRefreshToken(member.getEmail());
        }
    }

    @Override
    @Transactional
    public String saveLoginStatus(Long memberId, String accessToken) {
        loginStatusRepository.save(
                LoginStatus.builder()
                        .accessToken(accessToken)
                        .memberId(memberId)
                        .build()
        );

        return accessToken;
    }

    @Override
    @Transactional
    public void resolveLogout(String accessToken) {
        LoginStatus loginStatus = loginStatusRepository.findById(accessToken).get();
        loginStatusRepository.delete(loginStatus);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        Optional<RefreshToken> target = refreshTokenRepository.findById(refreshToken);
        if (target.isPresent())
            refreshTokenRepository.delete(target.get());
    }

    @Override
    public Boolean validateLoginToken(String accessToken) {
        Long aLong = tokenProvider.validateAndReturnSubject(accessToken);
        if (aLong == 0L)
            return true;
        return loginStatusRepository.findById(accessToken).isPresent();
    }
}

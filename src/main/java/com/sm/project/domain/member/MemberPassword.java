package com.sm.project.domain.member;

import com.sm.project.domain.Common.BaseDateTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberPassword extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_password_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setPassword(String password) {
        this.password = password;
    }
}

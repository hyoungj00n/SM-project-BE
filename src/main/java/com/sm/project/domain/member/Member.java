package com.sm.project.domain.member;


import com.sm.project.domain.Common.BaseDateTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Member extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "VARCHAR(13)")
    private String phone;

    private Boolean infoAgree;

    private Boolean messageAgree;

    @ColumnDefault("'ACTIVE'")
    private String status;

    @Enumerated(EnumType.STRING)
    private JoinType joinType;
}

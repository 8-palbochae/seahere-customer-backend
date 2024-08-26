package com.seahere.backend.alarm.entity;

import com.seahere.backend.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "AlarmToken")
public class AlarmTokenEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AlarmTokenId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    private String token;

    public void tokenUpdate(String token){
        this.token = token;
    }
}

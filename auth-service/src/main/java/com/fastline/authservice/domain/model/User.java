package com.fastline.authservice.domain.model;

import com.fastline.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name="p_users")
@NoArgsConstructor
public class User extends TimeBaseEntity {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(length = 50, nullable = false)
    private String slackId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private UUID hubId;

    public User(String email, String username, String password, UserRole role, UUID hubId, String slackId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.slackId = slackId;
        this.hubId = hubId;
        this.status = UserStatus.PENDING;
    }

    public void permitSignup() {
        this.status = UserStatus.APPROVE;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateSlackId(@NotBlank String slackId) {
        this.slackId = slackId;
    }

    public void updateReject() {this.status = UserStatus.REJECTED;}

    public void delete() {
        this.status = UserStatus.DELETED;
        markDeleted();
    }
}

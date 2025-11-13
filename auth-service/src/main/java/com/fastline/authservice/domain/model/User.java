package com.fastline.authservice.domain.model;

import com.fastline.authservice.domain.vo.DeliveryManagerType;
import com.fastline.authservice.domain.vo.UserStatus;
import com.fastline.common.auditing.TimeBaseEntity;
import com.fastline.common.security.model.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_users")
@NoArgsConstructor
public class User extends TimeBaseEntity<User> {
	@Id
	@Column(name = "user_id")
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

	@OneToOne(
			mappedBy = "user",
			optional = true,
			cascade = CascadeType.ALL)
	private DeliveryManager deliveryManager;

	public User(
			String email, String username, String password, UserRole role, UUID hubId, String slackId) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
		this.slackId = slackId;
		this.hubId = hubId;
		this.status = UserStatus.PENDING;
	}

	public User(String email, String username, String password, UserRole role, UUID hubId, String slackId, DeliveryManagerType deliveryType, Long deliveryNumber) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
		this.slackId = slackId;
		this.hubId = hubId;
		this.status = UserStatus.PENDING;
		this.deliveryManager = new DeliveryManager(this, deliveryType, deliveryNumber);
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

	public void updateReject() {
		this.status = UserStatus.REJECTED;
	}

	public void assign() {
		this.deliveryManager.assign();
	}

	public void complete() {
		this.deliveryManager.complete();
	}

	public void delete() {
		this.status = UserStatus.DELETED;
		markDeleted();
		if(this.role == UserRole.DELIVERY_MANAGER) this.getDeliveryManager().delete();
	}

	public void updateByManager(UUID hubId, UserStatus status, DeliveryManagerType deliveryType) {
		if(hubId!=null) this.hubId = hubId;
		if(status!=null) this.status = status;
		if(this.role == UserRole.DELIVERY_MANAGER && deliveryType!=null)
			this.getDeliveryManager().updateType(deliveryType);


	}
}

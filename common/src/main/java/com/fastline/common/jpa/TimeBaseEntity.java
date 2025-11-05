package com.fastline.common.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeBaseEntity {

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	protected Instant createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	protected Instant updatedAt;

	@Column(name = "deleted_at")
	protected Instant deletedAt;

	public void markDeleted() {
		this.deletedAt = Instant.now();
	}
}

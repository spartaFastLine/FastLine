package com.fastline.common.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ActorTimeBaseEntity<T extends ActorTimeBaseEntity<T>>
		extends TimeBaseEntity<T> {

	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	protected Long createdBy;

	@LastModifiedBy
	@Column(name = "updated_by")
	protected Long updatedBy;

	@Column(name = "deleted_by")
	protected Long deletedBy;

	// 작업자 정보와 함께 삭제
	public void markDeleted(Long deletedBy) {
		super.markDeleted();
		this.deletedBy = deletedBy;
	}

	// 작업자 정보 없이도 삭제 가능
	@Override
	public void markDeleted() {
		super.markDeleted();
	}
}

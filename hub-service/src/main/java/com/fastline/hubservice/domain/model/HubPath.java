package com.fastline.hubservice.domain.model;

import com.fastline.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "p_hub_paths")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HubPath extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "hub_path_id", nullable = false, updatable = false)
	private UUID hubPathId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "start_hub_id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_hub_path_start_hub"))
	private Hub startHub;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "end_hub_id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_hub_path_end_hub"))
	private Hub endHub;

	@Column(name = "duration")
	private Integer duration; // 소요 시간 (AI 계산 또는 수동 입력)

	@Column(name = "distance", precision = 8, scale = 2)
	private BigDecimal distance; // 거리 (KM 단위)

	@Column(name = "active", nullable = false)
	private Boolean active;
}

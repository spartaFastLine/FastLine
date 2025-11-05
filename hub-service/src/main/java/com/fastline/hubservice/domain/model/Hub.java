package com.fastline.hubservice.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "p_hubs")
public class Hub {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "hubs_id")
	private UUID hubsId;
}

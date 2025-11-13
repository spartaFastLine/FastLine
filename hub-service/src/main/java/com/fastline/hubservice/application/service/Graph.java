package com.fastline.hubservice.application.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.*;

public class Graph {

	// Jackson이 기본 생성자와 setter로 맵을 채울 수 있도록 초기화
	private Map<UUID, List<Edge>> adj = new HashMap<>();

	public Graph() {}

	public Map<UUID, List<Edge>> getAdj() {
		return adj;
	}

	public void setAdj(Map<UUID, List<Edge>> adj) {
		this.adj = adj;
	}

	public void addEdge(UUID from, Edge edge) {
		adj.computeIfAbsent(from, k -> new ArrayList<>()).add(edge);
	}

	public static class Edge implements Serializable {

		private final UUID to;
		private final Double distance;
		private final Integer duration;
		private final UUID hubPathId;

		@JsonCreator
		public Edge(
				@JsonProperty("to") UUID to,
				@JsonProperty("distance") Double distance,
				@JsonProperty("duration") Integer duration,
				@JsonProperty("hubPathId") UUID hubPathId) {
			this.to = to;
			this.distance = distance;
			this.duration = duration;
			this.hubPathId = hubPathId;
		}

		public UUID getTo() {
			return to;
		}

		public Double getDistance() {
			return distance;
		}

		public Integer getDuration() {
			return duration;
		}

		public UUID getHubPathId() {
			return hubPathId;
		}
	}
}

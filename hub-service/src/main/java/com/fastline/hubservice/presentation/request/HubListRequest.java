package com.fastline.hubservice.presentation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

/**
 * 허브 목록 조회 요청 DTO
 * - 필터(name, address, centralHubId, isCentral)
 * - 페이지네이션(page, size)
 * - 정렬(sort: "field,dir" 형식. 예: "createdAt,desc")
 *
 * 컨트롤러에서는 @ModelAttribute 또는 쿼리파라미터 바인딩으로 사용하세요.
 * 예) GET /api/hubs?name=서울&isCentral=true&page=0&size=20&sort=createdAt,desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubListRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String address;

    private UUID centralHubId;

    private Boolean isCentral;

    /** 0 기반 페이지 번호 */
    @Min(0)
    private Integer page = 0;

    /** 페이지 크기 (기본 20, 과도한 값은 toPageable()에서 제한) */
    @Min(1)
    private Integer size = 20;

    /** 정렬: "property,direction" (예: "createdAt,desc") */
    private String sort = "createdAt,desc";

    /**
     * 요청 기준의 Pageable 생성
     * - page < 0 인 경우 0으로 보정
     * - size <= 0 또는 너무 큰 경우(> 200) 20으로 보정
     * - sort 파싱 실패 시 기본 "createdAt,desc"
     */
    public Pageable toPageable() {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0 || size > 200) ? 20 : size;

        Sort sortObj = Sort.by("createdAt").descending();
        if (sort != null && !sort.isBlank()) {
            try {
                String[] parts = sort.split(",");
                String property = parts[0].trim();
                if (parts.length >= 2) {
                    String dir = parts[1].trim();
                    sortObj = "desc".equalsIgnoreCase(dir) ? Sort.by(property).descending()
                            : Sort.by(property).ascending();
                } else {
                    sortObj = Sort.by(property).ascending();
                }
            } catch (Exception ignored) {
                // fallback to default
            }
        }
        return PageRequest.of(p, s, sortObj);
    }
}

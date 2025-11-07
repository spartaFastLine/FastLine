package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

	private Double value;

	public Money plus(Double newValue) {
		return Money.of(this.value + newValue);
	}

	public Money minus(Double newValue) {
		return Money.of(this.value - newValue);
	}

	public Money multiplication(Double newValue) {
		return Money.of(this.value * newValue);
	}

	public Money division(Double newValue) {
		return Money.of(this.value / newValue);
	}

	public static Money of(Double value) {
		if (value < 0) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Money money = new Money();
		money.value = value;
		return money;
	}
}

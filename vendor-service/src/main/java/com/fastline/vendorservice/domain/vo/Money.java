package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

	private Double price;

	public Money plus(Double newValue) {
		return Money.of(this.price + newValue);
	}

	public Money minus(Double newValue) {
		return Money.of(this.price - newValue);
	}

	public Money multiplication(Double newValue) {
		return Money.of(this.price * newValue);
	}

	public Money division(Double newValue) {
		return Money.of(this.price / newValue);
	}

	public static Money of(Double value) {
		if (Objects.isNull(value) || value < 0) throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Money money = new Money();
		money.price = value;
		return money;
	}
}

package com.aim.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @AUTO
 * @Author AIM
 * @DATE 2021/10/21
 */
public class AimConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "aim", name = "enable", matchIfMissing = true)
	public AimController aimController() {
		return new AimController();
	}
}

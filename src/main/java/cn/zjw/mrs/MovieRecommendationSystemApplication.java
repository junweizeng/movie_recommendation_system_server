package cn.zjw.mrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zjw
 */
@SpringBootApplication
@EnableScheduling
public class MovieRecommendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieRecommendationSystemApplication.class, args);
	}

}

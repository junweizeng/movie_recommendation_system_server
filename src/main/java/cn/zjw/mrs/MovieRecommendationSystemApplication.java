package cn.zjw.mrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieRecommendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieRecommendationSystemApplication.class, args);
	}

}

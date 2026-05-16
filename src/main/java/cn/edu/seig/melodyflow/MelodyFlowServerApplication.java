package cn.edu.seig.melodyflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MelodyFlowServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MelodyFlowServerApplication.class, args);
    }

}

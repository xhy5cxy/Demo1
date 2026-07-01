package com.etour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * E旅游攻略系统主启动类
 * 
 * @author ETour团队
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.etour.mapper")
@ComponentScan(basePackages = "com.etour")
@EnableAsync
@EnableScheduling
public class ETourApplication {

    public static void main(String[] args) {
        SpringApplication.run(ETourApplication.class, args);
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    E旅游攻略系统启动成功！                      ║");
        System.out.println("║                  访问地址: http://localhost:8888/api              ║");
        System.out.println("║                  API文档: http://localhost:8888/api/doc.html      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
}
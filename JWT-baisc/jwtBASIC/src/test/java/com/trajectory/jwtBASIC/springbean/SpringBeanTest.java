package com.trajectory.jwtBASIC.springbean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class SpringBeanTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    @DisplayName("빈 이름 조회하기")
    void printAllBeans_withRole(ApplicationContext ctx) {
        // ApplicationContext → BeanFactory 꺼내기
        ConfigurableApplicationContext cac = (ConfigurableApplicationContext) ctx;
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) cac.getBeanFactory();

        String[] names = bf.getBeanDefinitionNames();
        System.out.println("=== ALL BEANS (" + names.length + ") ===");
        for (String name : names) {
            BeanDefinition def = bf.getBeanDefinition(name);
            int role = def.getRole(); // ROLE_APPLICATION / ROLE_INFRASTRUCTURE / ROLE_SUPPORT
            String roleStr = switch (role) {
                case BeanDefinition.ROLE_APPLICATION -> "APPLICATION";
                case BeanDefinition.ROLE_INFRASTRUCTURE -> "INFRA";
                case BeanDefinition.ROLE_SUPPORT -> "SUPPORT";
                default -> "UNKNOWN";
            };

            // BeanClassName이 null일 수 있어 보조적으로 타입 조회
            String className = def.getBeanClassName();
            if (className == null) {
                Class<?> type = ctx.getType(name);
                className = (type != null ? type.getName() : "(factory/alias)");
            }

            System.out.printf("[%s] %s -> %s%n", roleStr, name, className);
        }
    }
}
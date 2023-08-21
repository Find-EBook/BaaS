package com.sesac.baas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// Spring의 @Configuration 어노테이션은 해당 클래스가 Spring의 Java 기반 설정 파일임을 나타냅니다.
// 이 클래스는 XML 기반의 설정 대신 Java 코드로 Bean과 다른 설정을 정의하는 데 사용됩니다.
@Configuration
public class PasswordConfig {

    // @Bean 어노테이션은 Spring IoC 컨테이너에게 해당 메소드가 Bean 객체를 생성하는 팩토리 메소드임을 알려줍니다.
    // 이 Bean 객체는 Spring의 ApplicationContext에 의해 관리되며, 필요할 때 Spring에 의해 다른 Bean에 주입될 수 있습니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder는 비밀번호를 해싱하기 위한 구현입니다.
        // BCrypt 해싱 알고리즘을 사용하여 비밀번호를 안전하게 저장할 수 있게 해줍니다.
        // 이렇게 해싱된 비밀번호는 원래 비밀번호로 되돌릴 수 없으므로,
        // 데이터베이스가 노출되더라도 원본 비밀번호는 보호됩니다.
        return new BCryptPasswordEncoder();
    }
}



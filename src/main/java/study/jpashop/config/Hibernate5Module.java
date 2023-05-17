package study.jpashop.config;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Hibernate5Module {

    /**
     * Hibernate5JakartaModule은
     * LAZY 로딩으로 인해 proxy 객체가 담겨있는 객체에 대해서
     * 실행 시점에 조회하여 JSON 변환에 무리 없도록 해주는 기능이 있습니다.
     * 엔티티를 노출하는 경우 사용하지만 엔티티를 외부에 노출하지 않는게
     * 안전한 설계이므로 사용할 필요는 없을듯 합니다.
     */
    @Bean
    Hibernate5JakartaModule hibernate5JakartaModule() {
        Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
        //hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5JakartaModule;
    }
}

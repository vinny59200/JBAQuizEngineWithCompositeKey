package engine.vv;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;


@ComponentScan({"engine.vv"})
@EntityScan("engine.vv.model")
@EnableJpaRepositories("engine.vv.repository")
@Configuration
public class DataConfig {
}
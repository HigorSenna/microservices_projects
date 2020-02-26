import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication


@EnableZuulProxy// cria uma rota por 'baixo dos panos' que permite que eu acesse o course, através do gateway sem precisar de implementação,
// ex: http://localhost:8080/gateway/course/v1/admin/courses
@EnableEurekaClient
@ComponentScan("br.com.microservices")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
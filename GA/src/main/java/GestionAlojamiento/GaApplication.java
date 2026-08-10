package GestionAlojamiento;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GaApplication {
    public static void main(String[] args) {
        SpringApplication.run(GaApplication.class, args);
    }
}

package com.eloyroldan.tp_final_alojamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/// @EnableScheduling habilita el @Scheduled de PagoService.cancelarPagosVencidos
/// (job que corre cada 10 min y cancela reservas CONFIRMADA sin pagar tras 48hs).
@SpringBootApplication
@EnableScheduling
public class TpFinalAlojamientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpFinalAlojamientoApplication.class, args);
	}

}

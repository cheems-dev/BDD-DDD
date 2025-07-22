package com.example.bdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * COFOPRI - Sistema de Titulación BDD/DDD
 * 
 * Aplicación principal del sistema de titulación predial urbana
 * para el Organismo de Formalización de la Propiedad Informal (COFOPRI).
 * 
 * Implementa metodología BDD (Behavior-Driven Development) con
 * arquitectura DDD (Domain-Driven Design).
 * 
 * @author COFOPRI - Equipo de Desarrollo
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "pe.gob.cofopri")
@EntityScan("pe.gob.cofopri.infrastructure.entity")
@EnableJpaRepositories(basePackages = "pe.gob.cofopri.infrastructure.repository.jpa")
public class BddApplication {

	public static void main(String[] args) {
		SpringApplication.run(BddApplication.class, args);
		
		System.out.println("===========================================");
		System.out.println("🏛️  COFOPRI - Sistema BDD/DDD Iniciado");
		System.out.println("🌐  API: http://localhost:8080/api");
		System.out.println("💾  H2 Console: http://localhost:8080/h2-console");
		System.out.println("📋  Endpoints disponibles:");
		System.out.println("   POST   /api/solicitudes");
		System.out.println("   GET    /api/solicitudes/{id}");
		System.out.println("   POST   /api/ciudadanos");
		System.out.println("   GET    /api/ciudadanos/dni/{dni}");
		System.out.println("===========================================");
	}

}

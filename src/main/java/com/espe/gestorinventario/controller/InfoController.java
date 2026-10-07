package com.espe.gestorinventario.controller;

import com.espe.gestorinventario.config.AppInfoProperties;
import com.espe.gestorinventario.dto.AppInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta clase responderá peticiones web (REST) devolviendo JSON
@RequestMapping("/api") // Todas las rutas de esta clase empezarán con /api
public class InfoController {

    // Variable para guardar nuestras propiedades
    private final AppInfoProperties appInfoProperties;

    // Inyectamos las propiedades por el constructor (Buena práctica en Spring)
    public InfoController(AppInfoProperties appInfoProperties) {
        this.appInfoProperties = appInfoProperties;
    }

    @GetMapping("/info") // Esta ruta será GET /api/info
    public AppInfoDto getInfo() {
        // Tomamos los datos leídos por AppInfoProperties y los metemos en nuestro DTO de respuesta
        return new AppInfoDto(
                appInfoProperties.getName(),
                appInfoProperties.getVersion(),
                appInfoProperties.getDeveloperEmail(),
                appInfoProperties.getEnvironment()
        );
    }
}
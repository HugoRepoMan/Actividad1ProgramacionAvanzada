package com.espe.gestorinventario.controller;

import com.espe.gestorinventario.config.AppInfoProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfoController.class) // Levanta solo la capa web (Controladores) para que el test sea rapidísimo
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Nos permite simular peticiones HTTP (GET, POST, etc)

    @MockBean
    private AppInfoProperties appInfoProperties; // Simulamos el componente de propiedades

    @Test
    public void getInfo_DebeRetornarHttp200_Y_EstructuraJsonCorrecta() throws Exception {
        // 1. Arrange (Preparar): Le decimos a nuestro mock qué responder cuando el controlador le pida datos
        Mockito.when(appInfoProperties.getName()).thenReturn("Gestor Test");
        Mockito.when(appInfoProperties.getVersion()).thenReturn("1.0.0");
        Mockito.when(appInfoProperties.getDeveloperEmail()).thenReturn("hdarmijos002@gmail.com");
        Mockito.when(appInfoProperties.getEnvironment()).thenReturn("Entorno de Pruebas");

        // 2 & 3. Act & Assert (Actuar y Comprobar): Simulamos la petición GET y verificamos la respuesta
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk()) // Verifica que responda HTTP 200 (OK)
                .andExpect(jsonPath("$.name").value("Gestor Test")) // Verifica los campos del JSON
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.developerEmail").value("hdarmijos002@gmail.com"))
                .andExpect(jsonPath("$.environment").value("Entorno de Pruebas"));
    }
}

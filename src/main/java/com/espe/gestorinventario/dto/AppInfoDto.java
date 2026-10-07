package com.espe.gestorinventario.dto;

// Un record crea automáticamente los constructores y getters. Ideal para transferir datos inmutables.
public record AppInfoDto(String name, String version, String developerEmail, String environment) {
}
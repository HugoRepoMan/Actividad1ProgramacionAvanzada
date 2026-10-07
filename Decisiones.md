# Documento de Decisiones Arquitectónicas (Gestor de Inventario)

## 1. ¿Por qué usaste `@ConfigurationProperties` en lugar de `@Value`?
Se optó por `@ConfigurationProperties` porque permite agrupar las configuraciones de manera jerárquica y fuertemente tipada en un POJO (Plain Old Java Object) centralizado. A diferencia de `@Value`, que inyecta propiedades una por una y ensucia el código de los controladores o servicios, `@ConfigurationProperties` nos permite:
*   **Validación de arranque (Fail-Fast):** Al combinarlo con `@Validated` y anotaciones de Jakarta (como `@NotBlank` o `@Email`), la aplicación aborta su inicio inmediatamente si el correo tiene un formato inválido o si falta el nombre de la app, evitando errores silenciosos en producción.
*   **Autocompletado:** Gracias a la dependencia `spring-boot-configuration-processor`, el IDE reconoce las propiedades y nos sugiere los nombres al escribir en el archivo `application.properties`.

## 2. ¿Cómo garantizas que la versión no se desincronice del `pom.xml`?
Se garantizó mediante la técnica de **Filtrado de Recursos de Maven (Resource Filtering)**.
En el archivo `pom.xml`, habilitamos el filtrado para la carpeta de recursos. Luego, en el archivo `application.properties`, definimos la propiedad como `app.info.version=@project.version@`.
Al momento de compilar el proyecto, Maven intercepta este token (`@project.version@`) y lo reemplaza dinámicamente con la versión real escrita en el `<version>` del `pom.xml`. Esto establece a Maven como la única fuente de la verdad, eliminando el riesgo de actualizar la versión en el código y olvidar actualizarla en las propiedades.

## 3. ¿Qué pasaría si quitas el `@Component` de `AppInfoProperties`?
**Prueba realizada:** Se comentó la anotación `@Component` en la clase `AppInfoProperties` y se intentó levantar la aplicación.
**Resultado:** La aplicación falló de inmediato y no arrancó.
**Error documentado en consola:**
`Parameter 0 of constructor in com.espe.gestorinventario.controller.InfoController required a bean of type 'com.espe.gestorinventario.config.AppInfoProperties' that could not be found.`
**Explicación técnica:** Al quitar `@Component` (o `@Configuration`), la clase deja de ser administrada por el contenedor de Inversión de Control (IoC) de Spring. Por lo tanto, cuando Spring intenta crear el `InfoController`, se da cuenta de que el constructor exige un objeto `AppInfoProperties` que no existe en su memoria (no hay ningún Bean registrado con ese tipo), lanzando una excepción `NoSuchBeanDefinitionException` y abortando el despliegue.

## 4. ¿Qué diferencia hay entre tu `/info` y el `/actuator/info` de Spring?
*   **Nuestro endpoint `/api/info`:** Es un endpoint **de negocio y personalizado**. Nosotros creamos el controlador, definimos explícitamente el DTO (`AppInfoDto`) de respuesta y mapeamos exactamente los datos que queremos que los clientes de nuestra API consuman.
*   **El endpoint `/actuator/info`:** Es un endpoint **de infraestructura y estandarizado**. Pertenece a la librería Spring Boot Actuator, la cual está diseñada para la observabilidad y monitoreo del sistema. Este endpoint expone información interna generada automáticamente por componentes llamados `InfoContributors` (como variables del sistema operativo, versión de Java o detalles de los commits de Git), sin necesidad de que nosotros programemos un Controlador manualmente.
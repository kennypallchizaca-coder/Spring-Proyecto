# Informe Técnico: Sistema Backend de Gestión de Portafolios y Asesorías

## 1. Resumen Ejecutivo
Este documento describe la arquitectura, componentes y funcionalidades del sistema backend desarrollado para la plataforma de gestión de portafolios y asesorías técnicas. El sistema ha sido diseñado bajo estándares profesionales de ingeniería de software, priorizando la seguridad, la escalabilidad y la mantenibilidad del código fuente.

## 2. Arquitectura del Sistema
El backend está construido sobre el ecosistema de Spring Boot y sigue un patrón de diseño de arquitectura en capas, lo que permite una separación clara de responsabilidades:

- **Capa de Controladores (Controller Layer):** Gestiona las peticiones HTTP, valida la entrada de datos (DTOs) y maneja las respuestas de la API.
- **Capa de Servicios (Service Layer):** Contiene la lógica de negocio central del sistema, coordinando las interacciones entre repositorios, mappers y servicios externos.
- **Capa de Acceso a Datos (Repository Layer):** Utiliza Spring Data JPA para la interacción con la base de datos PostgreSQL mediante el mapeo objeto-relacional (ORM).
- **Capa de Mapeo (Mapper Layer):** Transforma los datos entre Entidades JPA, Modelos de Negocio y Objetos de Transferencia de Datos (DTOs).
- **Capa de Entidades y Modelos (Entity/Model Layer):** Define la estructura de datos y las reglas del dominio del sistema.

## 3. Módulos Principales

### 3.1 Seguridad y Autenticación
El sistema implementa una arquitectura de seguridad robusta mediante Spring Security y JSON Web Tokens (JWT) para la autenticación sin estado:
- **JWT:** Generación y validación de tokens seguros para el control de acceso.
- **BCrypt:** Encriptación de contraseñas de usuario antes de su persistencia.
- **Control de Acceso:** Restricciones basadas en roles (ADMIN, PROGRAMMER, USER) para proteger endpoints sensibles.
- **Filtros de Seguridad:** Intercepción de peticiones para validación de identidad en cada solicitud.

### 3.2 Gestión de Usuarios
Centraliza la administración de perfiles de programadores y usuarios generales:
- Registro y autenticación de usuarios.
- Gestión de disponibilidad para programadores.
- Actualización parcial de perfiles técnicos y redes sociales.

### 3.3 Portafolios y Proyectos
Módulo encargado de la exposición del trabajo técnico de los usuarios:
- **Portafolios:** Agrupaciones lógicas de proyectos con temas visuales personalizables.
- **Proyectos:** Registro detallado de aplicaciones, incluyendo stack tecnológico, repositorios y demostraciones en vivo.
- **Privacidad:** Control de visibilidad para portafolios públicos o privados.

### 3.4 Sistema de Asesorías
Facilita la conexión entre solicitantes y mentores técnicos:
- Creación de solicitudes de asesoría con detalles de agenda.
- Flujo de estados: Pendiente, Aprobado, Rechazado.
- Sistema de notificaciones automáticas por correo electrónico integrados mediante Spring Mail.

## 4. Tecnologías y Herramientas
- **Lenguaje:** Java 17 o superior.
- **Framework Principal:** Spring Boot 3.x.
- **Seguridad:** Spring Security y JJWT 0.12.3.
- **Base de Datos:** PostgreSQL.
- **Persistencia:** Spring Data JPA / Hibernate.
- **Utilidades:** Lombok (reducción de código repetitivo) y validaciones Jakarta.
- **Servicios Externos:** Cloudinary (gestión de imágenes) y servidores SMTP para correo electrónico.

## 5. Configuración y Despliegue
El sistema está diseñado para ser configurado mediante variables de entorno, facilitando su despliegue en contenedores o servicios en la nube. Los parámetros clave incluyen:
- **DB_URL, DB_USERNAME, DB_PASSWORD:** Credenciales de la base de datos.
- **JWT_SECRET:** Clave privada para la firma de tokens.
- **MAIL_USERNAME, MAIL_PASSWORD:** Configuración del servidor de correo.
- **CORS_ALLOWED_ORIGINS:** Control de dominios permitidos para el frontend.

## 7. Estructura Organizacional del Proyecto
El código fuente se organiza siguiendo una estructura modular por dominio, facilitando la escalabilidad y el aislamiento de responsabilidades. A continuación se detalla la función de cada componente:

### 7.1 Estructura de Directorios (Source)
La ruta raíz del código es `src/main/java/com/lexisware/portafolio/`, la cual se subdivide en los siguientes paquetes:

- **`advisory/`**: Implementa la lógica relacionada con las solicitudes de asesoría técnica entre usuarios y programadores.
- **`auth/`**: Centraliza los procesos de autenticación, generación de sesiones y registro de nuevos usuarios.
- **`config/`**: Contiene las clases de configuración global, incluyendo seguridad (JWT), políticas de CORS, configuración de Swagger/OpenAPI y bean dinámicos.
- **`files/`**: Gestiona la carga y administración de recursos multimedia, con integración directa a servicios de almacenamiento en la nube (Cloudinary).
- **`portfolio/`**: Maneja la entidad de portafolio, permitiendo agrupar proyectos y definir la presentación pública de los programadores.
- **`project/`**: Administra la información técnica de los proyectos individuales cargados por los usuarios.
- **`users/`**: Gestiona los datos maestros de los usuarios, sus roles, habilidades y perfiles profesionales.
- **`utils/`**: Provee clases de apoyo transversales, como manejadores de excepciones globales y servicios de mensajería (Email).

### 7.2 Componentes Internos de Módulo
Cada módulo principal sigue una arquitectura interna estandarizada para mantener la consistencia:

- **`controllers/`**: Define los puntos de entrada (endpoints) de la API REST. Valida los datos recibidos mediante anotaciones de Jakarta Validation.
- **`services/`**: Implementa el núcleo de la lógica de negocio. Estas clases son responsables de coordinar las acciones, aplicar reglas de validación complejas y gestionar transacciones de base de datos.
- **`entities/`**: Modelos de datos anotados con JPA para su persistencia en PostgreSQL. Definen la estructura de las tablas y sus relaciones (OneToMany, ManyToOne, etc.).
- **`models/`**: Clases POJO que representan el modelo de dominio puro, utilizado para desacoplar la lógica de negocio de la estructura de la base de datos.
- **`repositories/`**: Interfaces que extienden de `JpaRepository`, permitiendo operaciones CRUD y consultas personalizadas sobre las entidades.
- **`dtos/`**: Objetos de Transferencia de Datos que definen el contrato de entrada y salida de la API, protegiendo las entidades internas de la exposición directa.
- **`mappers/`**: Clases encargadas de la conversión eficiente de datos entre Entidades, Modelos y DTOs.

## 8. Pruebas y Documentación Interactiva
Para facilitar la verificación y el consumo de los servicios, el sistema expone una interfaz interactiva de Swagger UI donde se pueden probar todos los endpoints disponibles, consultar los esquemas de datos y validar los requisitos de seguridad.

- **Acceso a Swagger UI (Entorno Local):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Definición OpenAPI (JSON):** `/api-docs`

> [!NOTE]
> Para probar los endpoints protegidos, es necesario generar un token mediante el endpoint de `/api/auth/login` e ingresarlo en el botón "Authorize" de la interfaz de Swagger.

## 9. Conclusión
Este backend representa una solución integral y profesional para la gestión de talento técnico. La estructura modular y la documentación exhaustiva garantizan que el sistema sea escalable y fácil de auditar, cumpliendo con los requisitos técnicos de una aplicación lista para producción.

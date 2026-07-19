# Digital Soft - Backend API ⚙️

API RESTful y motor lógico para la plataforma de **Digital Soft**. Encargada de procesar las solicitudes de contacto, interactuar con la base de datos y gestionar el envío automático de notificaciones mediante un servicio SMTP.

## 🚀 Stack Tecnológico Principal
* **Lenguaje:** Java
* **Framework:** Spring Boot
* **Base de Datos:** MySQL
* **ORM:** Spring Data JPA / Hibernate
* **Servicios de Red:** JavaMailSender (Integración SMTP Gmail)

## 🏗️ Arquitectura
El proyecto sigue el patrón de diseño **MVC (Modelo-Vista-Controlador)** adaptado para APIs REST, separando las responsabilidades en capas claras:
* `Controllers`: Manejo de endpoints y peticiones HTTP.
* `Services`: Lógica de negocio y reglas de la aplicación.
* `Repositories`: Interfaces de acceso a datos.
* `Models/Entities`: Representación de las tablas de la base de datos.

## 🔐 Seguridad y Variables de Entorno
El sistema está diseñado para inyectar credenciales mediante variables de entorno, evitando exponer información sensible en el código fuente. Para ejecutarlo localmente, configura las siguientes variables en tu IDE:
* `DB_USER` (Usuario de MySQL)
* `DB_PASSWORD` (Contraseña de MySQL)
* `MAIL_USERNAME` (Correo emisor)
* `MAIL_PASSWORD` (Contraseña de aplicación SMTP)

## 🛠️ Ejecución Local
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/mariodufour/digitalsoft-backend.git](https://github.com/mariodufour/digitalsoft-backend.git)

2. Importar el proyecto como un proyecto Maven en tu IDE (IntelliJ, Eclipse, etc.).
3. Configurar las variables de entorno mencionadas y ejecutar la clase principal Application.java.

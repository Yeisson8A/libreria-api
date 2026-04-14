# **Librería API**

API REST desarrollada con Spring Boot para la gestión de una librería. Permite administrar libros, usuarios y préstamos con una arquitectura limpia y buenas prácticas de backend.

## **Tecnologías**

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- MapStruct
- Lombok
- Jakarta Validation
- OpenAPI / Swagger
- Docker

## **Arquitectura**

El proyecto sigue una arquitectura en capas:

```
controller → service → repository → database
                ↓
              mapper
                ↓
               dto
```

## **Crear la red Docker (una sola vez)**
`docker network create libreria-network`

## **Conectar el contenedor PostgreSQL a la red**
`docker network connect libreria-network postgresql`

## **Construir la imagen**
`docker-compose build`

## **Levantar todo**
`docker-compose up -d`

### **Estructura de paquetes**

```
com.ochoa.yeisson.libreria
│
├── controller/     # Endpoints REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos (JPA)
├── model/          # Entidades
├── dto/            # Objetos de transferencia
├── mapper/         # MapStruct
├── exception/      # Manejo global de errores
├── response/       # Estructura de respuesta de la API
├── util/           # Funciones utilitarias
├── config/         # Configuración (CORS, Swagger, etc.)
└── enums/          # Enumeraciones
```

## **Funcionalidades**

### **Libros**

- Crear libro
- Listar libros
- Obtener libro por ID
- Actualizar libro
- Eliminar libro
- Buscar

### **Usuarios**

- Crear usuario
- Listar usuarios
- Obtener usuario por ID
- Buscar

### **Préstamos**

- Prestar libro
- Devolver libro
- Listar préstamos

## **Configuración**

### **Base de datos (PostgreSQL)**

Configurada en `application.properties`:

- **spring.datasource.url**: Correspondiente a la cadena de conexión a la base de datos
- **spring.datasource.username**: Correspondiente al usuario para conectarse al servidor de base de datos
- **spring.datasource.password**: Correspondiente a la contraseña asociada al usuario para la conexión al servidor de base de datos
- **spring.jpa.hibernate.ddl-auto**: Utilizado para aplicar los respectivos cambios en las tablas de base de datos, tras modificación de las entidades
- **spring.jpa.show-sql**: Utilizado para mostrar las sentencias SQL correspondiente a cada operación que realiza la API

**Nota**: Tener en cuenta que el host del servidor corresponde al nombre del contenedor donde esta la base de datos, por ejemplo: **postgresql**

## **Ejecución**

````
mvn clean install
mvn spring-boot:run
````

## **Documentación API**

Swagger disponible en: `http://localhost:8080/swagger-ui/index.html`

<img width="1351" height="219" alt="image" src="https://github.com/user-attachments/assets/76c2633a-1d31-46ac-8f7e-a14edb02178a" />
<img width="1345" height="390" alt="image" src="https://github.com/user-attachments/assets/6ffb72e8-b6ac-4071-9ee0-b017962df1e4" />
<img width="1347" height="289" alt="image" src="https://github.com/user-attachments/assets/5d402d3b-6833-4f38-8fd4-9f1cc9017956" />

## **Ejemplos de Requests**

### **Crear libro**

```json
POST /api/libros

{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "1234567890",
  "fechaPublicacion": "2008-08-01"
}
```

### **Crear usuario**

```json
POST /api/usuarios

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com"
}
```

### **Crear préstamo**

```json
POST /api/prestamos

{
  "libroId": 1,
  "usuarioId": 1
}
```

---

## **Formato de Respuesta**

Todas las respuestas siguen un formato estándar:

```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": {},
  "errors": null,
  "timestamp": "2026-04-01T10:30:00"
}
```

## **Manejo de Errores**

```json
{
  "success": false,
  "message": "Libro no encontrado",
  "errors": null,
  "timestamp": "2026-04-01T10:30:00"
}
```

## **Validaciones**

Se implementan validaciones usando Jakarta Validation:

- Campos obligatorios
- Formato de email
- Tamaños mínimos/máximos
- Reglas de negocio

## **Testing**

- Para ejecutar las pruebas se deberá usar el comando: `mvn test`
- Para ejecutar las pruebas con cobertura se deberá usar el comando: `mvn clean test`

<img width="559" height="647" alt="image" src="https://github.com/user-attachments/assets/3352344f-cc99-4142-9cda-8d73f8e93635" />


# Pharmacy Management System

A simple JavaFX application for managing a pharmacy's inventory, sales, suppliers, and clients.

## Prerequisites
- Java 17 or higher
- Maven
- MySQL Server 8.0+

## Database Setup
1. Create a MySQL database named `pharmacy_db`.
2. Run the provided SQL script `schema.sql` to initialize tables and users.
   ```bash
   mysql -u root -p < schema.sql
   ```
   *Note: Detailed user creation script is included in schema.sql. Default admin is 'admin'/'admin'.*

## Configuration
Update the database credentials in `src/main/java/pharmacie/util/DBConnection.java` if your local MySQL configuration differs from:
- User: `root`
- Password: `password`

## Running the Application
Use Maven to run the application:
```bash
mvn clean javafx:run
```

## Features
- **Authentication**: Role-based login (Admin/Employee).
- **Products**: CRUD operations, Stock tracking.
- **Sales**: Cart system, Auto-stock decrease, Transaction history.
- **Suppliers**: Manage supplier info.
- **Orders**: Create supplier orders and receive stock.
- **Clients**: Manage client info.
- **Reports**: View sales total and low stock alerts.

## Default Credentials
- **Admin**: `admin` / `admin`
- **Employee**: `user` / `user`

## Architecture
- **View**: JavaFX FXML (`src/main/resources/view`)
- **Controller**: JavaFX Controllers (`pharmacie.controller`)
- **Service**: Business Logic (`pharmacie.service`)
- **DAO**: Data Access (`pharmacie.dao`)
- **Model**: Entities (`pharmacie.model`)

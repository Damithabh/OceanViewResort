<div align="center">

# 🌊 Ocean View Resort Management System

**A sophisticated, 3-tier enterprise Java web application designed to streamline hotel reservations, automate billing, and enhance administrative oversight.**

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![JSP/Servlets](https://img.shields.io/badge/Jakarta_EE-EE0000?style=for-the-badge&logo=jakartaee&logoColor=white)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

[Explore The Docs](System_Documentation.md) · [Report Bug](https://github.com/Damithabh/OceanViewResort/issues) · [Request Feature](https://github.com/Damithabh/OceanViewResort/issues)

</div>

---

## 📖 About The Project

The **Ocean View Resort** reservation system is a digital transformation initiative built to replace chaotic, manual spreadsheet tracking. It provides a secure, role-based platform where hotel staff can manage room inventories, process dynamic bookings, and generate automated revenue reports simultaneously without causing physical database collisions.

### ✨ Key Features

- **🔐 Secure Role-Based Access:** SHA-256 hashed authentication separating `ADMIN` and `STAFF` privileges securely via HTTP Sessions.
- **📅 Smart Reservation Engine:** Built-in defenses against double-booking and invalid date chronologies, backed natively by MySQL triggers.
- **💰 Dynamic Pricing Strategies:** Utilizes OOP Strategy patterns to seamlessly adapt room rates based on logic (e.g., Seasonal vs. Standard pricing).
- **📊 Real-Time Administrative Reporting:** Denormalized SQL Views power automated occupancy graphs and revenue dashboards.
- **🔔 Asynchronous Notifications:** Implements the Observer pattern to trigger simulated Email/SMS alerts and Audit Logs without freezing the UI.

---

## 🏗️ System Architecture

The project strictly adheres to a decouple **Three-Tier Architecture** to ensure long-term maintainability and scalability.

1. **Presentation Layer (Frontend):** Responsive, mobile-first design leveraging HTML5, CSS3, and Bootstrap 5 powered by dynamic JavaServer Pages (JSP).
2. **Business Logic Layer (Backend):** Deeply object-oriented Java Servlet controllers orchestrating Service classes, Design Patterns, and data validation.
3. **Data Access Layer (Storage):** A rigorously normalized MySQL 8.0 database enforcing relational integrity through `ON DELETE RESTRICT` foreign keys, `Triggers`, and `Stored Procedures`.

### 🧩 Core Design Patterns Implemented
* **Singleton:** `DBConnectionPool` prevents resource exhaustion.
* **Data Access Object (DAO):** Isolates native SQL logic from business processing.
* **Strategy:** Dynamically swaps pricing algorithms at runtime.
* **Observer:** Asynchronously handles event-driven tasks like Audit Logging and Alerts.
* **MVC (Model-View-Controller):** The backbone routing mechanism of the application.

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   Java Development Kit (JDK) 11 or higher
*   Apache Tomcat 9.0+
*   MySQL Server 8.0+
*   Eclipse IDE for Enterprise Java (or IntelliJ IDEA Ultimate)

### Installation

1. **Clone the repository**
   ```sh
   git clone https://github.com/Damithabh/OceanViewResort.git
   ```
2. **Initialize the Database**
   * Open your MySQL client.
   * Run the provided database initialization script:
   ```sql
   SOURCE /path/to/project/database_schema.sql;
   ```
3. **Configure Credentials**
   * Navigate to `src/main/resources/util/db.properties`.
   * Update the file with your local MySQL credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/ocean_view_resort
   db.user=root
   db.password=your_password_here
   ```
4. **Deploy to Tomcat**
   * Import the project into your IDE as a Dynamic Web Project.
   * Add the project to your configured Apache Tomcat server.
   * Right-click the project -> `Run As` -> `Run on Server` at `http://localhost:8080/OceanViewResort`.

---

## 🧪 Testing (Test-Driven Development)

This system was built with resilience in mind using **Test-Driven Development (TDD)** methodologies. Automated testing was conducted via JUnit 5 and Mockito to simulate and validate edge-cases before deployment.

- ✅ **Validates Happy Paths:** Confirms login, booking creations, and bill mathematical processing.
- 🛡️ **Validates Boundaries:** Defends against null pointers, max-length boundaries, and concurrent reservation collisions.

*For full testing traceability metrics and QA coverage, viewing the [System Documentation](System_Documentation.md) is highly recommended.*

---

## 🤝 Contribution & Versioning

This project adheres strictly to professional Software Configuration Management (SCM) standards, utilizing a GitFlow-inspired branching strategy and Atomic Commits to ensure clean rollback points and continuous integration readiness.

*Maintained by: [Damithabh](https://github.com/Damithabh)*

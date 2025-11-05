# 🏥 Advanced Hospital Management System

> A sleek and modular **Hospital Management System** built using **Java**, **Hibernate ORM**, and **PostgreSQL**, featuring clean entity relationships, appointment management, and exception-safe transaction handling.

---

## ⚙️ Tech Stack

| Technology | Description |
|-------------|-------------|
| ☕ **Java** | Core backend logic and CLI dashboard |
| 🧱 **Hibernate ORM** | Object-Relational Mapping between entities |
| 🐘 **PostgreSQL** | Relational database for data persistence |
| 🧩 **JPA Annotations** | Entity relationships (`@Entity`, `@ManyToOne`, `@JoinColumn`) |
| 💻 **IntelliJ IDEA** | Development Environment |

---

## 🚀 Features

✅ Add and View **Patients**  
✅ Add and View **Doctors**  
✅ Book Appointments with availability validation  
✅ Handles exceptions and transaction rollbacks gracefully  
✅ Structured ORM mapping between Patient, Doctor, and Appointment entities  
✅ Dynamic **Console Dashboard** for smooth user interaction  

---

## 🧩 Entity Relationship
- Each appointment links one patient to one doctor.
- A doctor can have up to **5 appointments per day**.

---

## 📂 Project Structure

com.hospital.riku
├── Appointment.java
├── Doctor.java
├── Patient.java
├── Management.java
└── Config.java


---

## 🧠 Learning Highlights

- Mastered Hibernate configuration and SessionFactory management  
- Implemented Many-to-One relationships using JPA annotations  
- Practiced transaction management and rollback strategies  
- Enhanced understanding of exception-safe database operations  

---

## 🖥️ How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/HospitalManagementSystem.git
   cd HospitalManagementSystem
🤝 Contributing

Feel free to fork, star ⭐, and raise issues or feature requests!
Contributions and suggestions are always welcome.

# 📚 Library Management System

## Project Overview
The **Library Management System (LMS)** is a web-based application developed to manage library operations efficiently and systematically. This project was created as the **final project for the Object-Oriented and Visual Programming (OOVP) course** at President University.
The system digitalizes core library activities such as book management, borrowing and returning transactions, member management, and reporting. It applies Object-Oriented Programming (OOP) principles and simulates a real-world library system with role-based access for administrators (librarians) and members (students).

---

## 🎯 Project Objectives

* Automate manual library processes to reduce errors and inefficiencies
* Implement Object-Oriented Programming concepts in a real-world system
* Build a full-stack Java web application using Servlet and JSP
* Provide role-based access control for Admin and Member users
* Enable real-time book availability tracking and transaction history

---

## 🛠️ Tech Stack & Tools

This project was developed using the following technologies:

* **Programming Language:** Java
* **Web Technologies:** Java Servlet, JSP (JavaServer Pages)
* **Database:** MySQL
* **Server:** Apache Tomcat
* **IDE:** NetBeans IDE
* **Database Connectivity:** JDBC
* **Architecture:** Client–Server Architecture (MVC-based approach)

---

## ⭐ Key Features

### 👨‍💼 Admin (Librarian) Features

* Secure admin authentication (login & register)
* Dashboard with real-time statistics:
  * Total books
  * Total members
  * Borrowed books
* Manage books (CRUD):
  * Add, edit, view, delete books
* Manage members (CRUD)
* Issue and return books
* Filter issued books:
  * Returned
  * Not Returned
  * Overdue
  * Extension Requests
* Process due date extension requests
* View complete borrowing history
* Search book and transaction history

### 👤 Member (User) Features

* Member registration and login
* Browse available books
* Borrow books online
* View currently borrowed books
* Request due date extensions
* Return borrowed books
* View borrowing history
* Edit personal profile information
* Dashboard overview (borrowed, overdue, history)

---

## 💡 System Highlights

* **Role-Based Access Control** (Admin & Member)
* **Real-Time Book Availability Tracking**
* **Automated Borrowing & Returning Process**
* **Extension Request Workflow**
* **Clean and Structured OOP Design**
* **Responsive and User-Friendly Interface**
* **Centralized Database for Accurate Record Keeping**

---

## 🧱 System Architecture

The system follows a **Client–Server Architecture**:

* **Client:** Web browser (Admin & Member UI)
* **Server:** Apache Tomcat running Java Servlets & JSP
* **Database Server:** MySQL
* **Communication:** HTTP (Client ↔ Server), JDBC (Server ↔ Database)

---

## ⚙️ Installation & Setup Guide

### 1️⃣ Prerequisites

Make sure you have the following installed:

* Java JDK (JDK 8 or higher)
* NetBeans IDE
* Apache Tomcat Server
* MySQL Server
* Web Browser (Chrome / Edge recommended)

---

### 2️⃣ Database Setup

1. Open **MySQL**
2. Create a new database:

   ```sql
   CREATE DATABASE library_management_system;
   ```
3. Import the provided SQL file into the database
4. Update database credentials in the project configuration (JDBC connection)

---

### 3️⃣ Project Setup in NetBeans

1. Open **NetBeans IDE**
2. Select **Open Project**
3. Import the Library Management System project
4. Configure **Apache Tomcat** as the server
5. Ensure JDBC MySQL Connector is added to the project libraries

---

### 4️⃣ Run the Application

1. Start Apache Tomcat from NetBeans
2. Run the project
3. Access the system via browser:

   ```
   http://localhost:8080/LibraryManagementSystem
   ```

---

## 📄 Project Report

For a complete explanation including problem analysis, system design, ERD, use case diagrams, screenshots, and code structure, please refer to the full project report:

🔗 **Project Report (PDF):**
👉 (https://drive.google.com/file/d/1dOYrkJ2QG_ijDLPN98D095w-JhKxWUsT/view?usp=sharing)

---

## 👥 Project Team

**Information System Class 3 – Group 3**
President University

* Fasya Nabila Salim
* Fatwa Putri Jingga
* Marsha Aulia Rizky
* Salwa Reva Andini

---

## 📖 `README.md` for **Vidify-Backend**

# 🎥 Vidify – Backend

The **backend** of Vidify, a modern **video streaming platform**, built with **Spring Boot**.  
This repository powers the **APIs for video uploads, streaming, and deleting**.



## 🚀 Features
- 📡 **REST APIs** for frontend integration  
- 🔐 **User Authentication** with Clerk  
- 🎬 **Video Management** (upload, fetch, delete)  
- 💾 **Database Integration** with MySQL  
- 🌍 **CORS enabled** for frontend communication  



## 🖥️ Prerequisites
Before running this project, make sure you have the following installed:

- **Java 17+**  
- **Maven 3.8+**  
- **MySQL 8+** (or PostgreSQL)  
- **Git**  





## 🛠️ Tech Stack
- **Spring Boot** – Backend framework  
- **Spring Data JPA** – ORM  
- **MySQL/PostgreSQL** – Database  
- **Clerk** – Authentication integration  
- **Maven** – Build tool  



## ⚡ Getting Started

1. Clone the repository  
   ```bash
   git clone https://github.com/Sk2112/Vidify-Backend.git

2. Set up your database (MySQL/Postgres) and update application.properties
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/vd5
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Run the backend
   ```
   ./mvnw spring-boot:run
   ```

## 📂 Example API Endpoints
- POST /api/videos – Upload video metadata
- GET /api/videos – Fetch all videos
- GET /api/videos/{id} – Fetch single video
- DELETE /api/videos/{id} – Delete a video

## 📌 Roadmap
- Implement live streaming support
- Enhance security & validation

## 🤝 Contributing
- Contributions are welcome! Please fork the repo and create a pull request.

## 📧 Contact
Built with ❤️ by Sumit Kumar
- LinkedIn : https://www.linkedin.com/in/21sk12/


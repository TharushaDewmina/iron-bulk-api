# Iron Bulk API

A stateless RESTful backend built with **Java 21**, **Spring Boot 4**, **Spring Security (JWT)**, and **MySQL** for tracking hypertrophy training volume and daily lean-bulk macronutrient targets.

## Tech Stack
* **Language & Framework:** Java 21, Spring Boot 4.1.2, Spring Data JPA (Hibernate 7)
* **Security:** Spring Security, Stateless JWT (`jjwt`), BCrypt Password Hashing, CORS
* **Database:** MySQL / MariaDB
* **Validation & Error Handling:** Jakarta Bean Validation, Global `@RestControllerAdvice`

## Database Schema
* **`users`**: Stores credentials (`password_hash`) and personalized daily calorie/protein targets.
* **`workouts`**: Tracks training sessions (`PUSH`, `PULL`, `LEGS`, `UPPER`, `LOWER`, `FULL_BODY`) per user.
* **`workout_sets`**: Stores individual exercise sets (`weight_kg`, `reps`, `is_drop_set`) linked to a workout.
* **`meal_logs`**: Tracks weighed food entries (`grams`, `calories`, `protein_g`, `carbs_g`, `fats_g`) by date.

## API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user with daily macro targets and receive a JWT |
| `POST` | `/api/auth/login` | Authenticate user credentials and receive a JWT |

### Workouts & Volume Tracking (Protected — Bearer Token)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/workouts` | Log a workout session with nested sets and auto-calculated total volume (kg) |
| `GET` | `/api/workouts` | Fetch user workout history (optional `?date=YYYY-MM-DD` filter) |
| `DELETE` | `/api/workouts/{id}` | Delete an owned workout session |

### Nutrition & Macro Summary (Protected — Bearer Token)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/meals` | Log a weighed meal entry |
| `GET` | `/api/meals` | Fetch daily meal logs and remaining calories/protein vs. user targets (`?date=YYYY-MM-DD`) |
| `DELETE` | `/api/meals/{id}` | Delete an owned meal entry |

## Local Setup

1. **Create the MySQL database:**
   ```sql
   CREATE DATABASE iron_bulk_db;

import React, { useState } from "react";
import UserRequestDTO from "../models/UserRequestDTO";
import { AuthService } from "../../../AuthService";
import styles from "../components/CreateUser.module.css";
// import Footer from "assets/Footer";
import LeagueImage from "assets/LeagueAlertsImg.jpg";

const CreateUserForm: React.FC = () => {
  const [formData, setFormData] = useState<UserRequestDTO>({
    email: "",
    firstName: "",
    lastName: "",
    password: "",
    connection: "Username-Password-Authentication",
  });
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const authService = new AuthService();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccessMessage(null);
    setErrorMessage(null);
    try {
      const response = await authService.createUser(formData);
      setSuccessMessage(`User created successfully! ID: ${response.user_id}`);
    } catch (error: any) {
      setErrorMessage(error.message || "Failed to create user");
    }
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>Sign Up</h1>
      <div className={styles.formContainer}>
        <div className={styles.imgContainer}>
          <img src={LeagueImage} alt="League Alerts" className={styles.img} />
        </div>
        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.inputGroup}>
            <label className={styles.label}></label>
            <input
              placeholder="Enter your email"
              className={styles.input}
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className={styles.inputGroup}>
            <label className={styles.label}></label>
            <input
              placeholder="Enter your first name"
              className={styles.input}
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className={styles.inputGroup}>
            <label className={styles.label}></label>
            <input
              placeholder="Enter your last name"
              className={styles.input}
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className={styles.inputGroup}>
            <label className={styles.label}></label>
            <input
              placeholder="Enter your password"
              className={styles.input}
              type="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              required
            />
          </div>
          <button className={styles.button} type="submit">
            Create User
          </button>
        </form>
      </div>
      {successMessage && (
        <p className={styles.successMessage}>{successMessage}</p>
      )}
      {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
      {/* <Footer/> */}
    </div>
  );
};

export default CreateUserForm;

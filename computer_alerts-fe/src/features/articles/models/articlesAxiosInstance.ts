import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1", // Adjust to match your backend
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosInstance;

import React, { useState, useEffect } from "react";
import { AuthService } from "../Service/AuthService";
import axios from "axios";
import Cookies from "js-cookie";

const Login: React.FC = () => {
  const [authService] = useState(new AuthService());
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
    // Check for accessToken in localStorage to set initial authentication state
    return !!localStorage.getItem("accessToken");
  });
  const [, setUserInfo] = useState<{
    name: string;
    roles: string[];
  } | null>(null);

  useEffect(() => {
    if (isAuthenticated) {
      // Fetch user info if already authenticated (access token exists)
      fetchUserInfo();
    }
  }, [isAuthenticated]);

  const fetchUserInfo = async () => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) {
        console.error("No access token found.");
        return;
      }

      const response = await fetch(
        "https://dolphin-app-sxvxi.ondigitalocean.app/api/userInfo",
        // "http://localhost:8080/api/userInfo",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          credentials: "include",
        },
      );

      if (response.ok) {
        const userData = await response.json();
        console.log("User data:", userData);
        Cookies.set("name", userData.name);
        Cookies.set("roles", JSON.stringify(userData.roles));
        setUserInfo(userData);
      } else {
        console.error("Failed to fetch user info:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  const handleLogin = async () => {
    try {
      console.log("Starting login process...");
      await authService.login();
      console.log("Login successful!");

      // Assume `authService.login()` stores accessToken in localStorage
      setIsAuthenticated(true);
      await fetchUserInfo();
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.error("Axios Error:", error.response);
      } else {
        console.error("Login failed:", error);
      }
    }
  };

  const handleLogout = async () => {
    try {
      await authService.logout();
      localStorage.removeItem("accessToken"); // Remove access token
      setIsAuthenticated(false);
      setUserInfo(null);
    } catch (error) {
      console.error("Logout failed", error);
    }
  };

  return (
    <div>
      {!isAuthenticated ? (
        <button onClick={handleLogin}>Log In</button>
      ) : (
        <div>
          <button onClick={handleLogout}>Log Out</button>
        </div>
      )}
    </div>
  );
};

export default Login;

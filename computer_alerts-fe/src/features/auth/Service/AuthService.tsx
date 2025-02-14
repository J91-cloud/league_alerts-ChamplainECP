import { createAuth0Client, Auth0Client } from "@auth0/auth0-spa-js";
import axios from "axios";
import UserRequestDTO from "features/readers/models/UserRequestDTO";
import AuthorRequestDTO from "../../authors/model/AuthorRequestDTO";
export class AuthService {
  URL = "https://dolphin-app-sxvxi.ondigitalocean.app/api/"; // Your backend URL
  // URL = "http://localhost:8080/api/"

  private auth0Client: Auth0Client | null = null;

  private AUTH0_DOMAIN = process.env.REACT_APP_API_DOMAIN;

  constructor() {
    this.initializeAuth0();
  }

  private async initializeAuth0() {
    console.log("Initializing Auth0...");
    this.auth0Client = await createAuth0Client({
      domain: "dev-im24qkb6l7t2yhha.ca.auth0.com",
      clientId: "COuKmAH95MAHPN2irCzsuOearf2gdsOH",
      authorizationParams: {
        redirect_uri: "https://league-alerts.web.app",
        audience: "https://dolphin-app-sxvxi.ondigitalocean.app/api/userInfo",
        // redirect_uri: "http://localhost:3000",
        // audience: "http://localhost:8080/api/userInfo",
        scope: "openid profile email roles",
      },
    });
  }

  private async ensureAuth0Client(): Promise<void> {
    if (!this.auth0Client) {
      await this.initializeAuth0();
    }
  }

  public async isAuthenticated(): Promise<boolean> {
    await this.ensureAuth0Client();
    const authenticated = await this.auth0Client!.isAuthenticated();
    console.log("Is authenticated:", authenticated);
    return authenticated;
  }

  public async login() {
    await this.ensureAuth0Client();
    await this.auth0Client!.loginWithPopup();
    const isAuthenticated = await this.isAuthenticated();
    if (isAuthenticated) {
      console.log("Logged in successfully.");
      const token = await this.getToken();

      localStorage.setItem("accessToken", token);
    } else {
      console.log("Login failed.");
    }
  }

  public async logout(): Promise<void> {
    await this.ensureAuth0Client();
    console.log("Logging out...");
    await this.auth0Client!.logout({
      logoutParams: {
        returnTo: window.location.origin,
      },
    });
  }

  // Get the access token for making API requests
  public async getToken(): Promise<string> {
    await this.ensureAuth0Client();
    const token = await this.auth0Client!.getTokenSilently();
    console.log("Retrieved token:", token);
    return token;
  }

  // Get user roles from the ID token claims
  public async getUserRoles(): Promise<string[]> {
    await this.ensureAuth0Client();
    const claims = await this.auth0Client!.getIdTokenClaims();
    console.log("User roles:", claims?.["roles"]);
    return claims?.["roles"] || [];
  }

  // Get the Management API token

  //////////////////////////////////////////////////////////
  private async getManagementApiToken(): Promise<string> {
    try {
      const response = await axios.post(
        `https://dev-im24qkb6l7t2yhha.ca.auth0.com/oauth/token`,
        {
          client_id: process.env.REACT_APP_API_ClIENT_ID,
          client_secret: process.env.REACT_APP_API_SECREAT,
          audience: process.env.REACT_APP_API_AUDIENCE,
          grant_type: "client_credentials",
        },
      );

      const token = response.data.access_token;
      return token;
    } catch (error: any) {
      console.error(
        "Error fetching Management API token:",
        error.response?.data || error.message,
      );
      throw new Error("Failed to fetch Management API token");
    }
  }

  async createUser(userRequest: UserRequestDTO): Promise<any> {
    try {
      // First create the author
      const response = await fetch(this.URL + "create/Reader", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userRequest),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Failed to create author");
      }

      const readerResponse = await response.json();

      const roleId = "[rol_LOREG4N5742ObYCz]";

      const encodeAuthUserId = readerResponse.auth0UserId.replace("|", "%7C");

      await fetch(this.URL + `create/${encodeAuthUserId}/assign-role/Reader`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${this.getManagementApiToken}`,
        },
        body: JSON.stringify(roleId),
      });

      return readerResponse;
    } catch (error: any) {
      console.error("Full error:", error);
      throw new Error(
        error.response?.data || "Failed to create user or assign roles",
      );
    }
  }

  async createAuthor(authorData: AuthorRequestDTO): Promise<any> {
    try {
      const accessToken = localStorage.getItem("accessToken");
      // First create the author
      const response = await fetch(this.URL + "create/Author", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(authorData),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Failed to create author");
      }

      const authorResponse = await response.json();

      // const managementApiToken = await this.getManagementApiToken();
      const roleId = "[rol_W1iELc1CHmzBtfE4]";

      const encodeAuthUserId = authorResponse.auth0UserId.replace("|", "%7C");

      await fetch(this.URL + `create/${encodeAuthUserId}/assign-role/Author`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",

          Authorization: `Bearer ${accessToken}`,
          //  Authorization: `Bearer ${managementApiToken}`,
        },
        body: JSON.stringify(roleId),
      });

      return authorResponse;
    } catch (error) {
      console.error("Error in createAuthor:", error);
      throw error;
    }
  }
}

const authTokenService = new AuthService();
export default authTokenService;

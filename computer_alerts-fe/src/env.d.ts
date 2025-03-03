declare namespace NodeJS {
  //add all of your evironment variables in this file
  interface ProcessEnv {
    REACT_APP_API_DOMAIN: string;
    REACT_APP_API_SECREAT: string;
    REACT_APP_API_AUDIENCE: string;
    REACT_APP_API_ClIENT_ID: string;
  }
}

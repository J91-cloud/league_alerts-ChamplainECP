import './App.css';
import { AppRoutePaths } from './shared/models/path.routes';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AppNavBar from './layouts/AppNavBar';
import HomePage from './pages/Home/HomePage';
import ArticleDetails from 'features/articles/components/ArticleDetails';
import AuthorPage from 'pages/AuthorPage';
// import LogoutButton from "./Logout";

function App(): JSX.Element {
  return (
    <div>
      <Router>
        <AppNavBar />
        <Routes>
          <Route path={AppRoutePaths.HomePage} element={<HomePage />} />
          <Route path="/articles/:id" element={<ArticleDetails />} />
          <Route path={AppRoutePaths.Authors} element={<AuthorPage />} />
        </Routes>
      </Router>
      {/* <ReaderList/> */}
    </div>
  );
}

export default App;

import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./AppNavBar.css";
import { AppRoutePaths } from "../shared/models/path.routes";
import { Navbar, Container, Nav } from "react-bootstrap";

export default function AppNavBar(): JSX.Element {
  return (
    <Navbar expand="lg" className="navbar">
      <Container>
        <Navbar.Brand
          as={Link}
          to={AppRoutePaths.HomePage}
          className="navbar-brand"
        >
          Home
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav">
          <span>☰</span>
        </Navbar.Toggle>
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link as={Link} to={AppRoutePaths.NBA} className="nav-link">
              NBA
            </Nav.Link>
            <Nav.Link as={Link} to={AppRoutePaths.NHL} className="nav-link">
              NHL
            </Nav.Link>
            <Nav.Link as={Link} to={AppRoutePaths.NFL} className="nav-link">
              NFL
            </Nav.Link>
            <Nav.Link as={Link} to={AppRoutePaths.UFC} className="nav-link">
              UFC
            </Nav.Link>
            <Nav.Link as={Link} to={AppRoutePaths.MLB} className="nav-link">
              MLB
            </Nav.Link>
            <Nav.Link
              as={Link}
              to={AppRoutePaths.CREATE_ACCOUNT}
              className="nav-link"
            >
              Create Account
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

import React from "react";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import './Styles/Home.scss';
import { useUserContext } from "../AppContextProvider";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

const Home = () => {
  const { user, isLogged } = useUserContext();

  return (
    <div className="home-background">
      <Container className="home-content">
        <h1 className="animated-header">Witaj, {user?.firstName || "Gościu"}!</h1>
        <p>
          Jeśli masz trochę czasu, a akurat potrzebujesz gotówki, praca dorywcza może być rozwiązaniem.
        </p>
        <Button className="animated-button" variant="primary" href="/oferty">
          Przeglądaj oferty
        </Button>
      </Container>

      <Container className="features-section">
        <h2>Dlaczego warto nas wybrać?</h2>
        <Row className="features-grid">
          <Col xs={12} md={4} className="feature-card">
            <i className="fas fa-search"></i>
            <h3>Łatwe wyszukiwanie</h3>
            <p>Znajdź idealną pracę w kilka sekund.</p>
          </Col>
          <Col xs={12} md={4} className="feature-card">
            <i className="fas fa-shield-alt"></i>
            <h3>Bezpieczne oferty</h3>
            <p>Sprawdzamy oferty, by zapewnić Ci bezpieczeństwo.</p>
          </Col>
          <Col xs={12} md={4} className="feature-card">
            <i className="fas fa-users"></i>
            <h3>Wsparcie społeczności</h3>
            <p>Dzięki nam pracodawcy i pracownicy współpracują.</p>
          </Col>
        </Row>
      </Container>

      {!isLogged ? (
        <Container className="call-to-action-section">
          <h2>Dołącz do nas już dziś!</h2>
          <p>Zarejestruj się i znajdź idealną pracę dorywczą lub dodaj swoją ofertę pracy.</p>
          <Button className="animated-button" variant="success" href="/rejestracja">
            Zarejestruj się
          </Button>
        </Container>
      ) : null}

    </div>
  );
};

export default Home;

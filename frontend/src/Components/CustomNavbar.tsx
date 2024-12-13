import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Card from 'react-bootstrap/Card'
import { Link } from 'react-router-dom';
import logo from '../Assets/logo.svg';
import './Styles/CustomNavbar.scss';
import { useNavigate } from 'react-router-dom';

import { useUserContext } from '../AppContextProvider';

const CustomNavbar = () => {
    const { isEmployer } = useUserContext();
    const { isLogged } = useUserContext();
    const { user } = useUserContext();
    const navigate = useNavigate();

    const notifications: any[] = []

    const handleLogout = () => {
        navigate('/logout');
    }

    return (
        <Navbar expand="lg" className="bg-body-tertiary" data-bs-theme="dark">
            <Container>
                <Navbar.Brand as={Link} to="/">
                    <img
                        src={logo}
                        height="30"
                        width="84.44"
                        className="d-inline-block align-top"
                        alt="doryw.pl logo"
                    />
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/oferty">Oferty</Nav.Link>
                    </Nav>
                    <Nav className='ms-auto '>
                        {/* <NavDropdown title={<i className="bi bi-bell-fill avatar" aria-label='powiadomienia'></i>} id="notification-dropdown">
                            {notifications.length > 0 ? (
                                notifications.map((notification, index) => (
                                    <NavDropdown.Item key={index}>
                                        <Card>
                                            <Card.Body>
                                                <Card.Title>{notification.title}</Card.Title>
                                                <Card.Text>{notification.message}</Card.Text>
                                            </Card.Body>
                                        </Card>
                                    </NavDropdown.Item>
                                ))
                            ) : (
                                <Container className='page-container'>
                                    <Card>
                                        <Card.Body>
                                            <Card.Text>Brak powiadomień</Card.Text>
                                        </Card.Body>
                                    </Card>
                                </Container>
                            )}
                        </NavDropdown> */}
                        <NavDropdown title={
                            <>
                                <span>{isLogged ? 'Witaj, ' + user.firstName + '!' : ''}</span>
                                <i className="bi bi-person-fill avatar"></i>
                            </>
                        }>
                            {isLogged ? <NavDropdown.Item as={Link} to="/uzytkownik">Panel użytkownika</NavDropdown.Item> : null}
                            <NavDropdown.Item as={Link} to="/pracodawca">{isEmployer ? 'Panel pracodawcy' : 'Zostań pracodawcą'}</NavDropdown.Item>
                            {isLogged ? null : <NavDropdown.Item as={Link} to="/logowanie">Zaloguj</NavDropdown.Item>}
                            {isLogged ? <NavDropdown.Item onClick={handleLogout}>Wyloguj</NavDropdown.Item> : null}
                            {!isLogged ? <NavDropdown.Item as={Link} to="/rejestracja">Zarejestruj</NavDropdown.Item> : null}
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default CustomNavbar;

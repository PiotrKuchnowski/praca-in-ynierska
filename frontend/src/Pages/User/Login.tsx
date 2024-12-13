import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Alert from "react-bootstrap/Alert";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import "../../Styles/Spinner.scss";
import { Link, useNavigate } from "react-router-dom";
import logo from "../../Assets/logo.svg";
import { login } from "../../Api/Users"
import { Spinner } from "react-bootstrap";
import { isAxiosError } from "axios";
import "./Styles/Login.scss";

import { useUserContext } from "../../AppContextProvider";

const Login = () => {
 
    const { setUser, setIsLogged, setIsEmployer} = useUserContext();
    
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const [password, setPassword] = useState('');
    const [redirecting, setRedirecting] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setLoading(true);
        setSuccessMessage('');
        setErrorMessage('');
        try{
            const responseData = await login(email, password);
            if (responseData.code === 200) {
                setRedirecting(true);
                setSuccessMessage('Zalogowano pomyślnie! Trwa przekierowanie...');
                setIsLogged(true);
                localStorage.setItem('wasLogged', 'true');
                setUser(responseData.data.user);
                setIsEmployer(responseData.data.user.isEmployer);
                setTimeout(() => {
                    navigate('/');
                }, 2000);
            } else {
                setErrorMessage('Logowanie nieudane: ' + responseData.message);
            }
        } catch (error) {
            console.log(error);
            if (isAxiosError(error)) {
                console.log("here");
                if (error.response && error.response.data && error.response.data.exception) {
                    console.log("here2");
                    setErrorMessage('Wystąpił błąd podczas logowania: ' + error.response.data.exception);
                }
            }else {
                console.log("here3");
                setErrorMessage('Wystąpił błąd podczas logowania: ' + error);
            }
            console.log(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="container-relative">
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item active>Logowanie</Breadcrumb.Item>
            </Breadcrumb>
            {loading && (
                <div className="login-in-overlay">
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                </div>
            )}
            <Container className="login-background">
                <Container >
                    <img
                        src={logo}
                        alt="doryw.pl logo"
                        className="logo"
                    />
                </Container>
                <Container className="login-box">
                    {successMessage && (
                        <Alert variant="success" className="text-center">
                            {successMessage}
                        </Alert>
                    )}
                    {errorMessage && (
                        <Alert variant="danger" className="text-center">
                            {errorMessage}
                        </Alert>
                    )}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formBasicEmail" className="form-group">
                            <Form.Label>Adres e-mail:</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Wprowadź adres mailowy"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formBasicPassword" className="form-group">
                            <Form.Label>Hasło:</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Wprowadź hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="button" disabled={loading || redirecting}>
                            Zaloguj
                        </Button>
                    </Form>
                    {!redirecting && !loading && (
                        <Nav className="link" as={Link} to={"resetuj-haslo"}>Zapomniałem hasła</Nav>
                    )}
                </Container>
                {!redirecting && !loading && (
                    <Container className="register">
                        <p>Nie masz jeszcze konta? <Link className="link" to="/rejestracja">Zarejestruj się!</Link></p>
                    </Container>
                )}
            </Container>
        </Container>
    );
};

export default Login;
/* eslint-disable @typescript-eslint/no-unused-vars */
import React from "react";
import { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import DatePicker from "react-datepicker";
import logo from "../../Assets/logo.svg";
import { format } from "date-fns";
import "./Styles/Login.scss";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../../Api/Users";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/esm/Alert";
import Breadcrumb from "react-bootstrap/Breadcrumb";



const comparePassword = (password: string, passwordRepeat: string) => {
    return password === passwordRepeat;
}

const Register = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordRepeat, setPasswordRepeat] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [birthDate, setBirthDate] = useState("1999-01-01");
    const [loading, setLoading] = useState(false);
    const [redirecting, setRedirecting] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const verificationURL = "/uzytkownik/zweryfikuj-konto";
            const responseData = await register(email, password, firstName, lastName, format(birthDate, 'yyyy-MM-dd').toString(), verificationURL);
            if (responseData.code === 201) {
                setRedirecting(true);
                setSuccessMessage('Zarejestrowano pomyślnie! Trwa przekierowanie...');
                console.log(responseData);
                setTimeout(() => {
                    navigate('/logowanie');
                }, 2000); // Redirect after 2 seconds
            } else {
                setErrorMessage('Rejestracja nieudana: ' + responseData.message);
            }
            console.log(responseData);
        } catch (error) {
            setErrorMessage('Wystąpił błąd podczas rejestracji: ' + error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="container-relative">
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item active>Rejestracja</Breadcrumb.Item>
            </Breadcrumb>
            {loading && (
                <div className="login-in-overlay">
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                </div>
            )}

            <Container className="login-background">
                <Container>
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
                    <Form onSubmit={(e) => {
                        let preventDefault = false;
                        let alertMessage = "";
                        if (email === "") {
                            preventDefault = true;
                            alertMessage += "Wprowadź adres e-mail\n";
                        }
                        if (password === "") {
                            preventDefault = true;
                            alertMessage += "Wprowadź hasło\n";
                        }
                        if (passwordRepeat === "") {
                            preventDefault = true;
                            alertMessage += "Powtórz hasło\n";
                        }
                        if (firstName === "") {
                            preventDefault = true;
                            alertMessage += "Wprowadź imię\n";
                        }

                        if (lastName === "") {
                            preventDefault = true;
                            alertMessage += "Wprowadź nazwisko\n";
                        }

                        if (birthDate === null) {
                            preventDefault = true;
                            alertMessage += "Wprowadź datę urodzenia\n";
                        }

                        if (preventDefault) {
                            alert(alertMessage);
                            e.preventDefault();
                        } else {
                            handleSubmit(e);
                        }
                    }}>
                        <Form.Group controlId="formEmail" className="form-group">
                            <Form.Label>Adres e-mail:</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Wprowadź adres mailowy"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />

                        </Form.Group>

                        <Form.Group controlId="formPassword" className="form-group">
                            <Form.Label>Hasło:</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Wprowadź hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group controlId="formPasswordRepeat" className="form-group">
                            <Form.Label>Powtórz hasło:</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Powtórz hasło"
                                value={passwordRepeat}
                                onChange={(e) => {
                                    setPasswordRepeat(e.target.value);
                                    if (!comparePassword(password, e.target.value)) {
                                        e.target.setCustomValidity("Hasła muszą być takie same");
                                    } else {
                                        e.target.setCustomValidity("");
                                    }
                                }}
                            />
                        </Form.Group>

                        <Form.Group controlId="formFirstName" className="form-group">
                            <Form.Label>Imię:</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Wprowadź imię"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group controlId="formLastName" className="form-group">
                            <Form.Label>Nazwisko:</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Wprowadź nazwisko"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group controlId="formBirthDate" className="form-group">
                            <Form.Label>Data urodzenia:</Form.Label>
                            <Form.Control
                                type="date"
                                placeholder="Data urodzenia"
                                value={birthDate}
                                onChange={(e) => setBirthDate(e.target.value)}
                            />
                        </Form.Group>

                        <Button
                            variant="primary"
                            type="submit"
                            className="button"
                        >
                            Zarejestruj
                        </Button>
                    </Form>
                </Container>
                <Container className="register">
                    <p>Masz już konto? <Link to="/logowanie">Zaloguj się!</Link></p>
                </Container>
            </Container>
        </Container>
    );
};

export default Register;
/* eslint-enable @typescript-eslint/no-unused-vars */
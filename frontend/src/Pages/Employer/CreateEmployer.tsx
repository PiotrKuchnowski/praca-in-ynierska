/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useEffect } from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Alert from "react-bootstrap/Alert";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Nav from "react-bootstrap/Nav";
import Spinner from "react-bootstrap/Spinner";
import { useUserContext } from "../../AppContextProvider";
import logo from "../../Assets/logo.svg";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import "../User/Styles/Login.scss";
import { createEmployer } from "../../Api/Employers";

const CreateEmployer = () => {
    const { user, isEmployer, setIsEmployer } = useUserContext();

    const [isNaturalPerson, setIsNaturalPerson] = useState(true);
    const [companyName, setCompanyName] = useState('');
    const [hasNIP, setHasNIP] = useState(false);
    const [companyNIP, setCompanyNIP] = useState('');
    const [description, setDescription] = useState('');

    const [loading, setLoading] = useState(false);
    const [redirecting, setRedirecting] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setLoading(true);
        try {
            const verificationUrl = "/pracodawca/zweryfikuj-konto";
            const responseData = await createEmployer(user.referenceId, companyName, companyNIP, description, isNaturalPerson, verificationUrl);
            if (responseData.code === 201) {
                setIsEmployer(true);
                setRedirecting(true);
                setSuccessMessage('Utworzono konto pracodawcy pomyślnie! Trwa przekierowanie...');
                setTimeout(() => {
                    navigate('/pracodawca');
                }, 2000); // Redirect after 2 seconds
            } else {
                setErrorMessage('Utworzenie konta pracodawcy nieudane: ' + responseData.message);
            }
        } catch (error) {
            setErrorMessage('Wystąpił błąd podczas tworzenia konta pracodawcy: ' + error);
        } finally {
            setLoading(false);

        };
    }

    useEffect(() => {
        if (isEmployer) {
            navigate('/pracodawca');
        }
    }, [isEmployer, navigate]);

    return (
        <Container className="container-relative">
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
                <Breadcrumb.Item active>Utwórz podkonto</Breadcrumb.Item>
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
                        <Nav variant="tabs" defaultActiveKey="1">
                            <Nav.Item>
                                <Nav.Link eventKey="1" onClick={() => setIsNaturalPerson(true)}>Osoba fizyczna</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link eventKey="2" onClick={() => setIsNaturalPerson(false)}>Firma</Nav.Link>
                            </Nav.Item>
                        </Nav>

                        {!isNaturalPerson && (
                            <Form.Group className="form-group">
                                <Form.Label>Nazwa firmy</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nazwa firmy"
                                    value={companyName}
                                    onChange={(e) => setCompanyName(e.target.value)}
                                    required
                                />
                            </Form.Group>
                        )}
                        {isNaturalPerson && (
                            <Form.Group className="form-group">
                                <Form.Check
                                    type="checkbox"
                                    checked={hasNIP}
                                    onChange={(e) => setHasNIP(e.target.checked)}
                                    label="Posiadam NIP jako osoba fizyczna"
                                />
                            </Form.Group>
                        )}
                        {(!isNaturalPerson || hasNIP) && (
                            <Form.Group className="form-group">
                                <Form.Label>NIP</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="NIP"
                                    value={companyNIP}
                                    onChange={(e) => setCompanyNIP(e.target.value)}
                                    required
                                />
                            </Form.Group>
                        )}
                        <Form.Group className="form-group">
                            <Form.Label>Opis działalności</Form.Label>
                            <Form.Control
                                as="textarea"
                                placeholder="Opis działalności"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="button" disabled={loading || redirecting}>
                            Utwórz podkonto
                        </Button>
                    </Form>
                </Container>
            </Container>
        </Container>
    );
}

export default CreateEmployer;
/* eslint-enable @typescript-eslint/no-unused-vars */
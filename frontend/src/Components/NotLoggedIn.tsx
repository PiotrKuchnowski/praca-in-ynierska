import React, { useEffect } from "react";
import Alert from "react-bootstrap/Alert";
import { useNavigate } from "react-router-dom";
import "./Styles/NotLoggedIn.scss";
import { Container } from "react-bootstrap";

const NotLoggedIn = () => {
    const navigate = useNavigate();

    const redirectMessage = 'Musisz być zalogowany aby przejść do tej strony. Przekierowywanie na stronę logowania...';

    

    useEffect(() => {
        const handleRedirect = () => {
            setTimeout(() => {
                navigate('/logowanie');
            }, 2000);
        }
        handleRedirect();
    }
    , [navigate]);


    return (
        <Container className="not-logged-in">
            <Alert variant="danger">{redirectMessage}</Alert>
        </Container>
    )
}

export default NotLoggedIn;

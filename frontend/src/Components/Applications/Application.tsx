import React from "react";
import { Button, Col, Row } from "react-bootstrap";
import { ApplicationProps } from "../../types";


const Application = (props: ApplicationProps) => {
    return (
        <Col style={{ alignItems: 'center' }}>
            <Col>
                <p>Imię: {props.application.user.firstName}</p>
                <p>Nazwisko: {props.application.user.lastName}</p>
                <p>Adres e-mail: {props.application.user.email}</p>
                <p>Numer telefonu: {props.application.user.phoneNumber ? props.application.user.phoneNumber : 'Nie podano'}</p>
                <p>Wiadomość: {props.application.message}</p>
            </Col>
            <Col style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignContent: 'center', alignItems: 'center' }}>
                {props.handleRejection && (
                    <Row className="mb-2">
                        <Button className="decision-button" variant="danger" size="sm" onClick={() => props.handleRejection && props.handleRejection()}>
                            Odrzuć zainteresowanie
                        </Button>
                    </Row>
                )}
                {props.handleAcceptance && (
                    <Row className="mb-2">
                    <Button className="decision-button" variant="success" size="sm" onClick={() => props.handleAcceptance && props.handleAcceptance()}>
                        Zaakceptuj zainteresowanie
                    </Button>
                </Row>
                )}
                
            </Col>
        </Col>
    );
}

export default Application;
import React from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import OfferDetails from "../Offers/OfferDetails";
import Application from "./Application";
import { ApplicationViewProps } from "../../types";
import './Styles/ApplicationView.scss';



const ApplicationView = (props: ApplicationViewProps) => {

    const getStatusClass = (status: string) => {
        var statusClassName = 'status-';
        switch (status) {
            case 'Zaakceptowano':
                return statusClassName + 'accepted';
            case 'Odrzucono':
                return statusClassName + 'rejected';
            case 'Zaaplikowano':
                return statusClassName + 'pending';
            default:
                return '';
        }
    }

    const handleOnClick = (status: String) => {
        if (props.acknowledgeApplication) {
            if (props.setMessage) {
                console.log(status);
                switch (status) {
                    case 'Zaaplikowano':
                        props.setMessage('Czy na pewno chcesz wycofać aplikację?');
                        break;
                    case 'Odrzucono':
                        props.setMessage('Przyjmujesz do wiadomości odrzucenie aplikacji. Aplikacja zostanie usunięta z systemu.');
                        break;
                    case 'Zaakceptowano':
                        props.setMessage('Przyjmujesz do wiadomości akceptację aplikacji. Aplikacja zostanie przeniesiona do zakłady "Kontrakty".');
                        break;
                    default:
                        props.setMessage('Nieznany status aplikacji');
                        break;
                }
                props.acknowledgeApplication();
            }
            
        }

    }

    console.log(props.application);

    return (
        <>
            {props.offer && (
                <Container className="main-container">
                    <OfferDetails
                        offer={props.offer}
                        viewOnly={true}
                    />
                    {props.application && (
                        <Container>
                            <Card className={`review-card ${getStatusClass(props.application.status)}`}>
                                <Card.Header>
                                    Status aplikacji: {props.application.status}
                                </Card.Header>
                                <Card.Body>
                                    <Row>
                                        <Col>
                                            <Application
                                                application={props.application}
                                            />
                                        </Col>
                                        <Col>
                                            <p>Odpowiedź pracodawcy: {props.application.response ? props.application.response : props.application.status === 'Zaakceptowano' || props.application.status === 'Odrzucono' ? 'Pracodawca nie dołączył wiadomości do odpowiedzi na aplikację.' : 'Brak odpowiedzi.'}</p>
                                            {props.application.status && (
                                                <Button
                                                    variant={props.application.status === 'Odrzucono' ? 'secondary' : 'primary'}
                                                    onClick={() => handleOnClick(props.application.status)}
                                                >
                                                    {props.application.status === 'Zaaplikowano' ? 'Wycofaj aplikację' : 'Potwierdź'}
                                                </Button>
                                            )}
                                        </Col>
                                    </Row>
                                </Card.Body>
                            </Card>
                        </Container>
                    )}
                </Container>
            )
            }
        </>
    );
};

export default ApplicationView;
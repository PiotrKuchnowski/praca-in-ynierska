import React, { useEffect, useState } from "react";
import { Container, Breadcrumb, Card, ListGroup, Spinner, FormText as Text, Button, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import { ApplicationsJobOffer } from "../../types";
import { getAllApplicationsForEmployer, respondToApplication } from "../../Api/Applications";
import { isAxiosError } from "axios";
import OfferDetails from "../../Components/Offers/OfferDetails";
import "../User/Styles/Login.scss";
import '../../Styles/Spinner.scss';
import './Styles/Interested.scss';
import Application from "../../Components/Applications/Application";
import { set } from "date-fns";
import InterestModal from "../../Components/InterestModal";

const Interested = () => {
  const [applications, setApplications] = useState<ApplicationsJobOffer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showRejectionModal, setShowRejectionModal] = useState<boolean>(false);
  const [showAcceptanceModal, setShowAcceptanceModal] = useState<boolean>(false);
  const [referenceId, setReferenceId] = useState<string>("");

  const respondToInterest = async (accepted : boolean, message : string) => {
    try {
      const responseData = await respondToApplication(referenceId, message, accepted);
      if (responseData.code === 200) {
        const newApplications = applications.filter((application) => application.jobOfferResponse.jobOfferReferenceId !== referenceId);
        setApplications(newApplications);
      } else {
        setError("Nie udało się odrzucić zainteresowania.");
      }
    } catch (error) {
      if (isAxiosError(error)) {
        setError(error.response?.data.message || "Błąd serwera.");
      } else {
        setError("Nieznany błąd.");
      }
    }
  }

  const handleRejection = (message : string) => {
    respondToInterest(false, message);
  }

  const handleAcceptance = (message : string) => {
    respondToInterest(true, message);
  }

  useEffect(() => {
    const getInterests = async () => {
      try {
        const response = await getAllApplicationsForEmployer();
        if (response.code === 200) {
          console.log(response.data.interests);
          setApplications(response.data.interests);
        } else {
          setError("Nie udało się pobrać listy zainteresowanych.");
        }
      } catch (error) {
        if (isAxiosError(error)) {
          setError(error.response?.data.message || "Błąd serwera.");
        } else {
          setError("Nieznany błąd.");
        }
      } finally {
        setLoading(false);
      }
    };
    getInterests();
  }, []);

  if (loading) {
    return (
      <Container className="loading-container">
        <div className="login-in-overlay">
          <Spinner animation="border" role="status" variant="primary" />
          <Text className="sr-only">Ładowanie listy zainteresowanych...</Text>
        </div>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="error-container">
        <p className="error-message">Błąd: {error}</p>
      </Container>
    );
  }

  return (
    <Container>
      <Breadcrumb>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
        <Breadcrumb.Item active>Zainteresowani kandydaci</Breadcrumb.Item>
      </Breadcrumb>

      {applications?.length === 0 ? (
        <p className="no-interests">Brak zainteresowanych kandydatów</p>
      ) : (
        applications?.map((applicationsJobOffer) => (
          <React.Fragment key={applicationsJobOffer.jobOfferResponse.jobOfferReferenceId}>
            {applicationsJobOffer?.applications?.length === 0 ? null : (
              <Card className="mb-3">
                <Card.Header>
                  Tytuł oferty: {applicationsJobOffer.jobOfferResponse.title}
                </Card.Header>
                <Card.Body>
                  <OfferDetails
                    offer={applicationsJobOffer.jobOfferResponse}
                    viewOnly={true}
                  />
                  <p>Zainteresowani kandydaci:</p>
                  <Container className="mb-3">
                    <ListGroup>
                      {applicationsJobOffer.applications.map((application) => (
                        <ListGroup.Item key={application.user.email}>
                          <Application
                            application={application}
                            handleRejection={() => {
                              console.log("rejection");
                              setReferenceId(application.applicationId); 
                              setShowRejectionModal(true);
                            }}
                            handleAcceptance={() => {
                              console.log("acceptance");
                              setReferenceId(application.applicationId); 
                              setShowAcceptanceModal(true);
                            }}
                          />
                        </ListGroup.Item>
                      ))}
                    </ListGroup>
                  </Container>
                </Card.Body>
              </Card>
            )}
          </React.Fragment>
        ))
      )}

      <InterestModal
        show={showRejectionModal}
        onHide={() => setShowRejectionModal(false)}
        onConfirm={(message) => handleRejection(message)}
        message="Czy na pewno chcesz odrzucić zainteresowanie?"
        messageTitle="Możesz zostawić wiadomość dla kandydata:"
        messageWarning="Ewentualna wiadomość zostanie przekazana kandydatowi."
        title="Odrzuć zainteresowanie"
      />

      <InterestModal
        show={showAcceptanceModal}
        onHide={() => setShowAcceptanceModal(false)}
        onConfirm={(message) => handleAcceptance(message)}
        message="Czy na pewno chcesz zaakceptować zainteresowanie?"
        messageTitle="Możesz zostawić wiadomość dla kandydata:"
        messageWarning="Ewentualna wiadomość zostanie przekazana kandydatowi."
        title="Zaakceptuj zainteresowanie"
      />
    </Container>
    
  );
};

export default Interested;

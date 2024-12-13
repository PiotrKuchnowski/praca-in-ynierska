import React, { useState } from "react";
import Container from "react-bootstrap/Container";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Alert from "react-bootstrap/Alert";
import './Styles/OfferDetails.scss';
import InterestModal from "../InterestModal";
import { payTypeShortName } from "../../Utils/PayTypeUtils";
import { format } from "date-fns";
import { OfferDetailsProps } from "../../types";
import { useNavigate, useLocation } from "react-router-dom";
import { useUserContext } from "../../AppContextProvider";
import { getCategoryIcon } from "../../Utils/OfferCardUtils";
import { apply } from "../../Api/Applications";
import { isAxiosError } from "axios";
import { deleteOffer } from "../../Api/Offers";
import ConfirmModal from "../ConfirmModal";

const OfferDetails: React.FC<OfferDetailsProps> = (props) => {
  const { isLogged } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();
  const [showInterestModal, setShowInterestModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const manageMode = location.state?.manageMode || false;
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const handleGoBack = () => {
    navigate(manageMode ? '/pracodawca/panel/zarzadzaj-ofertami' : '/oferty');
  };

  const handleShowInterest = () => {
    if (!isLogged) {
      navigate('/logowanie');
    }
    setShowInterestModal(true);
  };

  const deletion = async () => {
    try {
      const response = await deleteOffer(props.offer.jobOfferReferenceId);
      if (response.code === 200) {
        navigate('/pracodawca/panel/zarzadzaj-ofertami');
      } else {
        setErrorMessage('Nie udało się usunąć oferty.');
      }
    } catch (error) {
      setErrorMessage(isAxiosError(error) && error.response?.data?.exception
        ? `Wystąpił błąd: ${error.response.data.exception}`
        : `Wystąpił błąd: ${error}`);
    }
  };

  const handleDeletion = () => {
    deletion();
  }


  const handleConfirmInterest = async (message : string) => {
    try {
      await apply(props.offer.jobOfferReferenceId, message);
      setSuccessMessage('Zainteresowanie zostało wyrażone pomyślnie!');
      const timer = setTimeout(() => {
        setSuccessMessage('');
        clearTimeout(timer);
      }, 5000);
    } catch (error) {
      setErrorMessage(isAxiosError(error) && error.response?.data?.exception
        ? `Wystąpił błąd: ${error.response.data.exception}`
        : `Wystąpił błąd: ${error}`);
      const timer = setTimeout(() => {
        setErrorMessage('');
        clearTimeout(timer);
      }, 5000);
    } finally {
      setShowInterestModal(false);
    }
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return 'Brak';
    const date = new Date(dateString);
    return isNaN(date.getTime()) ? 'Brak' : format(date, 'dd.MM.yyyyy HH:mm');
  };
  const payType = payTypeShortName(props.offer.payType?.name || '');
  const locationText = Array.isArray(props.offer.locations)
    ? props.offer.locations.map((loc) => loc.remote ? 'Zdalna' : `${loc.postalCode || ''} ${loc.city || ''}, ${loc.street || ''} ${loc.houseNumber || ''}${loc.apartmentNumber ? "/" + loc.apartmentNumber : ''}`).join(" | ")
    : 'Zdalna';

  return (
    <Container className="offer-details">
      {successMessage && <Alert variant="success" className="text-center">{successMessage}</Alert>}
      {errorMessage && <Alert variant="danger" className="text-center">{errorMessage}</Alert>}
      <Card className="offer-details-card">
        <Row>
          <Col md={4} className="d-flex justify-content-center align-items-center">
            <div className="offer-details-icon">
              {getCategoryIcon(props.offer.jobCategory)}
            </div>
          </Col>
          <Col md={8}>
            <Card.Body>
              <Card.Title className="offer-details-title">{props.offer.title || 'N/A'}</Card.Title>
              <Card.Text className="offer-details-description">{props.offer.description || 'N/A'}</Card.Text>
              <Row>
                <Col xs={12} className="mb-2">
                  <i className="bi bi-currency-dollar offer-small-icon"></i>
                  <strong>Stawka:</strong> {props.offer.pay || 'N/A'} zł/{payType}
                </Col>
                <Col xs={12} className="mb-2">
                  <i className="bi bi-calendar offer-small-icon"></i>
                  <strong>Data:</strong> {formatDate(props.offer.startDate)} - {formatDate(props.offer.endDate)}
                </Col>
                <Col xs={12}>
                  <i className="bi bi-geo-alt offer-small-icon"></i>
                  <strong>Lokalizacja:</strong> {locationText}
                </Col>
              </Row>
              {!props.viewOnly && (
                <div className="mt-3">
                <Row className="g-2">
                  <Col xs={12} sm={6}>
                    <Button variant="secondary" className="w-100" onClick={handleGoBack}>Powrót do ofert</Button>
                  </Col>
                  <Col xs={12} sm={6}>
                  {manageMode ?
                    <Button variant="primary" className="w-100" onClick={() => navigate(`${location.pathname}/edytuj-oferte`, { state: { offer: props.offer } })}><i className="fas fa-pen-to-square"></i> Edytuj ofertę</Button>
                    :
                    <Button variant="primary" className="w-100" onClick={handleShowInterest}>Wyraź zainteresowanie</Button>
                  }
                  </Col>
                  <Col xs={12} sm={6}>
                  {manageMode && (
                    <Button variant="danger"className="w-100" onClick={() => setShowConfirmModal(true)}><i className="fas fa-trash-alt"></i> Usuń ofertę</Button>
                  )}
                  </Col>
                </Row>
                </div>
              )}
            </Card.Body>
          </Col>
        </Row>
      </Card>
      <InterestModal
        show={showInterestModal}
        onHide={() => setShowInterestModal(false)}
        onConfirm={(message) => handleConfirmInterest(message)}
      />

      <ConfirmModal
        show={showConfirmModal}
        onConfirm={handleDeletion}
        onHide={() => setShowConfirmModal(false)}
        message="Czy na pewno chcesz usunąć ofertę?"
        message2="Ta operacja jest nieodwracalna."
      />

    </Container>


  );
};

export default OfferDetails;

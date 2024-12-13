import React, { use, useEffect, useState } from "react";
import { OfferType, ApplicationType } from "../../types";
import { useLocation, useNavigate } from "react-router-dom";
import { getOfferById } from "../../Api/Offers";
import { Breadcrumb, Container } from "react-bootstrap";
import { getApplicationByJobOfferId, acknowledgeEmployment, deleteApplication } from "../../Api/Applications";
import "./Styles/Review.scss";
import ApplicationView from "../../Components/Applications/ApplicationView";
import ConfirmModal from "../../Components/ConfirmModal";

const ApplicationReview = () => {

  const [offer, setOffer] = useState<OfferType>();
  const [application, setApplication] = useState<ApplicationType>();
  const location = useLocation();
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const navigate = useNavigate();
  const [message, setMessage] = useState('');

  useEffect(() => {
    const jobOfferReferenceId = location.pathname.split('/')[5];
    const getOffer = async () => {
      try {
        const response = await getOfferById(jobOfferReferenceId);
        if (response.code === 200) {
          setOffer(response.data.offer);
        }
      } catch (error) {
        console.error('Error while getting offer:', error);
      }
    }

    const getApplication = async () => {
      try {
        const response = await getApplicationByJobOfferId(jobOfferReferenceId);
        if (response.code === 200) {
          setApplication(response.data.application);
        }
      } catch (error) {
        console.error('Error while getting application:', error);
      }
    }

    getOffer();
    getApplication();
  }, [location.pathname]);

  const acknowledgeApplication = async () => {
    debugger;
    if(application?.status === 'Zaakceptowano') {
      const response = await acknowledgeEmployment(application.applicationId);
      if(response.code === 200) {
        console.log('Employment acknowledged');
      } else {
        console.error('Error while acknowledging employment');
      }
    } else if(application?.status === 'Odrzucono' || application?.status === 'Zaaplikowano') {
      const response = await deleteApplication(application.applicationId);
      if(response.code === 200) {
        console.log('Application deleted');
      } else {
        console.error('Error while deleting application');
      }
    }
    navigate(-1);
  }



  return (
    <Container>
      <Breadcrumb>
        <Breadcrumb.Item href="/">Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik">Użytkownik</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik/panel">Panel</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik/panel/aplikacje">Aplikacje</Breadcrumb.Item>
        <Breadcrumb.Item active>Podgląd aplikacji</Breadcrumb.Item>
      </Breadcrumb>

      <ApplicationView
        application={application as ApplicationType}
        offer={offer as OfferType}
        acknowledgeApplication={() => setShowConfirmModal(true)}
        setMessage={setMessage}
      />

      <ConfirmModal
        title='Potwierdź'
        message={message}
        message2='Ta operacja jest nieodwracalna.'
        onConfirm={() => acknowledgeApplication()}
        show={showConfirmModal}
        onHide={() => setShowConfirmModal(false)}
      />
    </Container >
  );

};

export default ApplicationReview;

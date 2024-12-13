import React, { useEffect, useState } from "react";
import { Breadcrumb, Container } from "react-bootstrap";
import OfferDetails from "../../Components/Offers/OfferDetails";
import { ApplicationType, OfferType } from "../../types";
import { useLocation } from "react-router-dom";
import { getOfferById } from "../../Api/Offers";
import { getApplicationByJobOfferId } from "../../Api/Applications";
import ApplicationView from "../../Components/Applications/ApplicationView";

const EmploymentReview = () => {

  const [offer, setOffer] = useState<OfferType>();
  const [contract, setContract] = useState<ApplicationType>();
  const location = useLocation();

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
          setContract(response.data.application);
        }
      } catch (error) {
        console.error('Error while getting application:', error);
      }
    }

    getOffer();
    getApplication();
  }, [location.pathname]);

  return (
    <Container>
      <Breadcrumb>
        <Breadcrumb.Item href="/">Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik">Użytkownik</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik/panel">Panel</Breadcrumb.Item>
        <Breadcrumb.Item href="/uzytkownik/panel/kontrakty">Kontrakty</Breadcrumb.Item>
        <Breadcrumb.Item active>Podgląd kontraktu</Breadcrumb.Item>
      </Breadcrumb>

      <ApplicationView
        offer={offer as OfferType}
        application={contract as ApplicationType}
      />
    </Container>
  );
};

export default EmploymentReview;
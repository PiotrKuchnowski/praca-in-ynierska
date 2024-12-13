import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import OfferDetails from "../../Components/Offers/OfferDetails";
import "../Offers/Styles/Offer.scss";
import { getOfferById } from "../../Api/Offers";
import { OfferType } from "../../types";
import Container from "react-bootstrap/Container";
import Breadcrumb from "react-bootstrap/Breadcrumb";


const EmployerOffer = () => {
  console.log("Offer component called");
  const { offerId } = useParams<{ offerId: string }>();
  const [offer, setOffer] = useState<OfferType>();

  useEffect(() => {
    console.log("useEffect called");
    const retrieveOffer = async () => {
      try {
        if (offerId) {
          const response = await getOfferById(offerId);
          if (response.code === 200) {
            setOffer(response.data.offer);
          }
        }
      } catch (error) {
        console.error("Error retrieving offer:", error);
      }
    }
    retrieveOffer();
  }, [offerId]);

  return (
    <Container className="container-relative">
      <Breadcrumb>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel/zarzadzaj-ofertami" }}>Zarządzaj ofertami</Breadcrumb.Item>
        <Breadcrumb.Item active>Szczegóły oferty</Breadcrumb.Item>
      </Breadcrumb>
      {offer &&
        <OfferDetails
          offer={offer}
          viewOnly={false} 
      />
      }
    </Container>

  );
};

export default EmployerOffer;

import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Breadcrumb from 'react-bootstrap/Breadcrumb';
import Container from 'react-bootstrap/Container';
import { OfferType, HandleClickParams, Category, City } from "../../types";
import OffersPageContent from "../../Components/Offers/OffersPageContent";
import { getAllApplicationsForUser } from "../../Api/Applications";
import { Spinner } from "react-bootstrap";
import { getCategories, getCities } from "../../Api/Offers";

const AppliedOffers = () => {
  // const [offerCount, setOfferCount] = useState(255); // eslint-disable-line @typescript-eslint/no-unused-vars
  const [offers, setOffers] = useState<OfferType[]>([]);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState<Category[]>([]);
  const [cities, setCities] = useState<City[]>([]);

  const handleClick = (props: HandleClickParams) => {
    const encodedOfferName = encodeURIComponent(props.offerTitle);
    props.navigate(`/uzytkownik/panel/aplikacje/${encodedOfferName}/${props.jobOfferReferenceId}`, { state: { manageMode: props.manageMode } });
  };

  useEffect(() => {
    console.log('useEffect called');
    const retrieveOffers = async () => {
      try {
        const response = await getAllApplicationsForUser();
        console.log(response);
        if (response.code === 200) {
          console.log('Offers retrieved:', response.data);
          setOffers(response.data.offers);
        }
      } catch (error) {
        console.error('Error retrieving offers:', error);
      } finally {
        console.log('Offers retrieved:', offers);
        setLoading(false);
      }
    }
    const retrieveCategories = async () => {
      try {
        const response = await getCategories();
        console.log(response);
        if (response.code === 200) {
          console.log('Categories retrieved:', response.data);
          setCategories(response.data.categories);
        }
      } catch (error) {
        console.error('Error retrieving categories:', error);
      }
    }

    const retrieveLocations = async () => {
      try {
        const response = await getCities();
        console.log(response);
        if (response.code === 200) {
          console.log('Locations retrieved:', response.data);
          setCities(response.data.cities);
        }
      } catch (error) {
        console.error('Error retrieving locations:', error);
      }
    }
    retrieveLocations();
    retrieveCategories();
    retrieveOffers();
    // eslint-disable-next-line
  }, []);

  return (
    <Container className='page-container'>
      <Breadcrumb>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/uzytkownik" }}>Użytkownik</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/uzytkownik/panel" }}>Panel</Breadcrumb.Item>
        <Breadcrumb.Item active>Aplikacje</Breadcrumb.Item>
      </Breadcrumb>
      {!loading && offers ? (
        <OffersPageContent
          currentOffers={offers}
          loading={loading}
          manageMode={false}
          handleClick={handleClick}
          categories={categories}
          cities={cities}
        />
      ) : (
        <Container className='loading-container'>
          <div className='login-in-overlay'>
            <Spinner animation='border' role='status' variant='primary' />
            <span className='sr-only'>Ładowanie aplikacji...</span>
          </div>
        </Container>
      )}
    </Container>
  );
}

export default AppliedOffers;
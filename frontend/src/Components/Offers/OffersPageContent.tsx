import React, { useEffect, useState } from "react";
import Container from 'react-bootstrap/Container';
import Filters from "../../Components/Offers/Filters";
import OffersListView from "../../Components/Offers/OffersListView";
import { OffersPageContentProps } from "../../types";
import { payTypeFullName } from "../../Utils/PayTypeUtils";



const OffersPageContent = (props: OffersPageContentProps) => {
    const [currentPage, setCurrentPage] = useState(1);
    const [offersPerPage, setOffersPerPage] = useState(15); // Default to 15 offers per page
    const [selectedCategory, setSelectedCategory] = useState("");
    const [selectedPayType, setSelectedPayType] = useState(Number);
    const [selectedCity, setSelectedCity] = useState("");
    const [minPay, setMinPay] = useState(0);
    const [maxPay, setMaxPay] = useState(10000);
    const [remote, setRemote] = useState(false);
    const [currentOffers, setCurrentOffers] = useState(props.currentOffers);


    const handleOffersPerPageChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setOffersPerPage(Number(event.target.value));
        setCurrentPage(1);
    };

    const handlePageChange = (pageNumber: number) => {
        setCurrentPage(pageNumber);
    };


    useEffect(() => {
        const indexOfLastOffer = currentOffers?.length < offersPerPage ? currentOffers?.length : currentPage * offersPerPage;
        const indexOfFirstOffer = indexOfLastOffer - offersPerPage;
        setCurrentOffers(currentOffers?.slice(indexOfFirstOffer, indexOfLastOffer));
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage, offersPerPage]);

    useEffect(() => {
        const applyFilters = () => {
            setCurrentOffers(props.currentOffers?.filter(offer => {
                let categoryMatch = true;
                let locationMatch = true;
                let payMatch = true;
                let remoteMatch = true;
                let payTypeMatch = true;

                console.log('Offer:', offer);

                if (selectedCategory) {
                    categoryMatch = offer.jobCategory === selectedCategory;
                }

                if (selectedCity) {
                    locationMatch = offer.locations.some(location => location.city === selectedCity);
                }

                if (selectedPayType) {
                    payTypeMatch = offer.payType.payTypeID === selectedPayType;
                }

                if (minPay || maxPay) {
                    payMatch = offer.pay >= minPay && offer.pay <= maxPay;
                }

                if (remote) {
                    remoteMatch = offer.locations.some(location => location.remote === remote);
                }

                return categoryMatch && locationMatch && payMatch && remoteMatch && payTypeMatch;
            }));
        }
        applyFilters();

    }, [selectedCategory, selectedCity, minPay, maxPay, remote, selectedPayType , props.currentOffers]);

    const handleCategoryChange = (event: React.ChangeEvent<HTMLSelectElement>) =>
        setSelectedCategory(event.target.value);

    const handleLocationChange = (event: React.ChangeEvent<HTMLSelectElement>) =>
        setSelectedCity(event.target.value);

    const handlePayRangeChange = (min: number, max: number) => {
        setMinPay(min);
        setMaxPay(max);
    };

    const handlePayTypeChange = (id : number) =>
        setSelectedPayType(id);

    const handleRemoteToggle = (event: React.ChangeEvent<HTMLInputElement>) =>
        setRemote(event.target.checked);

    // Calculate total pages
    const totalPages = Math.ceil(currentOffers?.length / offersPerPage);

    return (
        <Container className='content-pane'>
            <Filters
                remote={remote}
                minPay={minPay}
                maxPay={maxPay}
                selectedCity={selectedCity}
                selectedCategory={selectedCategory}
                selectedPayType={selectedPayType}
                offersPerPage={offersPerPage}
                handleOffersPerPageChange={handleOffersPerPageChange}
                handleCategoryChange={handleCategoryChange}
                handleLocationChange={handleLocationChange}
                handlePayRangeChange={handlePayRangeChange}
                handleRemoteToggle={handleRemoteToggle}
                handlePayTypeChange={handlePayTypeChange}
                categories={props.categories}
                cities={props.cities}
            />
            <Container className='main-content'>
                <OffersListView
                    loading={props.loading}
                    currentOffers={currentOffers}
                    totalPages={totalPages}
                    currentPage={currentPage}
                    handlePageChange={handlePageChange}
                    manageMode={props.manageMode}
                    handleClick={props.handleClick}
                />
            </Container>
        </Container>
    );
}

export default OffersPageContent;
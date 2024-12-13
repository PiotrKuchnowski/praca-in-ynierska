import React from "react";
import Container from "react-bootstrap/Container";
import OfferList from "./OfferList";
import Spinner from "react-bootstrap/Spinner";
import PaginationBar from "./PaginationBar";
import Text from "react-bootstrap/FormText";
import { OffersListViewProps } from "../../types";


const OffersListView = (props: OffersListViewProps) => {
    return (
        <Container>
            {props.loading ? (
                <Container className='loading'>
                    <Spinner animation="border" role="status" variant="primary">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                    <Text className="sr-only">Ładowanie ofert...</Text>
                </Container>
            ) : (
                <>
                    <OfferList 
                        offers={props.currentOffers}
                         manageMode={props.manageMode}
                         handleClick={props.handleClick}
                    />

                    <PaginationBar
                        totalPages={props.totalPages}
                        currentPage={props.currentPage}
                        handlePageChange={props.handlePageChange}
                    />
                </>
            )}
        </Container>
    )
}

export default OffersListView;
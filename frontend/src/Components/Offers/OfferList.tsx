import React from 'react';
import OfferCard from './OfferCard';
import { Container, Row, Col } from 'react-bootstrap';
import { OfferListProps } from '../../types';

const OfferList: React.FC<OfferListProps> = ({ offers, manageMode, handleClick }) => {
  return (
      <Col>
        {offers.map((offer) => (
          <Row key={offer.jobOfferReferenceId} md={4}>
            {offer && (
              <OfferCard
                offer={offer}
                manageMode={manageMode}
                handleClick={handleClick}
              />
            )}

          </Row>
        ))}
      </Col>
  );
};

export default OfferList;
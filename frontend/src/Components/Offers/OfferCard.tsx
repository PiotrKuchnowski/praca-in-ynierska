import React, { useMemo } from 'react';
import Card from 'react-bootstrap/Card';
// import Button from 'react-bootstrap/Button';
// import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import './Styles/OfferCard.scss';
import { OfferCardProps, CustomLocation } from '../../types';
import { useNavigate } from 'react-router-dom';
// import { useUserContext } from '../../AppContextProvider';
// import { useUserContext } from '../../AppContextProvider';
import { getCategoryIcon } from '../../Utils/OfferCardUtils';
import { payTypeShortName } from '../../Utils/PayTypeUtils';
import { format } from 'date-fns';
import 'bootstrap-icons/font/bootstrap-icons.css';

const OfferCard: React.FC<OfferCardProps> = (props) => {
  // const [isLiked, setIsLiked] = useState(false);
  // const { isLogged } = useUserContext();
  // const [isLiked, setIsLiked] = useState(false);
  // const { isLogged } = useUserContext();
  const navigate = useNavigate();

  // const handleLikeClick = (event: React.MouseEvent<HTMLButtonElement>) => {
  //   event.stopPropagation();
  //   setIsLiked(!isLiked);
  // };
  // const handleLikeClick = (event: React.MouseEvent<HTMLButtonElement>) => {
  //   event.stopPropagation();
  //   setIsLiked(!isLiked);
  // };

  const handleCardClick = () => {
    const params = {
      offerTitle: props.offer.title,
      jobOfferReferenceId: props.offer.jobOfferReferenceId,
      manageMode: props.manageMode,
      navigate: (path: string, state: { state: { manageMode: boolean } }) => navigate(path, state)
    };
    console.log('handleCardClick', params);
    props.handleClick(params);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return format(date, 'dd.MM.yyyy');
  };

  const locationText = useMemo(() => {
    if (Array.isArray(props.offer.locations)) {
      return props.offer.locations.map((loc: CustomLocation, index: number) =>
        loc.remote ? '|Zdalna|' : `|${loc.postalCode || ''} ${loc.city || ''}, ${loc.street || ''} ${loc.houseNumber || ''}${loc.apartmentNumber ? '/' + loc.apartmentNumber + "|": '|'}`
      ).join("<br />");
    } else {
      return '';
    }
  }, [props.offer.locations]);

  return (
    <Card className="mb-3 offer-card" onClick={handleCardClick}>
      <Row className='w-100'>
        <Col xs={4} md={4} className="offer-card-img-col d-flex align-items-center justify-content-center">
          <div className='offer-card-img'>
            {getCategoryIcon(props.offer.jobCategory)}
            <p>{props.offer.jobCategory}</p>
          </div>
        </Col>
        <Col xs={8} md={8}>
          <Card.Body className="d-flex flex-column justify-content-between">
            <div>
              <Card.Title className="mb-1">{props.offer.title}</Card.Title>
              <Card.Text className="mb-1">
                {props.offer.description}
              </Card.Text>
            </div>
            <div className="mb-1">
              {/* {isLogged && !props.manageMode &&
              {/* {isLogged && !props.manageMode &&
                <Button
                  variant="primary"
                  size="sm"
                  onClick={handleLikeClick}
                  className='d-flex justify-content-center align-items-center'
                  style={{ fontSize: '0.7rem', padding: '0.2rem 0.5rem' }}
                  aria-label={isLiked ? `Usuń ofertę ${props.offer.title} z ulubionych` : `Dodaj ofertę ${props.offer.title} do ulubionych`}>
                  <i className={isLiked ? 'bi bi-heart-fill white-icon' : 'bi bi-heart white-icon'}></i>
                </Button>
              } */}
            
            </div>
            <div>
              <Row>
                <Col xs={12} md={6}>
                  <i className="bi bi-calendar offer-small-icon"></i> {formatDate(props.offer.startDate)} {props.offer.endDate ? ` - ${formatDate(props.offer.endDate)}` : ''}
                </Col>
                <Col xs={12} md={6}>
                  <div>
                    <i className="bi bi-geo-alt offer-small-icon"></i>
                    <span dangerouslySetInnerHTML={{ __html: locationText }} /><br />
                  </div>
                </Col>
                <Col xs={12} md={6}>
                  <i className="bi bi-currency-dollar offer-small-icon"></i> {props.offer.pay} zł/{payTypeShortName(props.offer.payType.name)}
                </Col>
              </Row>
            </div>
          </Card.Body>
        </Col>
      </Row>
    </Card>
  );
};

export default OfferCard;
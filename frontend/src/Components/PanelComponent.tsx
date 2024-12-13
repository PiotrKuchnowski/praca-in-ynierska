import React from "react";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Container from "react-bootstrap/Container";
import { PanelComponentProps } from "../types";
import PanelCard from "./Employer/PanelCard";
import { useNavigate } from "react-router-dom";

const PanelComponent = (props : PanelComponentProps) => {
    const navigate = useNavigate();

    return (
        <Container>
            <Row>
            { Array.from({ length: props.numberOfCards}).map((_, index) => (
                <Col key={index} md={6} className="mb-4">
                    <PanelCard
                        title={props.titles[index]}
                        text={props.texts[index]}
                        icon={props.icons ? props.icons[index] : undefined}
                        onClick={() => { navigate(props.links[index]) } 
                    }
                    />
                        
                </Col>
            ))}
            </Row>
        </Container>
    )
}

export default PanelComponent;
import React from "react";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import Container from "react-bootstrap/Container";
import { Link } from "react-router-dom";
import "./Styles/EmployerPanel.scss";
import PanelCard from "../../Components/Employer/PanelCard";
import { useNavigate } from "react-router-dom";
import PanelComponent from "../../Components/PanelComponent";

const EmployerPanel = () => {
    const navigate = useNavigate();

    return (
        <Container className="container-relative">
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
                <Breadcrumb.Item active>Panel</Breadcrumb.Item>
            </Breadcrumb>
            <PanelComponent 
                numberOfCards={4}
                titles={["Dodaj ofertę pracy", "Zarządzaj ofertami", "Zainteresowani kandydaci", "Profil"]}
                texts={["Dodaj nową ofertę pracy dla kandydatów", "Zarządzaj swoimi ofertami pracy", "Sprawdź kto jest zainteresowany Twoimi ofertami pracy", "Zmień dane swojego profilu pracodawcy"]}
                links={["/pracodawca/panel/dodaj-oferte", "/pracodawca/panel/zarzadzaj-ofertami", "/pracodawca/panel/zainteresowani-kandydaci", "/pracodawca/panel/profil"]}
                icons={["fas fa-plus", "fas fa-pen-to-square", "fas fa-users", "fas fa-user"]}
            />
        </Container>

    )
};

export default EmployerPanel;
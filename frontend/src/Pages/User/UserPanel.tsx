import React from "react";

import PanelComponent from "../../Components/PanelComponent";
import Container from "react-bootstrap/Container";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Link } from "react-router-dom";

const UserPanel = () => {
    return (
        <Container className="container-relative">
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/uzytkownik" }}>Użytkownik</Breadcrumb.Item>
                <Breadcrumb.Item active>Panel</Breadcrumb.Item>
            </Breadcrumb>
            <PanelComponent 
                numberOfCards={3}
                titles={[
                    "Profil",
                    "Aplikacje",
                    "Kontrakty"
                ]}
                texts={[
                    "Zarządzaj swoim profilem",
                    "Sprawdź status swoich aplikacji",
                    "Sprawdź status swoich kontraktów"
                ]}
                links={["/uzytkownik/panel/profil", "/uzytkownik/panel/aplikacje", "/uzytkownik/panel/kontrakty"]}
                icons={["fas fa-user", "fas fa-file-signature", "fas fa-file-contract"]}
            />
        </Container>
    )
};

export default UserPanel;
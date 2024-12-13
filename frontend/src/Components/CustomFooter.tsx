import React from "react";
import './Styles/CustomFooter.scss';
import { Container } from "react-bootstrap";

const CustomFooter = () => {
    return (
        <footer className="text-center bg-body-tertiary">
            <Container>
                <p className="text">&copy; 2024 Piotr Kuchnowski. Wszelkie prawa zastrzeżone.</p>
                {/* <p>
                    <a href="/kontakt">Kontakt</a> | <a href="/polityka-prywatnosci">Polityka prywatności</a>
                </p> */}
            </Container>
        </footer>
    );
}

export default CustomFooter;
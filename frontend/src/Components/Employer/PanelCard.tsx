import React from "react";
import Card from "react-bootstrap/Card";
import "../../types";
import { PanelCardProps } from "../../types";

const PanelCard = (props : PanelCardProps) => {
    const userIcon = "fa-user";
    return (
        <Card onClick={props.onClick} className="card-style">
            <Card.Body>
                <i className={`${props.icon} icon-style`}></i>
                <Card.Title>{props.title}</Card.Title>
                <Card.Text>
                    {props.text}
                </Card.Text>
            </Card.Body>
        </Card>
    )
}

export default PanelCard;
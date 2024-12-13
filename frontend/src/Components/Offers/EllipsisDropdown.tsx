import React from "react";
import Dropdown from "react-bootstrap/Dropdown";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import SquaredPaginationItem from "./SquaredPaginationItem";
import { EllipsisDropdownProps } from "../../types";

const EllipsisDropdown: React.FC<EllipsisDropdownProps> = ({ showInput, setShowInput, handleEllipsisClick, inputValue, handleInputChange, handleInputSubmit, totalPages }) => (
    <Dropdown show={showInput} onToggle={() => setShowInput(!showInput)}  >
        <Dropdown.Toggle as={SquaredPaginationItem} onClick={handleEllipsisClick} className="square-dropdown">
            ...
        </Dropdown.Toggle>
        <Dropdown.Menu>
            <div className="d-flex align-items-center p-2">
                <FormControl
                    type="number"
                    value={inputValue}
                    onChange={handleInputChange}
                    placeholder="Page"
                    min={1}
                    max={totalPages}
                    className="mr-2"
                />
                <Button onClick={handleInputSubmit}>Go</Button>
            </div>
        </Dropdown.Menu>
    </Dropdown>
);

export default EllipsisDropdown;
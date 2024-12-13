import React, { useState } from 'react';
import Pagination from 'react-bootstrap/Pagination';
import EllipsisDropdown from './EllipsisDropdown';
import { PaginationBarProps } from '../../types';

const PaginationBar: React.FC<PaginationBarProps> = ({ totalPages, currentPage, handlePageChange }) => {
    const [showStartInput, setShowStartInput] = useState(false);
    const [showEndInput, setShowEndInput] = useState(false);
    const [inputValue, setInputValue] = useState('');

    const handlePageChangeAndScroll = (page: number) => {
        handlePageChange(page);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleStartEllipsisClick = () => {
        setShowStartInput(!showStartInput);
        setShowEndInput(false);
    };

    const handleEndEllipsisClick = () => {
        setShowEndInput(!showEndInput);
        setShowStartInput(false);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(e.target.value);
    };

    const handleInputSubmit = () => {
        const pageNumber = parseInt(inputValue, 10);
        if (!isNaN(pageNumber) && pageNumber >= 1 && pageNumber <= totalPages) {
            handlePageChangeAndScroll(pageNumber);
        }
        setShowStartInput(false);
        setShowEndInput(false);
        setInputValue('');
    };

    const renderPageNumbers = () => {
        const pages = [];
        if (currentPage > 3) {
            pages.push(
                <EllipsisDropdown
                    key={0}
                    showInput={showStartInput}
                    setShowInput={setShowStartInput}
                    handleEllipsisClick={handleStartEllipsisClick}
                    inputValue={parseInt(inputValue, 10)}
                    handleInputChange={handleInputChange}
                    handleInputSubmit={handleInputSubmit}
                    totalPages={totalPages}
                />
            );
        }
        for (let pageNumber = Math.max(1, currentPage - 2); pageNumber <= Math.min(totalPages, currentPage + 2); pageNumber++) {
            pages.push(
                <Pagination.Item
                    key={pageNumber}
                    active={pageNumber === currentPage}
                    onClick={() => handlePageChangeAndScroll(pageNumber)}
                >
                    {pageNumber}
                </Pagination.Item>
            );
        }
        if (currentPage < totalPages - 2) {
            pages.push(
                <EllipsisDropdown
                    key={totalPages + 1}
                    showInput={showEndInput}
                    setShowInput={setShowEndInput}
                    handleEllipsisClick={handleEndEllipsisClick}
                    inputValue={parseInt(inputValue, 10)}
                    handleInputChange={handleInputChange}
                    handleInputSubmit={handleInputSubmit}
                    totalPages={totalPages}
                />
            );
        }
        return pages;
    };

    return (
        <div className="d-flex justify-content-center mt-3">
            <Pagination>
                <Pagination.First
                    onClick={() => handlePageChangeAndScroll(1)}
                    disabled={currentPage === 1}
                />
                <Pagination.Prev
                    onClick={() => handlePageChangeAndScroll(currentPage - 1)}
                    disabled={currentPage === 1}
                />
                {renderPageNumbers()}
                <Pagination.Next
                    onClick={() => handlePageChangeAndScroll(currentPage + 1)}
                    disabled={currentPage === totalPages}
                />
                <Pagination.Last
                    onClick={() => handlePageChangeAndScroll(totalPages)}
                    disabled={currentPage === totalPages}
                />
            </Pagination>
        </div>
    );
};

export default PaginationBar;
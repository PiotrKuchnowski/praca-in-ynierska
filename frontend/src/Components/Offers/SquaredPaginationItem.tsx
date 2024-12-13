import React from 'react';
import Pagination from 'react-bootstrap/Pagination';
import { SquaredPaginationItemProps } from '../../types';

const SquaredPaginationItem = React.forwardRef<HTMLLIElement, SquaredPaginationItemProps>(({ children, onClick, ...props }, ref) => (
    <Pagination.Item
        ref={ref}
        onClick={onClick}
        {...props}
        className="square-pagination-item"
    >
        {children}
    </Pagination.Item>
));

export default SquaredPaginationItem;
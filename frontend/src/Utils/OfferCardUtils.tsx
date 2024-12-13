import React from 'react';
import { ReactComponent as PawIcon } from '../Assets/paw.svg'; // Import the custom SVG icon

export const getCategoryIcon = (category: string) => {
  switch (category) {
    case 'Sprzedaż detaliczna':
      return <i className="bi bi-shop"></i>;
    case 'Gastronomia':
      return <i className="bi bi-cup-straw"></i>;
    case 'Dostawa':
      return <i className="bi bi-truck"></i>;
    case 'Obsługa klienta':
      return <i className="bi bi-headset"></i>;
    case 'Korepetycje':
      return <i className="bi bi-book"></i>;
    case 'Opieka nad dziećmi':
      return <i className="bi bi-person-badge"></i>;
    case 'Opieka nad zwierzętami':
      return <i className='fa-solid fa-paw'></i>
    case 'Sprzątanie':
      return <i className="bi bi-house"></i>;
    case 'Obsługa wydarzeń':
      return <i className="bi bi-calendar-event"></i>;
    case 'Magazyn':
      return <i className="bi bi-box"></i>;
    case 'Administracja':
      return <i className="bi bi-clipboard"></i>;
    case 'Marketing':
      return <i className="bi bi-megaphone"></i>;
    case 'Sprzedaż':
      return <i className="bi bi-cart"></i>;
    case 'Wsparcie techniczne':
      return <i className="bi bi-tools"></i>;
    case 'Pisanie na zlecenie':
      return <i className="bi bi-pencil"></i>;
    case 'Grafika':
      return <i className="bi bi-palette"></i>;
    case 'Fotografia':
      return <i className="bi bi-camera"></i>;
    case 'Instruktor fitness':
      return <i className="bi bi-bicycle"></i>;
    case 'Architektura krajobrazu':
      return <i className="bi bi-tree"></i>;
    case 'Budownictwo':
      return <i className="bi bi-hammer"></i>;
    default:
      return <i className="bi bi-question-circle"></i>; // Default icon for unknown categories
  }
};
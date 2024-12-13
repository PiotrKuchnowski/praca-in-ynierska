import React from "react";

export interface UserContextType {
    user: any;
    setUser: React.Dispatch<React.SetStateAction<any>>;
    isLogged: boolean;
    setIsLogged: React.Dispatch<React.SetStateAction<boolean>>;
    isEmployer: boolean;
    employer: any;
    setEmployer: React.Dispatch<React.SetStateAction<any>>;
    setIsEmployer: React.Dispatch<React.SetStateAction<boolean>>;
    resetContext: () => void;
    allertMessage: string;
    setAllertMessage: React.Dispatch<React.SetStateAction<string>>;
}

export interface User {
    id: number;
    userId: string;
    email: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    lastLogin: string;
    createdAt: string;
    updatedAt: string;
    role: string;
    authorities: string;
    accountNonExpired: boolean;
    accountNonLocked: boolean;
    isCredentialsNonExpired: boolean;
    enabled: boolean;
    birthDate: string;
}

export interface Employer {
    employerID: number;
    description: string;
    user: User;
}

export interface PayType {
    payTypeID: number;
    name: string;
}

export interface CustomLocation {
    locationReferenceID: string;
    remote: boolean;
    country: string;
    postalCode: string;
    city: string;
    street: string;
    houseNumber: string;
    apartmentNumber: string;
}

export interface OfferType {
    jobOfferReferenceId: string;
    employerID: Employer;
    startDate: string;
    endDate: string;
    description: string;
    title: string;
    pay: number;
    payType: PayType;
    locations: CustomLocation[];
    managed: boolean;
    jobCategory: string;
}


export interface OfferDetailsProps {
    offer: OfferType;
    viewOnly: boolean;
}

export interface PaginationBarProps {
    totalPages: number;
    currentPage: number;
    handlePageChange: (page: number) => void;
}

export interface EllipsisDropdownProps {
    showInput: boolean;
    setShowInput: (show: boolean) => void;
    handleEllipsisClick: () => void;
    inputValue: number;
    handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleInputSubmit: () => void;
    totalPages: number;
}

export interface OfferListProps {
    offers: OfferType[];
    manageMode: boolean;
    handleClick: (params: HandleClickParams) => void;
}

export interface SquaredPaginationItemProps {
    children: React.ReactNode;
    onClick: () => void;
}

export interface PanelCardProps {
    title: string;
    text: string;
    icon?: string;
    onClick?: () => void;
}

export interface OffersListViewProps {
    loading: boolean;
    currentOffers: OfferType[];
    totalPages: number;
    currentPage: number;
    handlePageChange: (page: number) => void;
    manageMode: boolean;
    handleClick: (params: HandleClickParams) => void;
}

export interface CreateLocation {
    remote: boolean;
    country: string;
    postalCode: string;
    city: string;
    street: string;
    houseNumber: string;
    apartmentNumber: string;
}

export interface CreateOffer {
    startDate: string;
    endDate: string;
    description: string;
    title: string;
    pay: string;
    payType: PayType;
    locations: CreateLocation[];
    jobCategory: string;
}

export interface LocationFormProps {
    index: number;
    location: CreateLocation;
    onLocationChange: (index: number, location: CreateLocation) => void;
}

export interface HandleClickParams {
    offerTitle: string;
    jobOfferReferenceId: string;
    manageMode: boolean;
    navigate: (path: string, state: { state: { manageMode: boolean } }) => void;
}

export interface OffersPageContentProps {
    loading: boolean;
    currentOffers: OfferType[];
    manageMode: boolean;
    handleClick: (params: HandleClickParams) => void;
    categories: Category[];
    cities: City[];
}

export interface AuthGuardProps {
    children: React.ReactNode;
}

export interface OfferCardProps {
    offer: OfferType;
    manageMode: boolean;
    handleClick: (params: HandleClickParams) => void;
}

export interface PanelComponentProps {
    numberOfCards: number;
    titles: string[];
    texts: string[];
    links: string[];
    icons?: string[];
}

export interface ApplicationsJobOffer {
    jobOfferResponse: OfferType;
    applications: ApplicationType[];
}

export interface FiltersProps {
    offersPerPage: number;
    selectedCategory: string;
    selectedCity: string;
    selectedPayType: number;
    minPay: number;
    maxPay: number;
    remote: boolean;
    categories: Category[];
    cities: City[];
    handleOffersPerPageChange: React.ChangeEventHandler<HTMLSelectElement>;
    handleCategoryChange: React.ChangeEventHandler<HTMLSelectElement>;
    handleLocationChange: React.ChangeEventHandler<HTMLSelectElement>;
    handlePayRangeChange: (min: number, max: number) => void;
    handleRemoteToggle: React.ChangeEventHandler<HTMLInputElement>;
    handlePayTypeChange: (id: number) => void;
}

export interface Category {
    name: string;
}

export interface ModalProps {
    show: boolean;
    onHide: () => void;
    onConfirm: (message : string) => void;
    message?: string;
    title?: string;
    messageTitle?: string;
    messageWarning?: string;
}

export interface ApplicationType {
    applicationId: string;
    message: string;
    response?: string;
    status: string;
    user: User;
}

export interface ApplicationProps {
    application: ApplicationType;
    handleAcceptance?: () => void;
    handleRejection?: () => void;
}

export interface ApplicationViewProps {
    application : ApplicationType;
    offer : OfferType;
    acknowledgeApplication? : () => void;
    setMessage? : React.Dispatch<React.SetStateAction<string>>;
}

export interface City {
    name: string;
}

export interface UpdateUserForm {
    email: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    birthDate: string;
}

export interface UpdateEmployerForm {
    companyName: string;
    nip : string;
    description : string;
}
    
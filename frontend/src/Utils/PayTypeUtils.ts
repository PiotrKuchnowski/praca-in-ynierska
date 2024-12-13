export const payTypeShortName = (payTypeName: string): string => {
    switch (payTypeName) {
        case 'Miesięczna':
            return 'miesiąc';
        case 'Godzinowa':
            return 'godzinę';
        case 'Dzienna':
            return 'dzień';
        case 'Od zlecenia':
            return 'zlecenie';
        default:
            return '';
    }
}

export const payTypeFullName = (payTypeName: string): string => {
    switch (payTypeName) {
        case 'miesiąc':
            return 'Miesięczna';
        case 'godzinę':
            return 'Godzinowa';
        case 'dzień':
            return 'Dzienna';
        case 'zlecenie':
            return 'Od zlecenia';
        default:
            return '';
    }
}

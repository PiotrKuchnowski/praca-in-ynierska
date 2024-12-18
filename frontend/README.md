
# Frontend

To jest frontend aplikacji, zbudowany w oparciu o React i TypeScript. Oferuje interfejs użytkownika, umożliwiający interakcję z usługami backendowymi.

## Spis Treści

- [Instalacja](#instalacja)
- [Uruchamianie](#uruchamianie)
- [Zmienne Środowiskowe](#zmienne-środowiskowe)
- [Docker](#docker)
- [Struktura Projektu](#struktura-projektu)
- [Główne Funkcje](#główne-funkcje)

## Instalacja

Aby zainstalować wszystkie zależności, użyj następującego polecenia:

```bash
npm install
```

## Uruchamianie

Aby uruchomić serwer deweloperski, użyj:

```bash
npm start
```

Aplikacja zostanie uruchomiona w trybie deweloperskim. Możesz otworzyć ją w przeglądarce pod adresem [http://localhost:3000](http://localhost:3000).

## Zmienne Środowiskowe

W projekcie wykorzystywane są następujące zmienne środowiskowe:
- **`REACT_APP_BASE_URL`**: Podstawa adresu URL aplikacji hostującej API.

Możesz ustawić te zmienne w pliku `.env` w katalogu głównym projektu.

## Docker

Aby zbudować i uruchomić aplikację za pomocą Dockera, wykonaj poniższe kroki:

1. Zbuduj obraz Dockera:
   ```bash
   docker build -t nazwa-obrazu .
   ```

2. Uruchom kontener:
   ```bash
   docker run -p 80:80 nazwa-obrazu
   ```

3. Otwórz aplikację w przeglądarce pod adresem [http://localhost:80](http://localhost:80).

## Struktura Projektu

- **`src/`**: Zawiera kod źródłowy aplikacji.
  - **`Api/`**: Funkcje obsługujące usługi API.
  - **`Auth/`**: Komponenty i strażnicy związani z autoryzacją.
  - **`Components/`**: Reużywalne komponenty interfejsu użytkownika.
  - **`Pages/`**: Główne strony aplikacji.
  - **`Styles/`**: Arkusze stylów SCSS.
- **`AppContextProvider.tsx`**: Dostarcza kontekst dla aplikacji.
- **`App.tsx`**: Główny komponent aplikacji.
- **`index.tsx`**: Punkt wejściowy aplikacji.

## Główne Funkcje

- **Autoryzacja Użytkowników**: Logowanie, rejestracja i weryfikacja konta.
- **Oferty Pracy**: Przeglądanie, podgląd i zarządzanie ofertami pracy.
- **Panel Pracodawcy**: Zarządzanie ofertami pracy, przegląd zainteresowanych kandydatów i zarządzanie profilem.
- **Panel Użytkownika**: Przegląd zaaplikowanych ofert, umowy o pracę i zarządzanie profilem.
- **Responsywny Design**: Aplikacja została zaprojektowana z myślą o responsywności i przyjazności dla użytkownika.

Aby uzyskać więcej informacji o funkcjach aplikacji, zajrzyj do głównego pliku README w katalogu głównym projektu.

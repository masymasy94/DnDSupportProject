# Frontend Tasks

Elenco delle modifiche e fix da fare sul frontend (repo DnD-platform-FE, branch release).

---

## Bug Fix

### 1. compendium-feats.html chiama getSkills() invece di getFeats()
- La pagina mostra le skills al posto dei feats
- Aggiungere `FEATS: '/api/compendium/feats'` in `API_CONFIG.COMPENDIUM` (api.js)
- Creare funzione `getFeats()` in api.js (stessa struttura di `getSkills()`)
- In `compendium-feats.html` sostituire la chiamata `getSkills()` con `getFeats()`

### 2. deleteCharacter() chiama un endpoint che non esiste ancora
- `deleteCharacter()` in api.js chiama `DELETE /characters/{id}` ma il backend non ha ancora questo endpoint
- **Non toccare il frontend** — il backend verra' aggiornato per aggiungere l'endpoint
- Verificare che la funzione funzioni correttamente una volta che il backend e' pronto

### 3. documents.html usa IndexedDB locale invece dell'asset-service
- I documenti caricati restano solo nel browser (IndexedDB), si perdono cambiando device/browser
- Collegare la pagina all'asset-service (porta 8085):
  - Aggiungere `ASSET_PORT: 8085` in `env.js`
  - Aggiungere sezione `ASSET` in `API_CONFIG` con gli endpoint:
    - `UPLOAD: '/api/assets/documents'` (POST, multipart)
    - `UPLOAD_BATCH: '/api/assets/documents/batch'` (POST, multipart)
    - `LIST: '/api/assets/documents'` (GET)
    - `DOWNLOAD: '/api/assets/documents/{documentId}'` (GET)
  - Creare funzioni API: `uploadDocument()`, `uploadDocumentsBatch()`, `listDocuments()`, `downloadDocument()`
  - Sostituire le operazioni IndexedDB con chiamate all'asset-service
  - Mantenere IndexedDB come cache locale (opzionale) ma la source of truth deve essere il backend

---

## Integrazioni Mancanti

### 4. Pagina completamento password reset
- Il frontend invia la richiesta di reset (`POST /auth/password-resets`) ma non ha la pagina per completarlo
- Creare una pagina `reset-password.html` (raggiungibile dal link nella email)
- Deve chiamare `PUT /auth/password-resets` con body: `{ token, newPassword }`
- Aggiungere funzione `resetPassword(token, newPassword)` in api.js
- L'endpoint corretto e' `/auth/password-resets` (stessa path della request, metodo PUT)

### 5. OTP Login
- Il backend supporta login tramite codice OTP via email
- Aggiungere opzione "Login con codice email" nella pagina login (index.html)
- Step 1: `POST /auth/otp-login-requests` con body `{ email }` — invia codice via email
- Step 2: `POST /auth/otp-login-tokens` con body `{ email, otpCode }` — restituisce JWT tokens
- Aggiungere le due funzioni API in api.js

### 6. Pagina profilo utente
- Non esiste una pagina profilo/settings
- Creare `profile.html` con:
  - Visualizzazione dati utente (`GET /users/{id}` su porta 8089, richiede Bearer token)
  - Modifica password (`PUT /users/{id}/password` su porta 8089)
- Aggiungere `USER_PORT: 8089` in `env.js` (gia' presente come `REGISTER_PORT`, rinominare o aggiungere alias)
- Aggiungere sezione `USER` in `API_CONFIG`:
  - `FIND_BY_ID: '/users/{id}'` (GET)
  - `UPDATE_PASSWORD: '/users/{id}/password'` (PUT)
  - `SEARCH: '/users/search'` (GET, query param: `query`, `page`, `size`)
- Aggiungere link al profilo nel sidebar

### 7. Ricerca utenti
- `GET /users/search?query=gandalf&page=0&size=20` esiste nel backend ma non e' usato
- Utile per: aggiungere membri alle campagne (attualmente si inserisce solo userId numerico)
- Integrare nella UI "Aggiungi membro" di campaigns.html: search autocomplete per username/email

### 8. Notification center
- Aggiungere `NOTIFICATION_PORT: 8088` in `env.js`
- Per ora il notification-service gestisce solo email server-side (non ha un inbox per l'utente)
- **Fase 1**: aggiungere icona notifiche nel header/sidebar (placeholder)
- **Fase 2**: integrare quando il backend avra' un endpoint per le notifiche in-app

### 9. documents.html non visibile nella navigazione
- Verificare che il link a `documents.html` sia presente nel sidebar di TUTTE le pagine autenticate
- Attualmente presente nel sidebar di dashboard.html ma controllare: characters.html, campaigns.html, compendium.html

---

## Miglioramenti Architetturali

### 10. Caching dati compendium
- Ogni visita a una pagina compendium rifa' fetch di dati che cambiano raramente
- Implementare cache in `sessionStorage` con TTL (es. 1 ora) per:
  - Classes, species, alignments, backgrounds, skills, conditions, damage types, languages,
    armor types, weapon types, tool types, proficiency types, spell schools
- Le pagine paginate (spells, monsters, equipment, magic items) possono cachare la prima pagina

### 11. Separazione api.js
- api.js ha 1134 righe — dividere in moduli:
  - `api-core.js` — apiRequest, authenticatedRequest, refreshAccessToken
  - `api-auth.js` — login, register, logout, password reset, OTP
  - `api-character.js` — CRUD characters, PDF import/export
  - `api-campaign.js` — CRUD campaigns, members, notes
  - `api-compendium.js` — tutte le funzioni compendium
  - `api-chat.js` — (se necessario, la maggior parte e' in chat.js)
  - `api-document-qa.js` — ask, conversations
  - `api-asset.js` — upload, list, download documents

### 12. Route guard consistente
- Ogni pagina autenticata ha `if (!token) { window.location.href = 'index.html'; }` inline
- Centralizzare in `utils.js` con una funzione `requireAuth()` che controlla anche token expiry

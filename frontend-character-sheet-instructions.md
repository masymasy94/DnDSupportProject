# Frontend: PDF Character Sheet Import

Instructions for implementing the PDF character sheet import UI that works with the backend `POST /characters/import-sheet` endpoint.

---

## Overview

Users fill out the **official WotC D&D 5e fillable character sheet PDF** directly in the browser using PDF.js, then submit it to create a character.

---

## PDF.js Setup

Use [PDF.js 4.x](https://mozilla.github.io/pdf.js/) with annotation (form) support enabled.

```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.x.x/pdf.min.mjs" type="module"></script>
```

Key configuration:
- Enable annotation rendering: `annotationMode: pdfjsLib.AnnotationMode.ENABLE_FORMS`
- This renders fillable form fields (text inputs, checkboxes) as interactive HTML elements on top of the PDF canvas

---

## Page: `character-sheet.html`

### Layout
1. **PDF Viewer** - Renders the character sheet PDF with fillable form fields
2. **Submit Button** - Sends the filled PDF to the backend

### Flow
1. Load the blank WotC 5e character sheet PDF into the PDF.js viewer
2. User fills out form fields (name, class, level, ability scores, etc.)
3. User clicks "Import Character"
4. Call `pdfDoc.saveDocument()` to get the filled PDF as bytes (Uint8Array)
5. Create a `FormData` with the PDF blob
6. `POST` to `/characters/import-sheet`
7. Display success/error message

### Example JavaScript

```javascript
async function importCharacterSheet() {
    // Get the filled PDF bytes from PDF.js
    const pdfBytes = await pdfDoc.saveDocument();
    const blob = new Blob([pdfBytes], { type: 'application/pdf' });

    const formData = new FormData();
    formData.append('file', blob, 'character-sheet.pdf');

    const response = await fetch('/characters/import-sheet', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${authToken}`
        },
        body: formData
    });

    if (response.ok) {
        const character = await response.json();
        // character is a CharacterViewModel - redirect to character page
        window.location.href = `/characters/${character.id}`;
    } else {
        const error = await response.text();
        showError(error);
    }
}
```

---

## API Contract

### Endpoint
```
POST /characters/import-sheet
```

### Request
- **Content-Type:** `multipart/form-data`
- **Authorization:** `Bearer <JWT token>` (user must have `PLAYER` role)
- **Body:** Form field `file` containing the PDF

### Success Response (200)
Returns a `CharacterViewModel` JSON object:

```json
{
    "id": 1,
    "name": "Thorin Ironforge",
    "species": "Dwarf",
    "characterClass": "Fighter",
    "level": 5,
    "background": "Soldier",
    "alignment": "Lawful Good",
    "abilityScores": {
        "strength": 16,
        "dexterity": 12,
        "constitution": 14,
        "intelligence": 10,
        "wisdom": 13,
        "charisma": 8
    },
    "hitPointsCurrent": 44,
    "hitPointsMax": 44,
    "armorClass": 11,
    "proficiencyBonus": 3,
    "skills": [...],
    "savingThrows": [...],
    "equipment": [...],
    "spells": [...],
    "physicalCharacteristics": {
        "age": "150",
        "height": "4'6\"",
        "weight": "180 lbs",
        "eyes": "Brown",
        "skin": "Tan",
        "hair": "Black"
    },
    "personalityTraits": "...",
    "ideals": "...",
    "bonds": "...",
    "flaws": "...",
    "createdAt": "2025-01-15T10:30:00"
}
```

### Error Responses

| Code | Cause |
|------|-------|
| 400  | No file uploaded, non-PDF file, empty file, PDF has no AcroForm, or missing required character fields |
| 401  | Missing or invalid JWT token |
| 403  | User does not have PLAYER role |
| 404  | Compendium validation failed (invalid species, class, background, or alignment name) |

Error body is plain text describing the issue.

---

## Auth Headers

The JWT token is obtained from the auth-service login endpoint. Include it in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

The token must contain the `PLAYER` role in its groups/roles claim.

---

## Important Notes

- **Max file size:** 10 MB
- **Accepted format:** PDF only (`application/pdf`)
- The backend extracts data from PDF form fields, so the PDF **must** be the fillable WotC 5e character sheet (not a scanned/image-based PDF)
- The original PDF is stored server-side alongside the character record
- The `file` form field name must be exactly `file`
- Do **not** set `Content-Type` header manually when using `FormData` - the browser will set it with the correct boundary

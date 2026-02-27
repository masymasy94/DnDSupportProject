# Asset Service

[![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.31-blue?style=flat-square&logo=quarkus)](https://quarkus.io/)
[![Port](https://img.shields.io/badge/Port-8085-green?style=flat-square)]()

The **Asset Service** provides file upload, download, and listing capabilities for the DnD Platform. It stores documents in **MinIO** (S3-compatible object storage) and exposes REST APIs for single and batch file uploads. All endpoints run on **virtual threads** (`@RunOnVirtualThread`) for scalable I/O handling.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Operative Flows](#operative-flows)
  - [Upload Single Document](#1-upload-single-document)
  - [Upload Batch Documents](#2-upload-batch-documents)
  - [List All Documents](#3-list-all-documents)
  - [Download Document](#4-download-document)
- [Design Patterns](#design-patterns)
- [MinIO Storage Layout](#minio-storage-layout)
- [Configuration](#configuration)

---

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────────────────┐
│                       Asset Service (:8085)                               │
│                                                                          │
│  ┌─────────────────┐    ┌───────────────────┐    ┌───────────────────┐  │
│  │ Adapter-Inbound │    │      Domain        │    │ Adapter-Outbound  │  │
│  │                 │    │                    │    │                   │  │
│  │ ResourceImpl    │    │ Upload Service     │    │ MinIO Upload Repo │  │
│  │ @RunOnVThread   │───►│ Download Service   │───►│ MinIO Download    │  │
│  │ Delegate        │    │ List Service       │    │ MinIO List Repo   │  │
│  └─────────────────┘    │ Batch Service      │    │                   │  │
│                          └───────────────────┘    └────────┬──────────┘  │
│                                                             │             │
└─────────────────────────────────────────────────────────────┼─────────────┘
                                                               │
                                                        ┌─────────────┐
                                                        │    MinIO     │
                                                        │(S3-compat.) │
                                                        │   :9000     │
                                                        └─────────────┘
```

---

## Module Structure

```
asset-service/
├── asset-service-domain/              # Business logic & domain models
│   ├── model/                         # Records: Document, DocumentListItem, DocumentContent
│   ├── DocumentUploadService          # Single file upload
│   ├── DocumentUploadBatchService     # Multi-file upload
│   ├── DocumentListService            # List all stored documents
│   └── DocumentDownloadService        # Download by ID
│
├── asset-service-view-model/          # DTOs & Resource interfaces
│   └── vm/                            # DocumentViewModel, DocumentListItemViewModel
│
├── asset-service-adapter-inbound/     # REST controllers (Driving Adapters)
│   └── documents/                     # Upload, batch upload, list, download
│
├── asset-service-adapter-outbound/    # Infrastructure (Driven Adapters)
│   └── minio/                         # MinIO client config + repository implementations
│
├── asset-service-client/              # REST client interface
└── asset-service/                     # Quarkus bootstrap & configuration
```

---

## API Endpoints

| Method | Path | Auth | Content-Type | Description |
|--------|------|------|-------------|-------------|
| `POST` | `/api/assets/documents` | Bearer (`PLAYER`) | `multipart/form-data` | Upload single document |
| `POST` | `/api/assets/documents/batch` | Bearer (`PLAYER`) | `multipart/form-data` | Upload multiple documents |
| `GET` | `/api/assets/documents` | Bearer (`PLAYER`) | `application/json` | List all documents |
| `GET` | `/api/assets/documents/{documentId}` | Bearer (`PLAYER`) | `application/octet-stream` | Download a document |

> Max upload size: **50 MB** (`quarkus.http.limits.max-body-size`)

---

## Operative Flows

### 1. Upload Single Document

**`POST /api/assets/documents`** — Uploads a file to MinIO and returns metadata.

```
Client                     Asset Service                              MinIO
  │                             │                                        │
  │  POST /api/assets/documents │                                        │
  │  Content-Type:              │                                        │
  │    multipart/form-data      │                                        │
  │  file: <binary>             │                                        │
  │  userId: "1"                │                                        │
  │────────────────────────────►│                                        │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ DocumentUploadDelegate           │   │
  │                             │  │  @RunOnVirtualThread             │   │
  │                             │  │                                  │   │
  │                             │  │  1. Read file from disk          │   │
  │                             │  │     Files.newInputStream()       │   │
  │                             │  │                                  │   │
  │                             │  │  2. Call service.upload()        │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ MinioDocumentUploadRepository    │   │
  │                             │  │                                  │   │
  │                             │  │  1. Generate UUID (documentId)   │   │
  │                             │  │  2. objectName = "{uuid}/{file}" │   │
  │                             │  │  3. PutObjectArgs:               │   │
  │                             │  │     bucket, object, stream,      │   │
  │                             │  │     contentType, size            │   │
  │                             │  │  4. minioClient.putObject()      │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  PUT /{bucket}/{uuid}/{filename}       │
  │                             │───────────────────────────────────────►│
  │                             │  200 OK                                │
  │                             │◄───────────────────────────────────────│
  │                             │                                        │
  │                             │  Build Document record:                │
  │                             │   {id, fileName, contentType,          │
  │                             │    size, uploadedBy, uploadedAt}       │
  │                             │                                        │
  │  200 OK                     │                                        │
  │  {id: "550e8400-...",       │                                        │
  │   fileName: "map.png",     │                                        │
  │   contentType: "image/png", │                                        │
  │   size: 245760,            │                                        │
  │   uploadedBy: "1",         │                                        │
  │   uploadedAt: "..."}       │                                        │
  │◄────────────────────────────│                                        │
```

---

### 2. Upload Batch Documents

**`POST /api/assets/documents/batch`** — Uploads multiple files sequentially, returning metadata for each.

```
Client                     Asset Service                              MinIO
  │                             │                                        │
  │  POST /.../documents/batch  │                                        │
  │  files: [file1, file2, ...] │                                        │
  │  userId: "1"                │                                        │
  │────────────────────────────►│                                        │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ DocumentUploadBatchDelegate      │   │
  │                             │  │                                  │   │
  │                             │  │  For each file:                  │   │
  │                             │  │   1. Read from disk              │   │
  │                             │  │   2. Wrap in DocumentContent     │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ DocumentUploadBatchServiceImpl   │   │
  │                             │  │                                  │   │
  │                             │  │  For each DocumentContent:       │   │
  │                             │  │   └► DocumentUploadRepository    │   │
  │                             │  │       .upload()  (reuses single  │   │
  │                             │  │        upload logic)             │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  PUT file1 → MinIO                     │
  │                             │───────────────────────────────────────►│
  │                             │  PUT file2 → MinIO                     │
  │                             │───────────────────────────────────────►│
  │                             │  ...                                   │
  │                             │                                        │
  │  200 OK                     │                                        │
  │  [{id, fileName, ...},      │                                        │
  │   {id, fileName, ...}]      │                                        │
  │◄────────────────────────────│                                        │
```

The batch service **reuses the single upload repository** for each file, executing uploads sequentially.

---

### 3. List All Documents

**`GET /api/assets/documents`** — Lists all stored documents by traversing the MinIO bucket.

```
Client                     Asset Service                       MinIO
  │                             │                                 │
  │  GET /api/assets/documents  │                                 │
  │────────────────────────────►│                                 │
  │                             │                                 │
  │                             │  ┌────────────────────────────┐ │
  │                             │  │ MinioDocumentListRepository│ │
  │                             │  │                            │ │
  │                             │  │ ListObjectsArgs:           │ │
  │                             │  │   bucket, recursive=true   │ │
  │                             │  │                            │ │
  │                             │  │ For each object:           │ │
  │                             │  │   Parse objectName:        │ │
  │                             │  │   "{uuid}/{filename}"      │ │
  │                             │  │   → documentId = uuid      │ │
  │                             │  │   → fileName = filename    │ │
  │                             │  └────────────────────────────┘ │
  │                             │                                 │
  │                             │  listObjects(bucket, recursive) │
  │                             │────────────────────────────────►│
  │                             │  Iterator<Result<Item>>         │
  │                             │◄────────────────────────────────│
  │                             │                                 │
  │  200 OK                     │                                 │
  │  [{id: "uuid1",             │                                 │
  │    fileName: "map.png"},    │                                 │
  │   {id: "uuid2",             │                                 │
  │    fileName: "token.jpg"}]  │                                 │
  │◄────────────────────────────│                                 │
```

---

### 4. Download Document

**`GET /api/assets/documents/{documentId}`** — Downloads a file by its UUID.

```
Client                     Asset Service                              MinIO
  │                             │                                        │
  │  GET /documents/{uuid}      │                                        │
  │────────────────────────────►│                                        │
  │                             │                                        │
  │                             │  ┌──────────────────────────────────┐   │
  │                             │  │ MinioDocumentDownloadRepository  │   │
  │                             │  │                                  │   │
  │                             │  │ 1. listObjects(prefix="{uuid}/")│   │
  │                             │  │    → find the first object      │   │
  │                             │  │                                  │   │
  │                             │  │ 2. statObject(objectName)       │   │
  │                             │  │    → get contentType + size     │   │
  │                             │  │                                  │   │
  │                             │  │ 3. getObject(objectName)        │   │
  │                             │  │    → get InputStream            │   │
  │                             │  │                                  │   │
  │                             │  │ 4. Return DocumentContent       │   │
  │                             │  │    {fileName, contentType,      │   │
  │                             │  │     size, inputStream}          │   │
  │                             │  └──────────────────────────────────┘   │
  │                             │                                        │
  │                             │  stat + getObject                      │
  │                             │───────────────────────────────────────►│
  │                             │  metadata + stream                     │
  │                             │◄───────────────────────────────────────│
  │                             │                                        │
  │  200 OK                     │                                        │
  │  Content-Type: image/png    │                                        │
  │  Content-Disposition:       │                                        │
  │    attachment; filename=... │                                        │
  │  Content-Length: 245760     │                                        │
  │  <binary stream>            │                                        │
  │◄────────────────────────────│                                        │
  │                             │                                        │
  │  (or 404 Not Found)         │                                        │
```

The download uses a two-step lookup: first list by prefix to find the object name, then stat + stream.

---

## Design Patterns

### Hexagonal Architecture (Ports & Adapters)

```
                     ┌───────────────────────────────────┐
    Driving Ports    │         DOMAIN CORE                │   Driven Ports
  ───────────────────│                                     │──────────────────
                     │  DocumentUploadService              │
  ResourceImpl ─────►│  DocumentUploadBatchService         │────► DocumentUploadRepository (MinIO)
  @RunOnVThread      │  DocumentListService                │────► DocumentListRepository (MinIO)
  Delegate           │  DocumentDownloadService            │────► DocumentDownloadRepository (MinIO)
                     │                                     │
                     │  Records: Document,                 │
                     │    DocumentListItem,                │
                     │    DocumentContent                  │
                     └───────────────────────────────────┘
```

### Delegate Pattern

ResourceImpl handles JAX-RS annotations and `@RunOnVirtualThread`; the `@Delegate` handles file reading and service orchestration.

### Thin Service Layer

Domain services are pass-through to repositories — all storage logic lives in the MinIO adapter. This is intentional: the service has no complex business rules, so the domain layer acts as a stable interface boundary without artificial complexity.

### Virtual Thread Pattern

All REST endpoints use `@RunOnVirtualThread` for scalable I/O. File uploads and downloads involve blocking I/O (MinIO SDK), and virtual threads prevent platform thread exhaustion under concurrent load.

---

## MinIO Storage Layout

Documents are stored with a **UUID-prefixed folder structure**:

```
bucket/
├── 550e8400-e29b-41d4-a716-446655440000/
│   └── map-of-barovia.png
├── 6ba7b810-9dad-11d1-80b4-00c04fd430c8/
│   └── character-token.jpg
└── 7c9e6679-7425-40de-944b-e07fc1f90ae7/
    └── session-notes.pdf
```

- **Object name**: `{documentId}/{originalFileName}`
- **Document ID**: UUID generated at upload time
- **Lookup**: List objects by prefix `{documentId}/` to find the file

---

## Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `minio.endpoint` | MinIO S3 endpoint URL | `http://localhost:9000` |
| `minio.access-key` | Access key (from Vault) | — |
| `minio.secret-key` | Secret key (from Vault) | — |
| `minio.bucket` | Bucket name | `${MINIO_BUCKET}` |
| `quarkus.http.limits.max-body-size` | Max upload size | `50M` |

The `MinioConfig` class creates the bucket on startup if it doesn't exist, ensuring the service is ready immediately after deployment.

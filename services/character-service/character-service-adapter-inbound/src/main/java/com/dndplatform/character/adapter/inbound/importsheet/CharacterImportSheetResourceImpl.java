package com.dndplatform.character.adapter.inbound.importsheet;

import com.dndplatform.character.view.model.CharacterImportSheetResource;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/characters")
@Tag(name = "Character", description = "Character management operations")
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class CharacterImportSheetResourceImpl implements CharacterImportSheetResource {

    private final CharacterImportSheetDelegate delegate;
    private final JsonWebToken jwt;

    @Inject
    public CharacterImportSheetResourceImpl(@Delegate CharacterImportSheetResource delegate, JsonWebToken jwt) {
        this.delegate = (CharacterImportSheetDelegate) delegate;
        this.jwt = jwt;
    }

    @POST
    @Path("/import-sheet")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Import character from PDF sheet",
               description = "Import a D&D 5E character by uploading a filled WotC character sheet PDF")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Character imported successfully",
                    content = @Content(schema = @Schema(implementation = CharacterViewModel.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid PDF - not a valid fillable character sheet"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token"
            )
    })
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public CharacterViewModel importSheet(@RestForm("file") FileUpload file) {
        if (file == null) {
            throw new BadRequestException("No file uploaded. Please provide a PDF file.");
        }

        String contentType = file.contentType();
        if (contentType != null && !contentType.equals("application/pdf")) {
            throw new BadRequestException("Invalid file type: %s. Only PDF files are accepted.".formatted(contentType));
        }

        byte[] pdfBytes;
        try {
            pdfBytes = Files.readAllBytes(file.uploadedFile());
        } catch (IOException e) {
            throw new BadRequestException("Failed to read uploaded file: " + e.getMessage());
        }

        if (pdfBytes.length == 0) {
            throw new BadRequestException("Uploaded file is empty");
        }

        Long userId = Long.parseLong(jwt.getSubject());
        String fileName = file.fileName() != null ? file.fileName() : "character-sheet.pdf";

        return delegate.importSheetWithUserId(pdfBytes, fileName, "application/pdf", userId);
    }

    @Override
    public CharacterViewModel importSheet(InputStream pdfData, String fileName) {
        throw new UnsupportedOperationException("Use the multipart form endpoint instead");
    }
}

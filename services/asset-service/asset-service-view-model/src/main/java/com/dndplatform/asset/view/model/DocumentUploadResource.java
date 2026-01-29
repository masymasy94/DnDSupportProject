package com.dndplatform.asset.view.model;

import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public interface DocumentUploadResource {

    DocumentViewModel upload(FileUpload file, String uploadedBy);

    List<DocumentViewModel> uploadMultiple(List<FileUpload> files, String uploadedBy);
}

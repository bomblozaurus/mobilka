package com.teamE.fileUpload.controller;

import com.teamE.ads.data.AdsRepo;
import com.teamE.events.data.EventsRepo;
import com.teamE.events.data.entity.Event;
import com.teamE.fileUpload.exception.MyFileNotFoundException;
import com.teamE.fileUpload.service.FileStorageService;
import com.teamE.imageDestinations.ImageDestination;
import com.teamE.imageDestinations.ImageDestinationRepo;
import com.teamE.rooms.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private FileStorageService fileStorageService;
    private AdsRepo adsRepo;
    private EventsRepo eventsRepo;
    private RoomRepository roomRepository;
    private  ImageDestinationRepo imageDestinationRepo;

    @Autowired
    public FileController(FileStorageService fileStorageService, AdsRepo adsRepo, EventsRepo eventsRepo, RoomRepository roomRepository, ImageDestinationRepo imageDestinationRepo) {
        this.fileStorageService = fileStorageService;
        this.adsRepo = adsRepo;
        this.eventsRepo = eventsRepo;
        this.roomRepository = roomRepository;
        this.imageDestinationRepo = imageDestinationRepo;
    }

    @PostMapping("/uploadImage")
    public ImageDestination uploadFile(@RequestParam("file") MultipartFile file) {
        return fileStorageService.storeFile(file);
    }

    //NIEZALECANA (rozwaz uzycie /uploadImage)
    //nie wiem dlaczego ale jak sie nie da praamtru files to w prawdzie nie dodoa zadnych plikow
    //ale zwroci 200 jakby wszystko bylo dobrze
    @PostMapping("/uploadMultipleImages")
    public List<ImageDestination> uploadMultipleFiles(@RequestParam(name="files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @GetMapping("/downloadAdditionalImage")
    public ResponseEntity<Resource> downloadAdditionalImage(@RequestParam Long idImage, HttpServletRequest request) {

        Optional<ImageDestination>  image = imageDestinationRepo.findById(idImage);
        if(image.isEmpty()) {
            throw new MyFileNotFoundException("Image with id:  " + idImage + "dont exists");
        }
        return fetchImage(image.get(), request);
    }

    @GetMapping("/events/downloadMainImage")
    public ResponseEntity<Resource> downloadMainImage(@RequestParam Long idEvent, HttpServletRequest request) {
        Optional<Event> optionalEvent = eventsRepo.findById(idEvent);
        if(optionalEvent.isEmpty()) {
            throw new MyFileNotFoundException("Event with id:  " + idEvent + "not found");
        }

        Optional<ImageDestination>  mainImage = imageDestinationRepo.findById(optionalEvent.get().getMainImage());
        if(mainImage.isEmpty()) {
            throw new MyFileNotFoundException("Event with id:  " + idEvent + "dont have main image");
        }
        return fetchImage(mainImage.get(), request);
    }

    private ResponseEntity<Resource> fetchImage(ImageDestination imageProduct, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(imageProduct.getImageName());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

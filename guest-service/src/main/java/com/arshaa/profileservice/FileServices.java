package com.arshaa.profileservice;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arshaa.entity.Guest;
import com.arshaa.entity.Image;
import com.arshaa.exception.ResourceNotFoundException;
import com.arshaa.repository.GuestRepository;
import com.arshaa.repository.ImageRepository;
import com.netflix.discovery.provider.Serializer;

@Service
public class FileServices {
    
    @Autowired
    private GuestRepository guestRepository ;
    
    @Autowired
    private ImageRepository imageRepository ;
  
        @SuppressWarnings("null")
        public Image uploadImage(String path, MultipartFile file,String guestId) throws IOException {
            // File name
           
            String name = file.getOriginalFilename();    
            // abc.png
            // random name generate file
            String randomID = UUID.randomUUID().toString();
            String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
            // Full path
            String filePath = path + File.separator + fileName1;
            // create folder if not created
            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            // file copy
            Files.copy(file.getInputStream(), Paths.get(filePath));
           System.out.println(imageRepository.existsByGuestId(guestId));
            if(!imageRepository.existsByGuestId(guestId) )
            {
            	Guest guest =guestRepository.findById(guestId).orElseThrow(() -> new  ResourceNotFoundException(false,"Guest Id Not Found"));	
            
                 Image newFile=new Image();   
                newFile.setGuestId(guest.getId());
                newFile.setImageName(fileName1);  
                return imageRepository.save(newFile);
            }
            return null;
          
        }  
        public InputStream getResource(String path, String fileName,String guestId) throws FileNotFoundException {
            String fullPath = path + File.separator + fileName;
            InputStream is = new FileInputStream(fullPath);
            // db logic to return inpustream
            return is;
        }

    }


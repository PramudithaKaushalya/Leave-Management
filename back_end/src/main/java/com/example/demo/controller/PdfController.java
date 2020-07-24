package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/pdf")
public class PdfController {

    @Value("${app.path}")
	private String path;

    @GetMapping("/leave_policy")
    public StreamingResponseBody getPdf( HttpServletResponse response ) throws IOException {
    
        InputStream inputStream = new FileInputStream(new File(path));
        try{
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Leave Policy.pdf\"");
            
            return outputStream -> {
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                }
            };
        
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
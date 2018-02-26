package com.isayusee.api.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Provides viewing and management of data sources that contain PII data the user may have a right to see */
@RestController
@Secured("ROLE_USER")
@CrossOrigin
public class DataSourcesController {

    @GetMapping("api/dummy")
    public String dummy() {
        return "dummy response";
    }

}

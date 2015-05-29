package br.com.pandox.lastcloud.view;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloEndpoint {

    @RequestMapping(value = "/api/hello", method = RequestMethod.GET)
    public ResponseEntity<HelloWorld> get() {
        return new ResponseEntity<>(new HelloWorld(), HttpStatus.OK);
    }

}

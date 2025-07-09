package mardi.erp_mini;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class RootController {
    @GetMapping(value = {""})
    public ResponseEntity index() {
        return ResponseEntity.ok("G'day mates");
    }

    @GetMapping(value = {"/health-check"})
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robot() {
        return "User-agent: *\n" +
                "Disallow: /";
    }
}

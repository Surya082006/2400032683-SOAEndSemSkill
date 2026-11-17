package soa.controller;
import soa.entity.User;
import soa.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody User user) {

        service.saveUser(user);

        return ResponseEntity.ok(
                "User Created Successfully");
    }
}
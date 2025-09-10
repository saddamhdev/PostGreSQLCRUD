package com.PostGreSQL.controller;
import com.PostGreSQL.model.User;
import com.PostGreSQL.repository.UserRepository;
import com.PostGreSQL.service.PagePermissionService;
import com.PostGreSQL.springSecurity.JwtFilter;
import com.PostGreSQL.springSecurity.JwtGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private JwtGenerator jwtGenerator;
    private JwtFilter jwtFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PagePermissionService pagePermissionService;
    public UserController(UserRepository userRepository,JwtGenerator jwtGenerator,JwtFilter jwtFilter) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.jwtFilter=jwtFilter;
    }

    // POST: Create new user
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("❌ Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    // GET: List all users (for testing)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response,
                                                     FilterChain chain, @RequestBody Map<String, String> requestData) {

        Map<String, Object> responseData = new HashMap<>();

        String username = requestData.get("username");
        String password = requestData.get("password");

        Optional<User> data = userRepository.findByEmail(username);

        if (data.isEmpty()) {
            responseData.put("error", "User not found or inactive.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
        }

        User employee = data.get();

        if (!passwordEncoder.matches(password, employee.getPassword())) {
            responseData.put("error", "Invalid credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
        }

        // Generate Tokens
        String token = jwtGenerator.generateToken(username);
        String refreshToken = jwtGenerator.generateRefreshToken(username);

        // ✅ এখানে .toString() বাদ দিন
        responseData.put("token", token);
        responseData.put("refreshToken", refreshToken);
        responseData.put("result", "Authenticated");
        responseData.put("accessComponent", pagePermissionService.getPermissionsAsTree(employee.getId()));

        if (employee.getUserType().equals("SuperAdmin")) {
            responseData.put("role", "SuperAdmin");
        }
        if (employee.getUserType().equals("Admin")) {
            responseData.put("role", "Admin");
        }
        if (employee.getUserType().equals("Branch")) {
            responseData.put("role", "Branch");
        }


        return ResponseEntity.ok(responseData);
    }

    // ✅ Delete user using POST (instead of DELETE)
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // ✅ Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)             // return 200 + user
                .orElseGet(() -> ResponseEntity.notFound().build()); // return 404 if not found
    }
    // ✅ Update User (via POST)
    @PostMapping("/{id}/update")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());

                    // Update password only if provided
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    existingUser.setUserType(updatedUser.getUserType());

                    User saved = userRepository.save(existingUser);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

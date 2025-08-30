package com.HieuPahm.AniHoyo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.model.entities.Permission;
import com.HieuPahm.AniHoyo.model.entities.Role;
import com.HieuPahm.AniHoyo.model.entities.User;
import com.HieuPahm.AniHoyo.repository.PermissionRepository;
import com.HieuPahm.AniHoyo.repository.RoleRepository;
import com.HieuPahm.AniHoyo.repository.UserRepository;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(RoleRepository roleRepository, PermissionRepository permissionRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>> INITIAL DATABASE BEGINS:");
        long countPermission = this.permissionRepository.count();
        long countRole = this.roleRepository.count();
        long countUser = this.userRepository.count();

        if (countPermission == 0) {
            ArrayList<Permission> arrResult = new ArrayList<>();
            arrResult.add(new Permission("Create a film ", "/api/v1/add-film", "POST", "FILMS"));
            arrResult.add(new Permission("Update a film", "/api/v1/update-film", "PUT", "FILMS"));
            arrResult.add(new Permission("Delete a film ", "/api/v1/delete-film/{id}", "DELETE", "FILMS"));
            arrResult.add(new Permission("Get film by id", "/api/v1/film/{id}", "GET", "FILMS"));
            arrResult.add(new Permission("Get films with pagination", "/api/v1/films", "GET", "FILMS"));

            arrResult.add(new Permission("Create a season ", "/api/v1/add-season", "POST", "SEASONS"));
            arrResult.add(new Permission("Update a season", "/api/v1/update-season", "PUT", "SEASONS"));
            arrResult.add(new Permission("Delete a season ", "/api/v1/delete-season/{id}", "DELETE", "SEASONS"));
            arrResult.add(new Permission("Get season by id", "/api/v1/season/{id}", "GET", "SEASONS"));
            arrResult.add(new Permission("Get seasons with pagination", "/api/v1/seasons", "GET", "SEASONS"));
            arrResult.add(new Permission("fetch all seasons of film", "/seasons/by-film/{filmId}", "GET", "SEASONS"));

            arrResult.add(new Permission("Create a tag", "/api/v1/add-tag", "POST", "TAGS"));
            arrResult.add(new Permission("Update a tag", "/api/v1/update-tag", "PUT", "TAGS"));
            arrResult.add(new Permission("Delete a tag", "/api/v1/delete-tag/{id}", "DELETE", "TAGS"));
            arrResult.add(new Permission("Get tags with pagination", "/api/v1/tags", "GET", "TAGS"));

            arrResult.add(new Permission("Create a permission", "/api/v1/add-permission", "POST", "PERMISSIONS"));
            arrResult.add(new Permission("Update a permission", "/api/v1/update-permission", "PUT", "PERMISSIONS"));
            arrResult.add(
                    new Permission("Delete a permission", "/api/v1/delete-permission/{id}", "DELETE", "PERMISSIONS"));
            arrResult.add(new Permission("Get a permission by id", "/api/v1/permission/{id}", "GET", "PERMISSIONS"));
            arrResult
                    .add(new Permission("Get permission with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arrResult.add(new Permission("Create a role", "/api/v1/add-role", "POST", "ROLES"));
            arrResult.add(new Permission("Update a role", "/api/v1/update-role", "PUT", "ROLES"));
            arrResult.add(new Permission("Delete a role", "/api/v1/delete-role/{id}", "DELETE", "ROLES"));
            arrResult.add(new Permission("Get role by id", "/api/v1/role/{id}", "GET", "ROLES"));
            arrResult.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));

            arrResult.add(new Permission("Create a user", "/api/v1/add-user", "POST", "USERS"));
            arrResult.add(new Permission("Update a user", "/api/v1/update-user", "PUT", "USERS"));
            arrResult.add(new Permission("Delete a user", "/api/v1/delete-user/{id}", "DELETE", "USERS"));
            arrResult.add(new Permission("Get a user by id", "/api/v1/user/{id}", "GET", "USERS"));
            arrResult.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));

            arrResult.add(new Permission("Create a category", "/api/v1/add-category", "POST", "CATEGORIES"));
            arrResult.add(new Permission("Update a category", "/api/v1/update-category", "PUT", "CATEGORIES"));
            arrResult.add(new Permission("Delete a category", "/api/v1/delete-category/{id}", "DELETE", "CATEGORIES"));
            arrResult.add(new Permission("Get categories with pagination", "/api/v1/categories", "GET", "CATEGORIES"));

            arrResult.add(new Permission("Create a episode", "/api/v1/add-episode", "POST", "EPISODES"));
            arrResult.add(new Permission("Update a episode", "/api/v1/update-episode", "PUT", "EPISODES"));
            arrResult.add(new Permission("Delete a episodde", "/api/v1/delete-epsiode/{id}", "DELETE", "EPISODES"));
            arrResult.add(
                    new Permission("fetch all eps of season", "/api/v1/episodes/by-season/{seasonId}", "GET",
                            "EPISODES"));

            arrResult.add(new Permission("Upload video", "/api/v1/upload/video", "POST", "EPISODES"));
            arrResult.add(new Permission("Upload file", "/api/v1/files", "POST", "FILES"));

            this.permissionRepository.saveAll(arrResult);
        }
        if (countRole == 0) {
            List<Permission> permissions = this.permissionRepository.findAll();

            Role initRole = new Role();
            initRole.setName("ADMIN");
            initRole.setDescription("Contain full of permissions on this web service");
            initRole.setActive(true);
            initRole.setPermissions(permissions);

            this.roleRepository.save(initRole);
        }
        if (countUser == 0) {
            User initUser = new User();
            initUser.setFullName("HieuPahmR2");
            initUser.setEmail("admin@gmail.com");
            initUser.setPassword(this.passwordEncoder.encode("123456"));

            Role userRole = this.roleRepository.findByName("ADMIN");
            if (userRole != null) {
                initUser.setRole(userRole);
            }
            this.userRepository.save(initUser);
        }
        if (countRole > 0 && countPermission > 0 && countUser > 0) {
            System.out.println("SKIP INITIAL DATABASE");
        } else {
            System.out.println("END TASK");
        }
    }
}

package org.dbconnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DatabaseManagerController {

    @Autowired
    private DatabaseManagerService databaseManagerService;

    @GetMapping("/")
    public String index(Model model) {
        List<User> users = databaseManagerService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @PostMapping("/insert")
    public String insert(@RequestParam("name") String name) {
        databaseManagerService.insertUser(name);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") int id, @RequestParam("name") String name) {
        databaseManagerService.updateUser(id, name);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        databaseManagerService.deleteUser(id);
        return "redirect:/";
    }

package com.datdev.controller;

import com.datdev.MapExplorerApplication;
import com.datdev.model.Map;
import com.datdev.repo.MapRepo;
import com.datdev.utils.SearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ImageController {
    @Autowired
    MapRepo mapRepository;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "") String search,
                        @RequestParam(required = false, defaultValue = "0") int offset,
                        @RequestParam(required = false, defaultValue = "50") int numPerPage) {

        model.addAttribute("offset", offset);
        model.addAttribute("search", search);
        model.addAttribute("numPerPage", numPerPage);
        model.addAttribute("basePath", MapExplorerApplication.basePath);
        return "/index";
    }

    @GetMapping("/image")
    public String image(Model model, @RequestParam(required = false) Integer id) {
        if (id == null) return "redirect:/";
        Optional<Map> map = mapRepository.findById(id);
        if (map.isEmpty()) return "redirect:/";

        model.addAttribute("map", map.get());
        model.addAttribute("basePath", MapExplorerApplication.basePath);
        return "/image";
    }
}

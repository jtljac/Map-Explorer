package com.datdev.controller;

import com.datdev.MapExplorerApplication;
import com.datdev.model.Map;
import com.datdev.repo.DuplicateRepo;
import com.datdev.repo.MapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ImageController {
    @Autowired
    MapRepo mapRepository;

    @Autowired
    DuplicateRepo duplicateRepo;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "") String search,
                        @RequestParam(required = false, defaultValue = "random") String order,
                        @RequestParam(required = false, defaultValue = "desc") String orderdir,
                        @RequestParam(required = false, defaultValue = "0") int offset,
                        @RequestParam(required = false, defaultValue = "50") int numPerPage) {

        model.addAttribute("offset", offset);
        model.addAttribute("search", search);
        model.addAttribute("order", order);
        model.addAttribute("orderdir", orderdir);
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

    @GetMapping("/nextimage")
    public String nextImage(@RequestParam Integer currentid) {
        Optional<Integer> nextMap = mapRepository.findNextID(currentid);

        return nextMap.map(integer -> "redirect:/image?id=" + integer).orElseGet(() -> "redirect:/image?id=" + currentid);
    }

    @GetMapping("/previmage")
    public String prevImage(@RequestParam Integer currentid) {
        Optional<Integer> prevMap = mapRepository.findPrevID(currentid);

        return prevMap.map(integer -> "redirect:/image?id=" + integer).orElseGet(() -> "redirect:/image?id=" + currentid);
    }

    @GetMapping("/duplicates")
    public String duplicates(Model model) {
        model.addAttribute("duplicates", duplicateRepo.findAll());
        model.addAttribute("basePath", MapExplorerApplication.basePath);
        return "/duplicates";
    }

    @GetMapping("uploadImage")
    public String upload(Model model) {
        return "/upload";
    }
}

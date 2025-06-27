package com.csv.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csv.file.model.Member;
import com.csv.file.repository.MemberRepository;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public String listMembers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model //Used to pass data from the controller to the Thymeleaf view
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageable);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", memberPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "members"; // View name
    }
}

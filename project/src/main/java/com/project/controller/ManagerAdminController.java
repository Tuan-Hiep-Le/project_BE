package com.project.controller;

import com.project.entity.Book;
import com.project.entity.User;
import com.project.service.impl.ManagerBookServiceImpl;
import com.project.service.impl.ManagerOrderServiceImpl;
import com.project.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ManagerAdminController {
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ManagerOrderServiceImpl managerOrderService;
    @GetMapping("/admin")
    public String adminHome(Model model, @RequestParam(defaultValue = "overview") String section, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loggedUser");
        if (user.getAvatar() != null) {
            model.addAttribute("hasAvatar",true);
            model.addAttribute("avatar",user.getAvatar());
        } else {
            model.addAttribute("hasAvatar",false);
        }
        long totalBook = managerBookService.getCountBook();
        long totalUser = userService.countUser();
        long totalOrder = managerOrderService.countOrder();
        BigDecimal totalRevenue = managerOrderService.getTotalRevenue();
        model.addAttribute("totalBook",totalBook);
        model.addAttribute("totalUser",totalUser);
        model.addAttribute("totalOrder",totalOrder);
        model.addAttribute("totalRevenue",totalRevenue);
        model.addAttribute("section",section);
        return "admin_home";
    }

    @GetMapping("/overview")
    public String moveOverview(Model model){
        long totalBook = managerBookService.getCountBook();
        long totalUser = userService.countUser();
        long totalOrder = managerOrderService.countOrder();
        BigDecimal totalRevenue = managerOrderService.getTotalRevenue();
        model.addAttribute("totalBook",totalBook);
        model.addAttribute("totalUser",totalUser);
        model.addAttribute("totalOrder",totalOrder);
        model.addAttribute("totalRevenue",totalRevenue);
        return "overview";
    }

    @GetMapping("/admin/manage_book")
    public String managerBook(Model model, @RequestParam(value = "valuePage",defaultValue = "0") int valuePage,HttpServletRequest request ){
        Pageable pageable = PageRequest.of(valuePage,10);
        Page<Book> books = managerBookService.getAllBook(pageable);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("books",books);
        model.addAttribute("totalPage",(books.getTotalPages()));
        model.addAttribute("section","manage_book");
        User user = (User) request.getSession().getAttribute("loggedUser");
        if (user.getAvatar() != null) {
            model.addAttribute("hasAvatar",true);
            model.addAttribute("avatar",user.getAvatar());
        } else {
            model.addAttribute("hasAvatar",false);
        }
        return "admin_home";
    }

    @GetMapping("/admin/move_manage_order")
    public String moveManageUser(Model model, HttpServletRequest request){
        User user =  (User) request.getSession().getAttribute("loggedUser");
        if (user.getAvatar() != null){
            model.addAttribute("hasAvatar",true);
            model.addAttribute("avatar",user.getAvatar());
        }else {
            model.addAttribute("hasAvatar",false);
        }
        List<Object[]> listOrder = managerOrderService.getInformationOrder();
        model.addAttribute("listOrder",listOrder);
        model.addAttribute("section","manage_order");
        return "admin_home";
    }


}

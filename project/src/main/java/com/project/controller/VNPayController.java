package com.project.controller;

import com.project.response.VNPayResponse;
import com.project.service.impl.VNPayServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class VNPayController {
    @Autowired
    private VNPayServiceImpl vnPayService;
    @GetMapping("/checkout_after_payment")
    public String checkoutAfterPayment(@ModelAttribute VNPayResponse vnPayResponse){
        if (vnPayService.isSuccess(vnPayResponse)){
            return "success";
        }
        return "fail";
    }


}

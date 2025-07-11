package com.example.project.service;

import java.util.List;

public interface ManagerUserVoucherService {
    //Lay ra cac voucher TRANSPORT cua user
    public List<Object[]> getAllVoucherTransfer(Integer id);

    //Lay ra cac voucher DISCOUNT của  user
    public List<Object[]> getAllVoucherDiscount(Integer id);
}

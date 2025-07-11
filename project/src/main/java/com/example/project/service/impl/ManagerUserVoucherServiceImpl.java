package com.example.project.service.impl;

import com.example.project.entity.Voucher;
import com.example.project.entity.enum_entity.TypeVoucher;
import com.example.project.repository.ManagerUserVoucherRepository;
import com.example.project.service.ManagerUserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerUserVoucherServiceImpl implements ManagerUserVoucherService {
    @Autowired
    private ManagerUserVoucherRepository managerUserVoucherRepository;

    @Override
    public List<Object[]> getAllVoucherTransfer(Integer id) {
        List<Object[]> list = managerUserVoucherRepository.getAllVoucherById(id);
        List<Object[]> listVoucherTranfer = new ArrayList<>();
        for (Object[] row : list){
            Voucher voucher = (Voucher) row[0];

            if (voucher.getTypeVoucher() == TypeVoucher.TRANSPORT){
                listVoucherTranfer.add(row);
            }

        }
        return listVoucherTranfer;
    }

    @Override
    public List<Object[]> getAllVoucherDiscount(Integer id) {
        List<Object[]> list = managerUserVoucherRepository.getAllVoucherById(id);
        List<Object[]> listVoucherDiscount = new ArrayList<>();
        for (Object[] row : list){
            Voucher voucher = (Voucher) row[0];

            if (voucher.getTypeVoucher() == TypeVoucher.DISCOUNT){
                listVoucherDiscount.add(row);
            }

        }
        return listVoucherDiscount;
    }
}

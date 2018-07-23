package com.capgemini.core.pwa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.core.pwa.bean.Customer;

public interface WalletService {
	public Customer createAccount(String name ,String mobileno, BigDecimal amount);
	public Customer showBalance (String mobileno);
	public Customer fundTransfer (String sourceMobileNo,String targetMobileNo, BigDecimal amount);
	public Customer depositAmount (String mobileNo,BigDecimal amount );
	public Customer withdrawAmount(String mobileNo, BigDecimal amount);
	boolean isValid(Customer customer);
	public ArrayList transactionHistory(String mobileno);
}

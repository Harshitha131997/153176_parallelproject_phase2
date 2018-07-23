package com.capgemini.core.pwa.repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Date;
import com.capgemini.core.pwa.bean.Customer;
public interface WalletRepo {
	public boolean save(Customer customer);
	public Customer findOne(String mobileNo);
	public boolean saveTransaction(String mobileNo,String transactiontype,Date date,BigDecimal amount);
	public ArrayList printTransactionHistory(String mobileNo);
}

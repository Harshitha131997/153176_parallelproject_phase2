package com.capgemini.core.pwa.service;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import com.capgemini.core.pwa.bean.Customer;
import com.capgemini.core.pwa.bean.Wallet;
import com.capgemini.core.pwa.exception.InsufficientBalanceException;
import com.capgemini.core.pwa.exception.InvalidInputException;
import com.capgemini.core.pwa.repository.WalletRepo;
import com.capgemini.core.pwa.repository.WalletRepoImpl;

public class WalletServiceImpl implements WalletService{
	ArrayList list;
	WalletRepo walletrepo;
	public WalletServiceImpl() {		
		walletrepo= new WalletRepoImpl();
	}
	@Override
	public Customer createAccount(String name, String mobileno, BigDecimal amount) {
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		wallet.setBalance(amount);
		customer.setName(name);
		customer.setMobileNo(mobileno);	
		customer.setWallet(wallet);
		if(isValid(customer))
			walletrepo.save(customer);
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		walletrepo.saveTransaction(mobileno, "Account Created",date, amount);
		return customer;
	}
	@Override
	public Customer showBalance(String mobileno) {
		if(isMobileNumberInvalid(mobileno)==true)
			throw new InvalidInputException("invalid mobile number");
		Customer customer = new Customer();
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		customer = walletrepo.findOne(mobileno);
		BigDecimal bal=customer.getWallet().getBalance();
		walletrepo.saveTransaction(mobileno, "Balance check",date,bal);
		return customer;
	}
	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		Customer customer1;
		Customer customer2;
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);	
		customer1=withdrawAmount(sourceMobileNo,amount);
		walletrepo.saveTransaction(sourceMobileNo, "FundTransfer-Withdraw",date, amount);
		walletrepo.save(customer1);
		customer2=depositAmount(targetMobileNo,amount);
		walletrepo.saveTransaction(targetMobileNo, "FundTransfer-Deposit",date, amount);
		walletrepo.save(customer2);
		return customer1;

	}
	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		if(isMobileNumberInvalid(mobileNo)==true)
			throw new InvalidInputException("invalid mobile number");
		if(isBalanceInvalid(amount)==true)
			throw new InvalidInputException("amount is not valid");
		Customer customer1;
		customer1 = walletrepo.findOne(mobileNo);
		BigDecimal bal = customer1.getWallet().getBalance().add(amount);
		Wallet wallet= new Wallet();
		wallet.setBalance(bal);
		customer1.setWallet(wallet);
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		walletrepo.save(customer1);
		walletrepo.saveTransaction(mobileNo, "Deposit",date, amount);
		return customer1;

	}
	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		if(isMobileNumberInvalid(mobileNo)==true)	
			throw new InvalidInputException("invalid mobile number");
		if(isBalanceInvalid(amount))
			throw new InvalidInputException("amount is not valid");
		Customer customer1;
		Wallet wallet= new Wallet();	
		customer1 = walletrepo.findOne(mobileNo);
		if(customer1.getWallet().getBalance().compareTo(amount)>0)
		{
			BigDecimal bal = customer1.getWallet().getBalance().subtract(amount);
			wallet.setBalance(bal);
			customer1.setWallet(wallet);
			long millis=System.currentTimeMillis();
			Date date=new Date(millis);
			walletrepo.save(customer1);
			walletrepo.saveTransaction(mobileNo, "Withdraw",date, amount);
		} 
		else
			throw new InsufficientBalanceException("balance is not adequate");
		return customer1;	
	}
	@Override
	public boolean isValid(Customer customer) 
	{
		if( customer.getName() == null ||  customer.getName().trim().isEmpty() )
			throw new InvalidInputException( "Name Cannot be Empty" );
		if( customer.getMobileNo() == null ||  isMobileNumberInvalid( customer.getMobileNo() ) || customer.getMobileNo().trim().isEmpty())
			throw new InvalidInputException( "Mobile Number is invalid" );
		if(customer.getWallet() == null || isBalanceInvalid(customer.getWallet().getBalance()))
			throw new InvalidInputException("Balance is invalid ");
		else
			return true;
	}
	public static boolean isMobileNumberInvalid( String phoneNumber ){
		if(String.valueOf(phoneNumber).matches("^[7-9]{1}[0-9]{9}$")) 
			return false;		
		else 
			return true;
	}
	public static boolean isBalanceInvalid(BigDecimal balance) {
		if(balance.compareTo(new BigDecimal(0))<0)
			return true;
		else
			return false;
	}
	@Override
	public ArrayList transactionHistory(String mobileNo){
		if(isMobileNumberInvalid(mobileNo)==true)
			throw new InvalidInputException("invalid mobile number");
		list=walletrepo.printTransactionHistory(mobileNo);
		return list;
	}  

}








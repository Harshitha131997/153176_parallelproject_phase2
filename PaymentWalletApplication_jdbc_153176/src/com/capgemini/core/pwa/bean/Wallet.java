package com.capgemini.core.pwa.bean;
import java.math.BigDecimal;
public class Wallet {
	private BigDecimal balance;
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return ", balance="+balance;
	}
	public Wallet(BigDecimal balance) {
		this.balance = balance;
	}
	public Wallet() {
		super();
	}
}
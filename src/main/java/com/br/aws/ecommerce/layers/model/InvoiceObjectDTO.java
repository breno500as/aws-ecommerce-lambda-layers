package com.br.aws.ecommerce.layers.model;

public class InvoiceObjectDTO {

	private String custumerName;

	private String invoiceNumber;

	private long totalValue;

	private int quantity;

	private String productId;

	public String getCustumerName() {
		return custumerName;
	}

	public void setCustumerName(String custumerName) {
		this.custumerName = custumerName;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public long getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(long totalValue) {
		this.totalValue = totalValue;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "InvoiceFileEntity [custumerName=" + custumerName + ", invoiceNumber=" + invoiceNumber + ", totalValue="
				+ totalValue + ", quantity=" + quantity + ", productId=" + productId + "]";
	}

}

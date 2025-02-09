package Ecommerce.restAssured.Pojo;

public class validateOrderResponce {
	private orderData data;
	private String message;

	public orderData getData() {
		return data;
	}

	public void setData(orderData data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

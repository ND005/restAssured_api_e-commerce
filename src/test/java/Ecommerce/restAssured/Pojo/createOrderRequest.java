package Ecommerce.restAssured.Pojo;
import java.util.List;
public class createOrderRequest {
	
	private List<createOrderSubRequest> orders;
	public List<createOrderSubRequest> getOrders() {
		return orders;
	}

	public void setOrders(List<createOrderSubRequest> orders) {
		this.orders = orders;
	}
	
}

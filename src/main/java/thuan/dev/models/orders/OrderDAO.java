package thuan.dev.models.orders;

import java.util.List;

public interface OrderDAO {

    public boolean addOrder(Order ord);

    public boolean removeOrder(Order orderID);

    public void updateOrder(int orderID);

    public List<Order> showDisplayCard();

    public void receipt();

    public List<Order> menuOrder(Order ord);

    public Order getOrder(int customerID,int productID);

    public void update(int productID, int quantity, int total);

}

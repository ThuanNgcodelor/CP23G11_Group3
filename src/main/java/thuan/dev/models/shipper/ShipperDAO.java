package thuan.dev.models.shipper;

import java.util.List;

public interface ShipperDAO {
    public List<Shippers> showAllShipper(Shippers ship);
    public boolean deleteShipper(Shippers ship);
    public boolean addShipper(Shippers ship);
    public boolean updateShipper(Shippers ship);
    public void getShipper(Shippers ship);

}

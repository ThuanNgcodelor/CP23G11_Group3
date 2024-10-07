package thuan.dev.models.voucher;



public class Voucher {

    private final int id;
    private final String voucher;
    private final String discount;

    public Voucher(int id, String voucher, String discount) {
        this.id = id;
        this.voucher = voucher;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getVoucher() {
        return voucher;
    }

    public String getDiscount() {
        return discount;
    }
}

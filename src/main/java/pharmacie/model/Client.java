package pharmacie.model;

/**
 * Représente un client de la pharmacie.
 */
public class Client {
    private int id;
    private String name;
    private String phone;
    private String email;
    private int loyaltyPoints;

    public Client() {
    }

    public Client(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.loyaltyPoints = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return name;
    }
}

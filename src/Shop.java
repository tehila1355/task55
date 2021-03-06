import java.util.Scanner;

public class Shop {
    private static final int SIGN_IN_CUSTOMER = 1;
    private static final int SIGN_IN_EMPLOYEE = 2;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int IS_CLUB_MEMBER = 1;
    private static final int IS_NOT_CLUB_MEMBER = 2;
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 3;
    private static final int COMPLETION_OF_PURCHASE = -1;
    private static final int PRODUCT_IN_STOCK = 1;
    private static final int PRODUCT_NOT_IN_STOCK = 2;
    private static final int MIN_PRODUCT_PRICE = 1;
    private static final int MIN_PRODUCT_AMOUNT = 1;
    private static final int MIN_PROPER_DISCOUNT = 0;
    private static final int MAX_PROPER_DISCOUNT = 100;


    private Employee[] employees;
    private Customer[] customers;
    private Product[] products;

    public Shop() {
        this.employees = new Employee[0];
        this.customers = new Customer[0];
        this.products = new Product[0];
    }

    public Product[] getProducts() {
        return products;
    }

    public void crateAccount() {
        Scanner scanner = new Scanner(System.in);

        String firstName;
        String lastName;
        String username;
        String password;
        int userType;
        int clubMemberChoice;
        int rank;
        boolean usernameIsExist;
        boolean isCostumer;
        boolean isClubMember;


        do {
            System.out.println("-for customer enter 1" + "\n" + "-for employee enter 2");
            userType = scanner.nextInt();
        } while (userType != SIGN_IN_CUSTOMER && userType != SIGN_IN_EMPLOYEE);

        if (userType == 1) {
            isCostumer = true;
        } else {
            isCostumer = false;
        }

        Scanner scanner1 = new Scanner(System.in);
        do {
            System.out.println("Enter your first name: ");
            firstName = scanner1.nextLine();
        } while (hasDigit(firstName));

        do {
            System.out.println("Enter your last Name : ");
            lastName = scanner1.nextLine();
        } while (hasDigit(lastName));


        do {
            System.out.println("Enter username: ");
            username = scanner1.nextLine();
            if (isCostumer) {
                usernameIsExist = isCustomerUsernameExist(username);
            } else {
                usernameIsExist = isEmployeeUsernameExist(username);
            }
            if (usernameIsExist) {
                System.out.println("Username is already taken.");
            }
        } while (usernameIsExist);

        do {
            System.out.println("Enter password with 6 chars: ");
            password = scanner1.nextLine();
        } while (password.length() < MIN_PASSWORD_LENGTH);

        do {
            System.out.println("If you are a club member tap 1 if you do not tap 2");
            clubMemberChoice = scanner1.nextInt();
        } while (clubMemberChoice != IS_CLUB_MEMBER && clubMemberChoice != IS_NOT_CLUB_MEMBER);

        if (clubMemberChoice == IS_CLUB_MEMBER) {
            isClubMember = true;
        } else {
            isClubMember = false;
        }


        if (isCostumer) {
            addCustomerToArray(firstName, lastName, username, password, isClubMember);
        } else {
            do {
                System.out.println("What is your rank\n" +
                        "1 - Regular employee.\n" +
                        "2 - Director.\n" +
                        "3 - member of the management team.");
                rank = scanner.nextInt();
            } while (rank > MAX_RANK || rank < MIN_RANK);
            addEmployeeToArray(firstName, lastName, username, password, isClubMember, rank);
        }
    }

    public Employee signInEmployee(String usernameToCheck, String passwordToCheck) {
        for (int i = 0; i < this.employees.length; i++) {
            Employee currentEmployee = this.employees[i];
            if (currentEmployee.getUsername().equals(usernameToCheck) &&
                    currentEmployee.getPassword().equals(passwordToCheck)) {
                return currentEmployee;
            }
        }
        return null;
    }

    public Customer signInCustomer(String usernameToCheck, String passwordToCheck) {

        for (int i = 0; i < this.customers.length; i++) {
            Customer currentCustomer = this.customers[i];
            if (currentCustomer.getUsername().equals(usernameToCheck) &&
                    currentCustomer.getPassword().equals(passwordToCheck)) {
                return currentCustomer;
            }
        }
        return null;
    }

    public int purchase(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        int productNumber;
        int productAmount;
        Product[] productsInStock = printAndGetProductInStock();
        do {
            System.out.println("please enter the product's number:");
            System.out.println("Else - enter (-1) for end ");
            productNumber = scanner.nextInt();
        } while (productNumber > productsInStock.length || productNumber < COMPLETION_OF_PURCHASE || productNumber == 0);

        if (productNumber != COMPLETION_OF_PURCHASE) {
            if (customer.getShoppingCart().getTotalPrice() == 0) {
                customer.addNumberOfPurchase();
            }
            do {
                System.out.println("please enter the amount: ");
                productAmount = scanner.nextInt();
            } while (productAmount < MIN_PRODUCT_AMOUNT);

            for (int i = 0; i < productAmount; i++) {
                customer.setShoppingCart(productsInStock[productNumber - 1]);
            }
            double shoppingCartPrice = customer.calculatePrice();
            customer.getShoppingCart().setTotalPrice(shoppingCartPrice);
        }
        System.out.println(customer.getShoppingCart());
        return productNumber;
    }

    public void printCustomerWithTheTopDollar (){
        if (this.customers.length != 0){
            Customer topDollarCustomer = this.customers[0];

            for (int i = 1; i < this.customers.length ; i++) {
                if (this.customers[i].getSumPurchases() > topDollarCustomer.getSumPurchases()) {
                    topDollarCustomer = this.customers[i];
                }
            }
            System.out.println("the customer with the top dollar purchase is: " + topDollarCustomer.getFirstName() + " " + topDollarCustomer.getLastName() + "\n");
        }else {
            System.out.println("there is no costumers\n");
        }
    }

    public void printAllCustomers() {
        if (this.customers.length != 0){
            for (int i = 0; i < this.customers.length; i++) {
                System.out.println(i + 1);
                System.out.println(this.customers[i]);
            }
        } else {
            System.out.println("there is no costumers");
        }
    }

    public void printTheClubMemberCustomer() {
        int counter = 1;
        for (int i = 0; i < this.customers.length; i++) {
            if (this.customers[i].isClubMember()) {
                System.out.println(counter);
                System.out.println(this.customers[i]);
                counter++;
            }
        }
    }

    public void printCustomersWithAtLeastOnePurchase () {
        for (int i = 0; i < this.customers.length; i++) {
            Customer currentCustomer = this.customers[i];
            if (currentCustomer.getNumberOfPurchase() >= 1) {
                System.out.println(currentCustomer);
            }
        }
    }

    public void changeIsInStock () {
        Scanner scanner = new Scanner(System.in);
        int indexOfTheProductToChange;
        int ifInStock;
        boolean isInStock = false;
        for (int i = 0; i < this.products.length; i++) {
            System.out.println((i + 1) + ". " + this.products[i]);
        }
        do {
            System.out.println("Enter the number of the product you want to change its status to: ");
            indexOfTheProductToChange = scanner.nextInt();
        }while (indexOfTheProductToChange > this.products.length || indexOfTheProductToChange < 1);


        do {
            System.out.println("If the product is in stock enter 1, else enter 2");
            ifInStock = scanner.nextInt();
        }while (ifInStock != PRODUCT_IN_STOCK && ifInStock != PRODUCT_NOT_IN_STOCK);

        if (ifInStock == PRODUCT_IN_STOCK) {
            isInStock = true;
        }
        this.products[indexOfTheProductToChange - 1].isInStock(isInStock);
    }

    public void addNewProduct (){
        Scanner scanner = new Scanner(System.in);
        String description;
        int productPrice;
        double discountPercentages;

        System.out.println("Please enter the product's description: ");
        description = scanner.nextLine();

        do {
            System.out.println("Please enter the product's price: ");
            productPrice = scanner.nextInt();
        }while (productPrice < MIN_PRODUCT_PRICE);

        do {
            System.out.println("Enter the discount Percentages for club members:  ");
            discountPercentages = scanner.nextDouble();
        }while (discountPercentages < MIN_PROPER_DISCOUNT || discountPercentages > MAX_PROPER_DISCOUNT);

        Product newProduct = new Product(description,productPrice,discountPercentages);
        addProductToArray(newProduct);
        System.out.println("-Product Successfully Added!-");
    }



    private void addProductToArray (Product productToAdd) {
        Product[] newProductArray = new Product[products.length + 1];
        for (int i = 0; i < this.products.length; i++) {
            newProductArray[i] = this.products[i];
        }
        newProductArray[this.products.length] = productToAdd;
        this.products = newProductArray;
    }

    private Product[] printAndGetProductInStock() {
        System.out.println("A List Of Products In Stock ------");
        int counter = 1;
        for (int i = 0; i < this.products.length; i++) {
            if (this.products[i].isInStock()) {
                counter++;
            }
        }

        Product[] productsInStock = new Product[counter - 1];
        counter = 1;
        for (int i = 0; i < productsInStock.length; i++) {
            if (this.products[i].isInStock()) {
                System.out.print(counter + ". ");
                System.out.println(this.products[i]);
                productsInStock[i] = this.products[i];
                counter++;
            }
        }
        System.out.println("-------------------");
        return productsInStock;
    }

    private boolean isEmployeeUsernameExist(String username) {
        boolean isExist = false;
        for (int i = 0; i < this.employees.length; i++) {
            if (this.employees[i].getUsername().equals(username)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private boolean isCustomerUsernameExist(String username) {
        boolean isExist = false;
        for (int i = 0; i < this.customers.length; i++) {
            if (this.customers[i].getUsername().equals(username)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private boolean hasDigit(String stringToCheck) {
        boolean hasDigit = false;
        for (int i = 0; i < stringToCheck.length(); i++) {
            if (Character.isDigit(stringToCheck.charAt(i))) {
                hasDigit = true;
                break;
            }
        }
        return hasDigit;
    }

    private void addEmployeeToArray(String firstName, String lastName, String username, String password, boolean isClubMember, int rank) {
        Employee[] newEmployeeArray = new Employee[this.employees.length + 1];
        for (int i = 0; i < this.employees.length; i++) {
            newEmployeeArray[i] = this.employees[i];
        }
        Employee employeeToAdd = new Employee(firstName, lastName, username, password, isClubMember, rank);
        newEmployeeArray[this.employees.length] = employeeToAdd;
        this.employees = newEmployeeArray;
    }

    private void addCustomerToArray(String firstName, String lastName, String username, String password, boolean isClubMember) {
        Customer[] newCustomerArray = new Customer[this.customers.length + 1];
        for (int i = 0; i < this.customers.length; i++) {
            newCustomerArray[i] = this.customers[i];
        }
        Customer customerToAdd = new Customer(firstName, lastName, username, password, isClubMember);
        newCustomerArray[this.customers.length] = customerToAdd;
        this.customers = newCustomerArray;
    }
}
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final int SIGN_UP = 1;
    private static final int SIGN_IN = 2;
    private static final int EXIT = 3;
    private static final int SIGN_IN_CUSTOMER = 1;
    private static final int SIGN_IN_EMPLOYEE = 2;
    private static final int COMPLETION_OF_PURCHASE = -1;
    private static final int PRINT_ALL_CUSTOMERS = 1;
    private static final int PRINT_CLUB_MEMBER_CUSTOMER = 2;
    private static final int PRINT_CUSTOMER_WITH_AN_LEAST_ONE_PURCHASE = 3;
    private static final int PRINT_THE_CUSTOMER_WHOSE_PURCHASE_AMOUNT_HIGHEST = 4;
    private static final int ADDING_PRODUCT_TO_STORE = 5;
    private static final int CHANGE_IF_PRODUCT_IS_IN_STOCK = 6;
    private static final int MAKING_A_PURCHASE = 7;
    private static final int RETURN_TO_THE_MAIN_MENU = 8;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Shop shop = new Shop();
        String userNameToCheck;
        String passwordToCheck;
        int employeeOrCustomer;
        int userChoice;
        int employeeChoice;

        do {
            do {
                System.out.println("1 - Create a new account\n" +
                        "2 - Login to an existing account\n" +
                        "3 - Exit");
                userChoice = scanner.nextInt();
            } while (userChoice != SIGN_UP && userChoice != SIGN_IN && userChoice != EXIT);


            if (userChoice == SIGN_UP) {
                shop.crateAccount();
            }

            if (userChoice == SIGN_IN) {
                System.out.println("--Logging in--");
                do {
                    System.out.println("Are you interested in logging in to an employee's or customer's account" +
                            "\n" + "for customer enter 1" + "\n" + "for employee enter 2");
                    employeeOrCustomer = scanner.nextInt();
                } while (employeeOrCustomer != SIGN_IN_CUSTOMER && employeeOrCustomer != SIGN_IN_EMPLOYEE);


                Scanner scanner1 = new Scanner(System.in);

                System.out.println("Enter your username: ");
                userNameToCheck = scanner1.nextLine();
                System.out.println("Enter your password: ");
                passwordToCheck = scanner1.nextLine();
                if (employeeOrCustomer == SIGN_IN_CUSTOMER) {
                    Customer loggedCustomer = shop.signInCustomer(userNameToCheck, passwordToCheck);
                    if (loggedCustomer == null) {
                        System.out.println("Login failed, The username or password is incorrect");
                    } else {
                        loggedCustomer.helloMessage();
                        System.out.println();

                        if (shop.getProducts().length == 0) {
                            System.out.println("There are no products in stock");
                        }else {
                            int customerProductChoice;
                            do {
                                customerProductChoice = shop.purchase(loggedCustomer);
                                if (customerProductChoice == COMPLETION_OF_PURCHASE && loggedCustomer.getShoppingCart().getTotalPrice() != 0) {
                                    loggedCustomer.addToSumPurchases(loggedCustomer.getShoppingCart().getTotalPrice());
                                    loggedCustomer.purchaseReset();
                                    Date dateOfAcquisition = new Date();
                                    loggedCustomer.setDateOfLastPurchase(dateOfAcquisition);
                                }
                            } while (customerProductChoice != COMPLETION_OF_PURCHASE);
                        }

                    }
                } else {
                    Employee loggedEmployee = shop.signInEmployee(userNameToCheck, passwordToCheck);
                    if (loggedEmployee == null) {
                        System.out.println("Login failed, The username or password is incorrect");
                    } else {
                        loggedEmployee.helloMessage();

                        do {
                            do {
                                System.out.println("Enter your choice: " +
                                        "\n" + "1- Print a list of all customers" +
                                        "\n" + "2- Print a list of customer members in the club" +
                                        "\n" + "3- Print the list of customers who have made at least one purchase" +
                                        "\n" + "4- Print the customer whose purchase amount is the highest" +
                                        "\n" + "5- Adding a new product to the store" +
                                        "\n" + "6- Change inventory status for product" +
                                        "\n" + "7- Making a purchase" +
                                        "\n" + "8- Disconnect and return to the main menu");
                                Scanner scanner2 = new Scanner(System.in);
                                employeeChoice = scanner2.nextInt();
                            } while (employeeChoice > 8 || employeeChoice < 1);


                            switch (employeeChoice) {
                                case PRINT_ALL_CUSTOMERS:
                                    shop.printAllCustomers();
                                    break;
                                case PRINT_CLUB_MEMBER_CUSTOMER:
                                    shop.printTheClubMemberCustomer();
                                    break;
                                case PRINT_CUSTOMER_WITH_AN_LEAST_ONE_PURCHASE:
                                    shop.printCustomersWithAtLeastOnePurchase();
                                    break;
                                case PRINT_THE_CUSTOMER_WHOSE_PURCHASE_AMOUNT_HIGHEST:
                                    shop.printCustomerWithTheTopDollar();
                                    break;
                                case ADDING_PRODUCT_TO_STORE:
                                    shop.addNewProduct();
                                    break;
                                case CHANGE_IF_PRODUCT_IS_IN_STOCK:
                                    shop.changeIsInStock();
                                    break;
                                case MAKING_A_PURCHASE:
                                    int employeeProductChoice;
                                    do {
                                        employeeProductChoice = shop.purchase(loggedEmployee);
                                        if (employeeProductChoice == COMPLETION_OF_PURCHASE && loggedEmployee.getShoppingCart().getTotalPrice() != 0) {
                                            loggedEmployee.purchaseReset();
                                        }
                                    } while (employeeProductChoice != COMPLETION_OF_PURCHASE);
                                    break;
                                case RETURN_TO_THE_MAIN_MENU:
                                    break;
                            }
                        }while (employeeChoice != RETURN_TO_THE_MAIN_MENU);
                    }
                }
            }
        } while (userChoice != EXIT);


    }

}
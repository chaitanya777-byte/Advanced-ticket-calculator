import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdvancedTicketPriceCalculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Integer> userLoyaltyPoints = new HashMap<>();
    private static final double TAX_PERCENTAGE = 10.0;
    private static final double GROUP_DISCOUNT_THRESHOLD = 10;
    private static final double GROUP_DISCOUNT_PERCENTAGE = 5.0;
    private static final double LOYALTY_REDEMPTION_RATE = 1.0; // $1 for 1 point
    private static final double LOYALTY_EARNING_RATE = 10.0; // 1 point per $10 spent

    public static void main(String[] args) {
        System.out.println("Welcome to the Advanced Ticket Price Calculator!");

        while (true) {
            System.out.print("\nEnter your name (or type 'exit' to quit): ");
            String customerName = scanner.nextLine().trim();
            if (customerName.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the Ticket Price Calculator! Goodbye.");
                break;
            }

            // Initialize loyalty points for new users
            userLoyaltyPoints.putIfAbsent(customerName, 0);

            // Dynamic ticket pricing
            double regularPrice = getTicketPrice("Regular");
            double premiumPrice = getTicketPrice("Premium");
            double vipPrice = getTicketPrice("VIP");

            System.out.println("\nTicket Types:");
            System.out.println("1. Regular - $" + regularPrice);
            System.out.println("2. Premium - $" + premiumPrice);
            System.out.println("3. VIP - $" + vipPrice);

            int ticketType = getValidInput("Select the ticket type (1 for Regular, 2 for Premium, 3 for VIP): ", 1, 3);
            int ticketCount = getValidInput("Enter the number of tickets: ", 1, Integer.MAX_VALUE);

            double ticketPrice = switch (ticketType) {
                case 1 -> regularPrice;
                case 2 -> premiumPrice;
                case 3 -> vipPrice;
                default -> 0.0; // Should never happen
            };

            // Apply group discount if applicable
            double groupDiscount = (ticketCount > GROUP_DISCOUNT_THRESHOLD)
                    ? (ticketPrice * ticketCount) * (GROUP_DISCOUNT_PERCENTAGE / 100)
                    : 0.0;

            // Calculate subtotal and tax
            double subtotal = (ticketPrice * ticketCount) - groupDiscount;
            double tax = subtotal * (TAX_PERCENTAGE / 100);

            // Loyalty points redemption
            int loyaltyPoints = userLoyaltyPoints.get(customerName);
            System.out.println("\nYou have " + loyaltyPoints + " loyalty points.");
            System.out.print("Do you want to redeem your points? (yes/no): ");
            boolean redeemPoints = scanner.nextLine().trim().equalsIgnoreCase("yes");
            double loyaltyDiscount = redeemPoints ? Math.min(loyaltyPoints, subtotal) : 0.0;
            double finalPrice = subtotal + tax - loyaltyDiscount;

            // Update loyalty points
            int pointsEarned = (int) (finalPrice / LOYALTY_EARNING_RATE);
            userLoyaltyPoints.put(customerName, loyaltyPoints - (int) loyaltyDiscount + pointsEarned);

            // Generate receipt
            System.out.println("\n--- Receipt ---");
            System.out.println("Customer Name: " + customerName);
            System.out.println("Ticket Type: " + getTicketTypeName(ticketType));
            System.out.println("Number of Tickets: " + ticketCount);
            System.out.println("Ticket Price (per ticket): $" + String.format("%.2f", ticketPrice));
            System.out.println("Subtotal: $" + String.format("%.2f", ticketPrice * ticketCount));
            if (groupDiscount > 0) {
                System.out.println("Group Discount: -$" + String.format("%.2f", groupDiscount));
            }
            if (redeemPoints) {
                System.out.println("Loyalty Points Redeemed: -$" + String.format("%.2f", loyaltyDiscount));
            }
            System.out.println("Tax (" + TAX_PERCENTAGE + "%): $" + String.format("%.2f", tax));
            System.out.println("Final Price: $" + String.format("%.2f", finalPrice));
            System.out.println("Loyalty Points Earned: " + pointsEarned);
            System.out.println("New Loyalty Points Balance: " + userLoyaltyPoints.get(customerName));
            System.out.println("----------------");

            System.out.print("\nDo you want to calculate another ticket? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.println("Thank you for using the Ticket Price Calculator! Goodbye.");
                break;
            }
        }
    }

    private static double getTicketPrice(String ticketType) {
        System.out.print("Enter the price for " + ticketType + " Ticket: $");
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a valid price for " + ticketType + " Ticket: $");
            scanner.next();
        }
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return price;
    }

    private static int getValidInput(String prompt, int min, int max) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (value < min || value > max) {
            System.out.print("Input out of range. " + prompt);
            return getValidInput(prompt, min, max);
        }
        return value;
    }

    private static String getTicketTypeName(int ticketType) {
        return switch (ticketType) {
            case 1 -> "Regular";
            case 2 -> "Premium";
            case 3 -> "VIP";
            default -> "Unknown";
        };
    }
}


/*Welcome to the Advanced Ticket Price Calculator!

Enter your name (or type 'exit' to quit): John
Enter the price for Regular Ticket: $15
Enter the price for Premium Ticket: $25
Enter the price for VIP Ticket: $50

Ticket Types:
1. Regular - $15.0
2. Premium - $25.0
3. VIP - $50.0
Select the ticket type (1 for Regular, 2 for Premium, 3 for VIP): 2
Enter the number of tickets: 12

You have 0 loyalty points.
Do you want to redeem your points? (yes/no): no

--- Receipt ---
Customer Name: John
Ticket Type: Premium
Number of Tickets: 12
Ticket Price (per ticket): $25.00
Subtotal: $300.00
Group Discount: -$15.00
Tax (10.0%): $28.50
Final Price: $313.50
Loyalty Points Earned: 31
New Loyalty Points Balance: 31
----------------

Do you want to calculate another ticket? (yes/no): no
Thank you for using the Ticket Price Calculator! Goodbye.
 */
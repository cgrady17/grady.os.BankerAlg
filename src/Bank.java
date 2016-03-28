/**
 * Interface for the Bank in the Banker's Algorithm.
 *
 * @author Connor Grady
 * @version 1.0
 */
public interface Bank {
    /**
     * Adds a Customer to the Bank.
     * @param custNum The Number of the new Customer.
     * @param maxDemand The maximum demand for this Customer.
     */
    void addCustomer(int custNum, int[] maxDemand);

    /**
     * Outputs the value of available, maximum, allocation, and need.
     */
    void getState();

    /**
     * Requests the specified resources for the specified Customer.
     * @param custNum The number of the Customer requesting the resources.
     * @param request The resources being requested.
     * @return True/False indicating if the resources were granted.
     */
    boolean requestResources(int custNum, int[] request);

    void releaseResources(int custNum, int[] release);
}

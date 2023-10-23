package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class TransactionDataFetcher {

    public static void main(String[] args) {
        TransactionDataFetcher tdf = new TransactionDataFetcher();
        System.out.println("Total Transaction Amount: " + tdf.getTotalTransactionAmount());
        System.out.println("Total Transaction Amount Sent by Sender Tom Shelby: "+ tdf.getTotalTransactionAmountSentBy("Tom Shelby"));
        System.out.println("Highest value in transactions: " + tdf.getMaxTransactionAmount());
        System.out.println("Number of Unique clients: " + tdf.countUniqueClients());
        System.out.println("Does Tom Shelby have open compliance issue: " + tdf.hasOpenComplianceIssues("Billy Kimber"));
        System.out.println("UnsolvedIssueIds issues: " + tdf.getUnsolvedIssueIds());
        System.out.println("AllSolvedIssueMessages issues: " + tdf.getAllSolvedIssueMessages());
     System.out.println("Top3TransactionsByAmount issues: " + tdf.getTop3TransactionsByAmount());
          System.out.println(" <<< TopSender >>>: " + tdf.getTopSender());




        tdf.getTransactionsByBeneficiaryName();
    }


    // Using jackson ObjectMapper to deserialize transactions.json into a DTO
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        double totalTransactionAmount = 0;
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            for(Transaction transaction : transactions) {
                totalTransactionAmount = totalTransactionAmount + transaction.getAmount();
            }
            return totalTransactionAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        double totalTransactionAmountSentBy = 0;
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            boolean senderExists = transactions.stream().filter(transaction -> senderFullName.equalsIgnoreCase(transaction.getSenderFullName())).findFirst().isPresent();
            if(senderExists) {
                for(Transaction transaction : transactions) {
                    if(transaction.getSenderFullName().equalsIgnoreCase(senderFullName)) {
                        totalTransactionAmountSentBy = totalTransactionAmountSentBy + transaction.getAmount();
                    }
                }
            } else {
                throw new Exception("Sender does not exist");
            }
            return totalTransactionAmountSentBy;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        double maxTransactionAmount = 0;
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            maxTransactionAmount = transactions.stream().mapToDouble(value -> value.getAmount()).max().orElseThrow(NoSuchElementException::new);
            return maxTransactionAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            List<String> clients = new ArrayList<String>();
            for(Transaction transaction : transactions) {
                clients.add(transaction.getSenderFullName());
                clients.add(transaction.getBeneficiaryFullName());
            }
            return clients.stream().distinct().count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            boolean clientExists = transactions.stream()
                    .filter(transaction -> clientFullName.equalsIgnoreCase(transaction.getSenderFullName()) || clientFullName.equalsIgnoreCase(transaction.getBeneficiaryFullName()))
                    .findFirst().isPresent();
            if(clientExists) {
                for(Transaction transaction : transactions) {
                    if(transaction.getSenderFullName().equalsIgnoreCase(clientFullName) && !transaction.isIssueSolved()) {
                        return true;
                    } 
                }
            } else {
                throw new Exception("Client does not exist");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public void getTransactionsByBeneficiaryName() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Map<String, List<Transaction>> transactionMap = new HashMap<String, List<Transaction>>();
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            
            // Add unique beneficiaries:
            Set<String> beneficiaries = new HashSet<String>();
            for(Transaction transaction : transactions) {
                beneficiaries.add(transaction.getBeneficiaryFullName());
            }

            // Convert beneficiary set to list to retrieve later on
            List<String> beneficiaryList = new ArrayList<String>(beneficiaries);
            for(int i = 0; i < beneficiaryList.size(); i++) {
                List<Transaction> transactionsByBenificiaryList = new ArrayList<Transaction>();
                for(int j = 0; j < transactions.size(); j++) {
                    if(beneficiaryList.get(i).equalsIgnoreCase(transactions.get(j).getBeneficiaryFullName())) {
                        transactionsByBenificiaryList.add(transactions.get(j));
                    }
                }
                transactionMap.put(beneficiaryList.get(i), transactionsByBenificiaryList);
            }
            for(int i = 0; i < beneficiaryList.size(); i++) {
                System.out.println("==========================\nTransactions for " + beneficiaryList.get(i) + ": \n==========================");
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(transactionMap.get(beneficiaryList.get(i))));
            }
	    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
List<Transaction> transactions = new ArrayList<Transaction>();
        Map<String, List<Transaction>> transactionMap = new HashMap<String, List<Transaction>>();
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            
            // Add unique beneficiaries:
            Set<Integer> beneficiaries = new HashSet<Integer>();
            for(Transaction transaction : transactions) {
                if(transaction.isIssueSolved() == false)
                beneficiaries.add(transaction.getIssueId());
            }
            return beneficiaries;
        }
            catch(Exception e){
            e.printStackTrace();
    }
  return null; 
}

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
List<Transaction> transactions = new ArrayList<Transaction>();
        
        try {
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            
            // Add solvIssueMessage:
             List<String> solvIssueMessage = new ArrayList<>();
            for(Transaction transaction : transactions) {
                if(transaction.isIssueSolved() && transaction.getIssueMessage()!= null)
                solvIssueMessage.add(transaction.getIssueMessage());
            }
            return solvIssueMessage;
        }
            catch(Exception e){
            e.printStackTrace();
    }
throw new UnsupportedOperationException();
}    

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> transactions = new ArrayList<Transaction>();

        try{
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            
            //  New Array List:
            List<Transaction> top3TransactionsByAmount = new ArrayList();
            for(Transaction transaction : transactions) {
                top3TransactionsByAmount.add(transaction);
            }

             return top3TransactionsByAmount
                .stream()
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount())) // Sort in descending order
                .limit(3) // Get the top 3 transactions
                .toList();
        
        }catch(Exception e){
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() 
        {
        List<Transaction> transactions = new ArrayList<Transaction>();

        try{
            transactions = mapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>(){});
            
            
        // Create a map to store the total sent amounts per sender
        Map<String, Double> senderTotalAmounts = new HashMap<>();

        // Calculate the total sent amounts for each sender
        for (Transaction transaction : transactions) {
            String senderFullName = transaction.getSenderFullName();
            double amount = transaction.getAmount();

            senderTotalAmounts.put(senderFullName, senderTotalAmounts.getOrDefault(senderFullName, 0.0) + amount);
        }

        // Find the sender with the most total sent amount
        Optional<Map.Entry<String, Double>> senderWithMostSentAmountOptional = senderTotalAmounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        Optional<String> senderWithMostSentAmount = senderWithMostSentAmountOptional.map(Map.Entry::getKey);

        System.out.println("Sender with the most total sent amount: " + senderWithMostSentAmount);
        return senderWithMostSentAmount;
    
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }
}

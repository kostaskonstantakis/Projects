public class ComputeTotalsPerCustomer {
    
    private Account acnt;
    
    public static Account ComputeCustomerTotals(Account account)
    {
        if(account==null || account.Id==null)
            throw new IllegalArgumentException('Account and its id cannot be null! Try again!');
        
        AggregateResult[] groupedResults = [select   Customer__c    ,  sum(Total_Quoted_Amount__c)  totalAmount ,
                                            count(id)   totalQuotes,
                                            count(Total_quoted_Items__c) totalQuotedItems
                                            from Quote__c 
                                            where Status__c  = 'Closed'
                                            and Result__c  ='Won'
                                            group by Customer__c ];
        
        for(AggregateResult  result :  groupedResults){
            if(groupedResults[0].get('totalAmount') != null)
            { 
                system.debug(result.get('totalAmount')) ; 
                system.debug(result.get('totalQuotes')) ; 
                system.debug(result.get('totalQuotedItems')) ;
                decimal d1 = decimal.valueOf(string.valueOf(result.get('totalAmount')));
                decimal d2 = decimal.valueOf(string.valueOf(result.get('totalQuotes')));
                decimal d3 = decimal.valueOf(string.valueOf(result.get('totalQuotedItems')));
            }
		}
        
        return account;
        
        
    }
    
    public static Account ComputeCustomerTotals(Id id)
    {
        Account ac=new Account(Id=id);
        return ComputeCustomerTotals(ac);
        
    }

}
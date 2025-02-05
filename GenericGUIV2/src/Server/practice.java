package Server;

public class practice {

	public void result() {
	   int k=3;
		//int  arr[] = new int[n];
		   int arr1[] = { 1, 2 ,3, 1, 4, 5 ,2,3, 6 };
		    int n = arr1.length;
			int max=0;
		for(int i=0;i<=n-k;i++) 
		{
		int sum=0;
//		System.out.print(i+" ");
	
			for(int j=0;j<k;j++) {
	
			sum=sum+arr1[i+j];
			
				
			}
			
			if(max<sum) {
				max=sum;
			}{
				
			}
			
			//System.out.print(sum+" " );

		
		}
		//System.out.print(max+" " );
		
		 // Compute sum of first window of size k
        int max_sum = 0;
        for (int i = 0; i < k; i++)
            max_sum += arr1[i];

        // Compute sums of remaining windows by
        // removing first element of previous
        // window and adding last element of
        // current window.
        int window_sum = max_sum;
        for (int i = k; i < n; i++) {
        	//  System.out.println(i+" ---"+ (i-k) );
            window_sum += arr1[i] - arr1[i - k];
            max_sum = Math.max(max_sum, window_sum);
            System.out.println(max_sum );
        }
        int min =arr1[0];
        for (int i = 0; i < n-k; i++) {
        	 
           for(int j=0;j<k;j++) {
        	   if(min>arr1[j]) {
        		   
        		   min=arr1[j];
        	   }
           }
           System.out.print(min);
           
        }
        
      
        //return max_sum;
	}
	
	
	public static void main(String[] args)
    {
		practice obj = new practice();
		obj.result();
		
	}
	
}

#include <iostream>
#include <fstream>
using namespace std;

int main() {
    
int a[200];
int sum=0;
int product=0;
try {
	ifstream file; //input file
	file.open("input.txt");
	for(int i=0;i<200;i++) file>>a[i];
		file.close();
		cout<<"I'm gonna find 3 random numbers that sum to 2020, and multiply them, too."<<endl;
		for(int j=0;j<200;j++) 
		{
			 for(int z=j+1;z<200;z++) 
			 {

				for(int x=z+1;x<200;x++)
	                        {
					sum=a[j]+a[z]+a[x];
					if(sum==2020) 
					{
					  product=a[j]*a[z]*a[x];
					  cout<<a[j]<<"+"<<a[z]<<"+"<<a[x]<<"="<<sum<<".\t"<<a[j]<<"*"<<a[z]<<"*"<<a[x]<<"="<<product<<"."<<endl;
                   			}
			  	}		
			 }
		}
     } catch (const exception& e) {
     	cout << e.what()<<endl;
    }
    return 0;
}

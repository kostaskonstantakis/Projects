
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
		cout<<"I'm gonna find 2 random numbers that sum to 2020, and multiply them, too."<<endl;
		for(int j=0;j<200;j++) 
		{
			 for(int z=j+1;z<200;z++) 
			 {
					sum=a[j]+a[z];
					if(sum==2020) 
					{
					  product=a[j]*a[z];
					  cout<<a[j]<<"+"<<a[z]<<"="<<sum<<".\t"<<a[j]<<"*"<<a[z]<<"="<<product<<"."<<endl;
                   			}		
			 }
		}
     } catch (const exception& e) {
     	cout << e.what()<<endl;
    }
    return 0;
}

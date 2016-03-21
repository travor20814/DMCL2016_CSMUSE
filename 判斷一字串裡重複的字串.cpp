#include <iostream>
#include<cstdio>
#include<string>
#include<stdio.h>
using namespace std;

int main()
{
	char A[100];
	int L;
	int tempA=0,tempB=0;
	int flag=0;
	while (gets_s(A) && A[0] != '\0' )
	{
		cout << A << endl;
		L = strlen(A);
		cout << "L:" << L << endl;

		for (int i = 1;i<L; i++)
		{
			if (A[0] == A[i])
			{
				tempA = i; 
				tempB = L / tempA;
				for (int m = 0; m < tempA; m++)
				{
					for (int n=tempA;n<L;n+=tempA)
					{

						if (A[m] != A[m+n])
						{
							flag = 0;
							break;
						}
						else
						{
							flag = 1;
						}
					}
					cout << "A~";
				}
				if (flag == 1)
				{
					cout << "³Ìµu:" << tempA << endl;
					system("pause");
					break;
				}

			}
		}

	}

	system("pause");
	return 0;
}
#Python Basics
#https://www.geeksforgeeks.org/python-programming-examples

#implicit function
def factorial(n):
	return 1 if (n==1 or n==0) else n * factorial(n - 1);
	
#Class Structure:
	
class PyBasicExample:

	def compound_interest(self, principle, rate, time):
		amount = principle * (pow((1 + rate / 100), time))
		compInterest = amount - principle
		return compInterest
	
	@staticmethod
	def simple_interest( principle, rate, time):
		simpleInterest = principle * rate / 100 * time
		return simpleInterest		
	 
	def execute(self):
		print("\nFactorial of 100 is:",factorial(100))
		interestAmount = self.compound_interest(10000, 10.25, 5)
		print("\nCompound interest for amount, rate, time: {10000, 10.25, 5} is: ", round(interestAmount,2))
		interestAmount = self.simple_interest(10000, 10.25, 5)
		print("\nSimple interest for amount, rate, time: {10000, 10.25, 5} is: ", interestAmount)		

PyBasicExample().execute()

#TODO: Try out independent program for Armstrong number check: https://www.geeksforgeeks.org/program-for-armstrong-numbers/

#Operators:
#Arithmetic Operators : +, -, *, /, %, **(Exponent), //(Floor Division)
#Relational Operators : ==, != , < , >, >=, <=
#Assignment Operators : =, +=, -=, *=, /=, %=, **= ,//=
#Logical Operators : and, or, not
#Bitwise Operators : &, |, ^, ~,  << , >>
#Membership Operators : in, not in
#sIdentity Operators : is, is not

#Conditional Statements:

if( 15 > 10 ): print( "\n15 is greater than 10")


#Datatypes:
#Number: int, float, complex ; String ; Collections: array/list/set,Dictionary ; Tuple
#type(data_variable) gives the type..

class BasicDataStructures:
#Basic datastructures:

	#Array:
	arr = [12, 3, 4, 15, [3,7] ] 
	size = len(arr)
	sumValue = sum(arr[0:3])
	thirdElement = arr[2]


	def rotateArray(self, arr,d,n):
		import copy
		newArray = copy.deepcopy(arr) 
		# or use copy.copy if its flat array...
		newArray[:]=newArray[d:n]+newArray[0:d]
		return newArray
 
	def execute(self):
		print("\nBase array: ", self.arr, " Array size: " , self.size, " Array sum: " , self.sumValue , " 3rd Element: " , self.thirdElement, " rotated array: " , self.rotateArray(self.arr,2,len(self.arr)))
		#List
		baseList = [] 
		baseList.append("5_Test01");  baseList.append("4_Test002"); baseList.append("1_Test0003"); baseList.append("2_Test00004");
		print( "\nSort List: " , baseList.sort(), " length is: ", len(baseList))
		if "4_Test002" in baseList:
			print("\nTest element exists")
		#Tuple
		tuple01 = (( 11, "Testing 11"), ( 9, "Testing 9") )
		print("\nDictionary from tuple: " , dict(tuple01) )

BasicDataStructures().execute()
#Exception handling

try: 
	15/0 
except: 
	print("\nZero Division Error Occurred" ) ; 
else: 
	print("No Exception occurred" ) ; 
finally: 
	print("Execute this finally");
	
	
#Manipulating collections: https://kaizen.itversity.com/courses/hdpcsd-hdp-certified-spark-developer-hdpcsd-python/lessons/hdpcsd-basics-of-programming-using-python/topic/hdpcsd-manipulating-collections-using-map-reduce-apis-python-3-python/


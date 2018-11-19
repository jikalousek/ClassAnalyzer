# ClassAnalyzer
## Introduction
The ClassAnalyzer project focuses on tracking the code execution during JUnit testing in Java. Data collected when running test cases are exported to a csv file. A programmer can easily find out which line was executed during which test case. Moreover, this tool also allows to compute suspiciousness of each tested line of code. As a result, a programmer should be able to quickly find bugs in his code.

This project makes use of [JaCoCo Java Code Coverage Library](https://www.eclemma.org/jacoco/).

## How to use it
1) Add _ClassAnalyzer.jar_ to your project libraries.
2) Create an interface (e.g. `IMyClass`) for the class that needs to be tested. Make sure that the interface contains all methods that you want to test. Then your tested class (e.g. `MyClass`) must implement this interface.
3) Create JUnit test class for the tested class. The test class must extend `ClassAnalyzer` with the interface as a generic type (e.g. `class MyClassTest extends ClassAnalyzer\<IMyClass\>`).
4) Call `super(MyClass.class)` from the constructor of the test class.
5) Then, in test cases, call `super.newInstrumentedInstance()` to get an instance of the tested class instead of calling `new MyClass()`.
6) After each "assert", you need to call `super.saveInstrumentationResults(<testName>, <testResult>, <arg1, arg2, ...>)` to save the coverage data.
7) Open the generated csv file _mypackage_myclass_mytestname.csv_ to see the coverage results.

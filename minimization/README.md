-- Definition of Input and Output

Input file:

First line:	start state (only one start state is allowed)
Second line:	final states (at least one final state should be declared)

From the third line to the end of file:
		Each line declares one transition: fromState  toState  input

Output file:
Start State:	XX
Final States:	XX	XX	XX
Transitions:	
XX	XX	XX
XX	XX	XX
...    ...      ...
------------------------------------------------------------
Maps:
XX 	replaces	XX
XX	replaces	XX
...	replaces	..


-- Usage

Run the JAR file, and at the same time indicate the input file:

java	–jar	minimization.jar	the_input_file

If the user doesn’t indicate the input file path, nothing would happen.

Then after minimization, in the same directory of the input file, the output file has been generated:	test_minimization.fsm



<b>-- Definition of Input and Output</b>

<b>Input file:</b>

First line:	start state (only one start state is allowed)

Second line:	final states (at least one final state should be declared)

From the third line to the end of file:

		Each line declares one transition: fromState  toState  input

<b>Output file:</b>

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

<b>-- Usage</b>

Run the JAR file, and at the same time indicate the input file:

java	–jar	minimization.jar	the_input_filename.fsm

If the user doesn’t indicate the input file path, nothing would happen.

Then after minimization, in the same directory of the input file, the output file will be generated:	the_input_filename_minimization.fsm



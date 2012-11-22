The software tool PYTHIA was developed at Bell Laboratories for automatically generating test sequences for protocol data portions, and I participated in the development and improvement of the PYTHIA project during my internship there.

In the paper by David Lee and Mihalis Yannakakis: 'PYTHIA: A Software Tool for Testing Data Portions of Network Protocols', Finite State Machine (FSM) and Extended Finite State Machine (EFSM) are defined to model protocol control and data portions, respectively. Then they went ahead and proved that a deterministic EFSM with finite variable domains is a compact representation of an FSM.

In Conformance Testing, we need to check whether an implementation conforms to its specification. However, testing the structural isomorphism of the implementation EFSM and the specification EFSM is almost impossible, for the Graph Isomorphism Problem is an NP-hard problem neither known to be solvable in polynomial time nor NP-Complete. Thus, a practical solution to conformance testing is to construct a set of test sequences of a desirable fault coverage which ensures that the implementation machine under test conforms to the specification.

A practical criterion of fault coverage is that each transition in the specification EFSM has to be executed at least once. For each transition in the EFSM, we can designate a distinct color to it, and it is also carried over to the induced transitions in the reachability graph, which is an FSM itself. We want to find tests such that all the colors are covered, i.e., the corresponding transitions are executed. Note that the same color may have multiple appearances in the reachability graph. As a result, a complete test set for conformance testing of an EFSM with a color assignment of color set C is a set of test sequences such that each color in C is covered by at least one sequence. For more details, please refer the Lee & Yannakakis's paper.

My major contribution to the PYTHIA projects are the following:

1. Redefined a clear file format for FSM and EFSM

2. Created a parser using Lex & Yacc for parsing FSM and EFSM

3. Studied the State Explosion problem of the reachability graph of EFSM

4. Implemented the Minimization algorithm for reachability graph, which itself is an FSM